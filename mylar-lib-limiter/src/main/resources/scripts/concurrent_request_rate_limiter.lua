--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2023/4/15 0015
-- Time: 14:26
-- To change this template use File | Settings | File Templates.
--

-- 限流键
local limit_key = KEYS[1]

-- 请求唯一 ID
local id = KEYS[2]

-- 限流上限
local capacity = tonumber(ARGV[1])

-- 当前时间
local now = tonumber(ARGV[2])

-- 超时时间
local timeout = tonumber(ARGV[3])

-- 清除超时数据
redis.call('zremrangebyscore', limit_key, 0, now - timeout)

-- 获取执行中的请求数量
local count = redis.call("zcard", limit_key)

-- 允许通过数量
local allowed = 0

-- 执行中的请求数量 < 限流上限：允许通过，添加当前请求到集合中
if count < capacity then
    redis.call("zadd", limit_key, now, id)
    allowed = 1
    count = count + 1
end

-- 返回结果
return { allowed, count }

