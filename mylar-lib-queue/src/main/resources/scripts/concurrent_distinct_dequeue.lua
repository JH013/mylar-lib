--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2023/12/25 0025
-- Time: 23:05
-- To change this template use File | Settings | File Templates.
--

-- 等待中队列缓存键（list）
local waiting_queue_key = KEYS[1]

-- 等待中数据缓存键（hash）
local waiting_data_key = KEYS[2]

-- 执行中数据缓存键（hash）
local running_data_key = KEYS[3]

-- 执行中数据的数量上限
local running_data_capacity = tonumber(ARGV[1])

-- 执行中数据的过期时间（单位：秒）
local running_data_expire = tonumber(ARGV[2])

-- 执行中数据缓存键后缀
local running_data_key_suffix = tonumber(ARGV[3])

-- 出队数量
local dequeue_count = tonumber(ARGV[4])

-- 执行中数据缓存键（当前）
local current_running_data_key = running_data_key .. running_data_key_suffix

-- 执行中数据缓存键（前任）
local before_running_data_key = running_data_key .. (running_data_key_suffix - 1)

-- 获取执行中的数量
local running_count = redis.call("hlen", current_running_data_key) + redis.call("hlen", before_running_data_key)

-- 执行中的数量大于容量上限
if running_count >= running_data_capacity then

    -- 返回空
    return nil
end

-- 数据ID（唯一键）
local data_id

-- 数据内容
local data_content

-- 已出队数量
local dequeued_count = 0;

-- 游标
local cursor = 1;

-- 结果集
local ret = {}

-- 循环出队
while (dequeued_count < dequeue_count)
do

    -- 数据ID出队
    data_id = redis.call('rpop', waiting_queue_key) or nil

    -- 队列没有数据，直接跳出循环
    if data_id == nil then
        break
    else

        -- 获取数据内容
        data_content = redis.call("hget", waiting_data_key, data_id) or nil

        -- 数据内容为空
        if data_content == nil then

            -- 说明是无效数据，移除掉冗余数据
            redis.call('lrem', waiting_queue_key, 0, data_id)
        else

            -- 封装结果集
            ret[cursor] = data_id
            ret[cursor + 1] = data_content

            -- 游标 + 2
            cursor = cursor + 2

            -- 已出队数量累加
            dequeued_count = dequeued_count + 1

            -- 删除等待中数据
            redis.call('hdel', waiting_data_key, data_id)
        end
    end
end

-- 没有数据出队，直接返回空
if data_content == nil then
    return nil
end

-- 添加到执行中数据
redis.call("hset", current_running_data_key, data_id, data_content)

-- 更新执行中数据的过期时间
redis.call("expire", current_running_data_key, running_data_expire)

-- 删除等待中数据
redis.call('hdel', waiting_data_key, data_id)

-- 返回结果
return ret