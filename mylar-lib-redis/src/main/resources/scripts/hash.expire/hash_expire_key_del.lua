-- 支持 HashKey 过期 - 删除缓存键

-- 原始缓存键
local cacheKey = KEYS[1]

-- 缓存键过期时间
local cacheKeyExpire = tonumber(ARGV[1])

-- 当前时间
local currentTime = tonumber(ARGV[2])

-- 缓存键后缀
local cacheKeySuffix = math.floor(currentTime / cacheKeyExpire)

-- 当前缓存键
local currentCacheKey = cacheKey .. cacheKeySuffix

-- 前任缓存键
local beforeCacheKey = cacheKey .. (cacheKeySuffix - 1)

-- 删除当前缓存键
redis.call("del", currentCacheKey)

-- 删除前任缓存键
redis.call("del", beforeCacheKey)

return 1
