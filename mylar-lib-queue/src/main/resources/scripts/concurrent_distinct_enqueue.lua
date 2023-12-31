--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2023/12/23 0023
-- Time: 19:42
-- To change this template use File | Settings | File Templates.
--

-- 等待中队列缓存键（list）
local waiting_queue_key = KEYS[1]

-- 等待中数据缓存键（hash）
local waiting_data_key = KEYS[2]

-- 执行中数据缓存键（hash）
local running_data_key = KEYS[3]

-- 数据ID（唯一键）
local data_id = ARGV[1]

-- 数据内容
local data_content = ARGV[2]

-- 等待中数据的数量上限
local waiting_data_capacity = tonumber(ARGV[3])

-- 等待中数据的过期时间（单位：秒）
local waiting_data_expire = tonumber(ARGV[4])

-- 执行中数据缓存键后缀
local running_data_key_suffix = tonumber(ARGV[5])

-- 当等待中已存在时更新数据内容，0 不更新 1 更新
local update_when_waiting_exist = tonumber(ARGV[6])

-- 执行中数据缓存键（当前）
local current_running_data_key = running_data_key .. running_data_key_suffix

-- 执行中数据缓存键（前任）
local before_running_data_key = running_data_key .. (running_data_key_suffix - 1)

-- 判断等待中数据中是否已存在，已存在则直接返回
if 1 == redis.call("hexists", waiting_data_key, data_id) then

    -- 当等待中已存在时更新数据内容
    if 1 == update_when_waiting_exist then

        -- 更新数据内容
        redis.call("hset", waiting_data_key, data_id, data_content)
    end

    -- 1 代表 “等待中已存在”
    return 1
end

-- 判断执行中数据中是否包含，已存在则直接返回
if 1 == redis.call("hexists", current_running_data_key, data_id) or 1 == redis.call("hexists", before_running_data_key, data_id) then

    -- 4 代表 “执行中已存在”
    return 4
end

-- 获取等待中的数量
local waiting_count = redis.call("hlen", waiting_data_key)

-- 等待中的数量小于容量上限
if waiting_count < waiting_data_capacity then

    -- 添加到等待中队列
    redis.call("lpush", waiting_queue_key, data_id)

    -- 设置过期时间
    redis.call("expire", waiting_queue_key, waiting_data_expire)

    -- 添加到等待中数据
    redis.call("hset", waiting_data_key, data_id, data_content)

    -- 设置过期时间
    redis.call("expire", waiting_data_key, waiting_data_expire)

    -- 2 代表 “添加到等待中”
    return 2
else

    -- 3 代表 “等待中数量超限”
    return 3
end