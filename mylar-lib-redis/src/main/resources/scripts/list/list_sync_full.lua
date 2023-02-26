-- List全量更新
-- User: wangz
-- Date: 2022/12/13 14:02

-- 缓存键
local cacheKey = KEYS[1]

-- 缓存键过期时间
local cacheKeyExpire = tonumber(ARGV[1])

-- 参数列表长度
local len = #ARGV

-- 删除缓存键
redis.call("del", cacheKey)

-- 循环插入
for i = 2, len do
    redis.call("rpush", cacheKey, ARGV[i])
end

-- 设置过期时间
if cacheKeyExpire > 0 then
    redis.call("expire", cacheKey, cacheKeyExpire)
end

return 0