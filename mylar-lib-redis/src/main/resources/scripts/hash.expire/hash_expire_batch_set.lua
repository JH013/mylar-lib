-- 支持 HashKey 过期 - 批量更新

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

-- 转换Hash值，数据格式：{时间戳}#{版本号}#{数据}
local function toHashValue(timestamp, version, data)
    return string.format("%s#%s#%s", timestamp, version, data)
end

-- 遍历参数
local len = #ARGV
local ret = 0
for i = 3, len, 4 do

    -- HashKey
    local hashField = ARGV[i]

    -- HashKey 过期时间
    local hashFieldExpire = tonumber(ARGV[i + 1])

    -- HashValue 时间戳
    local hashValueTimestamp = currentTime + hashFieldExpire

    -- HashValue 版本号
    local hashValueVersion = ARGV[i + 2]

    -- HashValue 数据
    local hashValueData = ARGV[i + 3]

    -- 删除前任缓存键的 HashKey
    redis.call("hdel", beforeCacheKey, hashField)

    -- 更新当前缓存键的 HashKey
    ret = ret + redis.call("hset", currentCacheKey, hashField, toHashValue(hashValueTimestamp, hashValueVersion, hashValueData))
end

-- 设置当前缓存键的过期时间
redis.call("expire", currentCacheKey, cacheKeyExpire)
return ret