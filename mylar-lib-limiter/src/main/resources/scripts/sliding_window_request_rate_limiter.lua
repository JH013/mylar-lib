--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2023/4/15 0015
-- Time: 2:23
-- To change this template use File | Settings | File Templates.
--

-- 限流键
local limit_key = KEYS[1]

-- 时间戳键
local timestamp_key = KEYS[2]

-- 子限流周期（子窗口大小），单位：秒
local sub_time_cycle = tonumber(ARGV[1])

-- 子限流周期数量（子窗口数量）
local cycle_count = tonumber(ARGV[2])

-- 限流上限
local capacity = tonumber(ARGV[3])

-- 当前时间
local now = tonumber(ARGV[4])

-- 当前子窗口时间
local window_current = now / sub_time_cycle * sub_time_cycle

-- 开始子窗口时间
local window_start = window_current - (cycle_count - 1) * sub_time_cycle;

-- 获取当前窗口内已通过的请求总数
local last_requested = 0
local exists_key = redis.call('exists', limit_key)
if (exists_key == 1) then
    last_requested = redis.call('zcount', limit_key, window_start, window_current);
end

-- 计算剩余可通过请求数量
local remain_request = capacity - last_requested

-- 允许通过数量
local allowed_num = 0

-- 已通过数量 < 限流上限：允许通过，添加当前请求到集合中
if (last_requested < capacity) then
    remain_request = remain_request - 1
    allowed_num = 1
    redis.call('zadd', limit_key, window_current, timestamp_key)
end

-- 清除过期窗口的数据
redis.call('zremrangebyscore', limit_key, 0, window_start)

-- 设置过期时间
redis.call('expire', limit_key, sub_time_cycle * cycle_count)

-- 返回结果
return { allowed_num, remain_request }

