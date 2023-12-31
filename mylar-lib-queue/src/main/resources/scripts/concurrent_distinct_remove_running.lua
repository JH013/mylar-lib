--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2023/12/25 0025
-- Time: 23:05
-- To change this template use File | Settings | File Templates.
--

-- 执行中数据缓存键（hash）
local running_data_key = KEYS[1]

-- 执行中数据缓存键后缀
local running_data_key_suffix = tonumber(ARGV[1])

-- 执行中数据缓存键（当前）
local current_running_data_key = running_data_key .. running_data_key_suffix

-- 执行中数据缓存键（前任）
local before_running_data_key = running_data_key .. (running_data_key_suffix - 1)

-- 参数长度（参数内容：数据ID集合）
local len = #ARGV

-- 删除数量
local ret = 0

-- 循环删除
for i = 2, len, 1 do

    -- 数据ID
    local data_id = ARGV[i]

    -- 删除执行中数据（前任）
    local delete_count = redis.call('hdel', before_running_data_key, data_id)

    -- 删除执行中数据（当前）
    if delete_count == 0 then
        delete_count = redis.call('hdel', current_running_data_key,  data_id)
    end

    -- 删除数量累加
    ret = ret + delete_count
end

-- 返回结果
return ret