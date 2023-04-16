--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2023/4/15 0015
-- Time: 1:38
-- To change this template use File | Settings | File Templates.
--

-- 限流键
local tokens_key = KEYS[1]

-- 时间戳键
local timestamp_key = KEYS[2]

-- 每秒填充速率
local rate = tonumber(ARGV[1])

-- 限流上限
local capacity = tonumber(ARGV[2])

-- 当前时间
local now = tonumber(ARGV[3])

-- 请求数量
local requested = tonumber(ARGV[4])

-- 填充时间
local fill_time = capacity / rate

-- 缓存键过期时间
local ttl = math.floor(fill_time * 2)

-- 获取剩余令牌数量
local last_tokens = tonumber(redis.call("get", tokens_key))
if last_tokens == nil then
    last_tokens = capacity
end

-- 获取上次刷新时间
local last_refreshed = tonumber(redis.call("get", timestamp_key))
if last_refreshed == nil then
    last_refreshed = 0
end

-- 计算填充后的令牌数量
local delta = math.max(0, now - last_refreshed)
local filled_tokens = math.min(capacity, last_tokens + (delta * rate))

-- 是否允许通过：剩余令牌数量 >= 请求数量
local allowed = filled_tokens >= requested

-- 当前剩余令牌数量
local new_tokens = filled_tokens

-- 允许通过数量
local allowed_num = 0

-- 允许通过：计算当前剩余令牌数量和允许通过数量
if allowed then
    new_tokens = filled_tokens - requested
    allowed_num = requested
end

-- 更新限流键：当前剩余令牌数量
redis.call("setex", tokens_key, ttl, new_tokens)

-- 更新时间戳键：令牌最后刷新时间
redis.call("setex", timestamp_key, ttl, now)

-- 返回结果
return { allowed_num, new_tokens }

