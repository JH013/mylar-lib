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

-- 出队数量
local dequeue_count = tonumber(ARGV[1])

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

-- 返回结果
return ret