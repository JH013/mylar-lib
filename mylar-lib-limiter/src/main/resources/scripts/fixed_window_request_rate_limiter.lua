--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2023/4/15 0015
-- Time: 1:38
-- To change this template use File | Settings | File Templates.
--

-- 限流键
local limit_key = KEYS[1]

-- 限流周期（窗口大小），单位：秒
local ttl = tonumber(ARGV[1])

-- 限流上限
local capacity = tonumber(ARGV[2])

-- 请求数量
local requested = tonumber(ARGV[3])

-- 获取当前窗口内已通过请求数量
local current_count = tonumber(redis.call('get', limit_key))

-- 限流键不存在：时间窗口已过期，重新设置限流键和过期时间
if current_count == nil then
    redis.call("setex", limit_key, ttl, 0)
    current_count = 0
end

-- 计算剩余可通过请求数量
local remains = capacity - current_count - requested
if remains < 0 then
    -- 拒绝
    return { 0, 0 }
else
    -- 通过：累计请求量
    redis.call("incrby", limit_key, requested)
    return { requested, remains }
end