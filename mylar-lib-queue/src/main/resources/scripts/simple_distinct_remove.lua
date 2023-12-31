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

-- 参数长度（参数内容：数据ID集合）
local len = #ARGV

-- 删除数量
local ret = 0

-- 循环删除
for i = 1, len, 1 do

    -- 数据ID
    local data_id = ARGV[i]

    -- 删除等待中队列
    redis.call('lrem', waiting_queue_key, 1, data_id)

    -- 删除等待中数据
    local delete_count = redis.call("hdel", waiting_data_key, data_id)

    -- 删除数量累加
    ret = ret + delete_count
end

-- 返回结果
return ret