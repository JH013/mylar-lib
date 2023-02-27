-- 支持 HashKey 过期 - 批量删除HashKey

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

-- 遍历参数
local len = #ARGV
local ret = 0
for i = 3, len, 1 do

    -- 删除当前缓存键的 HashKey
    local count = redis.call("hdel", currentCacheKey, ARGV[i])

    -- 删除前任缓存键的 HashKey
    if count == 0 then
        count = redis.call("hdel", beforeCacheKey, ARGV[i])
    end
    ret = ret + count
end
return ret

