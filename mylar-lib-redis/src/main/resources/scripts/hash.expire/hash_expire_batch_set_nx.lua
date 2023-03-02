-- 支持 HashKey 过期 - 批量更新 - 当 HashKey 不存在时更新

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

-- 解析Hash值，数据格式：{时间戳}#{版本号}#{数据}
local function analyzeAllHashValue(data)

    -- 时间戳
    local timestamp

    -- 版本号
    local version

    -- 数据
    local value

    -- 截取时间戳
    local startIndex = 1
    local endIndex = string.find(data, "#", 1)
    timestamp = string.sub(data, startIndex, endIndex - 1)

    -- 截取版本号
    startIndex = endIndex + 1
    endIndex = string.find(data, "#", startIndex)
    version = string.sub(data, startIndex, endIndex - 1)

    -- 截取数据
    local startIndex = endIndex + 1
    value = string.sub(data, startIndex, -1)

    -- 返回结果
    return timestamp, version, value
end

-- 当HashKey不存在时设置Hash值
local function hashSetNx(_hashField, _hashValueTimestamp, _hashValueVersion, _hashValueData)

    -- Hash值是否存前任缓存键中查出
    local fromBefore = 0

    -- 查询当前缓存键
    local hashValue = redis.call("hget", currentCacheKey, _hashField) or nil
    if hashValue == nil then

        -- 查询前任缓存键
        hashValue = redis.call("hget", beforeCacheKey, _hashField) or nil
        if hashValue == nil then

            -- 更新当前缓存键的 HashKey，并设置缓存过期时间
            redis.call("hset", currentCacheKey, _hashField, toHashValue(_hashValueTimestamp, _hashValueVersion, _hashValueData))
            redis.call("expire", currentCacheKey, cacheKeyExpire)
            return 1
        end

        -- 标识Hash值是从前任缓存键中查出
        fromBefore = 1
    end

    -- 解析Hash值
    local t, v, d = analyzeAllHashValue(hashValue)

    -- 转换过期时间
    local _timestamp = tonumber(t)

    -- HashKey已过期
    if currentTime > _timestamp then

        -- Hash值是从前任缓存键中查出，删除HashKey
        if fromBefore == 1 then
            redis.call("hdel", beforeCacheKey, _hashField)
        end

        -- 更新当前缓存键的 HashKey，并设置缓存过期时间
        redis.call("hset", currentCacheKey, _hashField, toHashValue(_hashValueTimestamp, _hashValueVersion, _hashValueData))
        redis.call("expire", currentCacheKey, cacheKeyExpire)
        return 1
    end

    -- 没有过期的数据，且HashKey在前任缓存键中，需要移动到当前缓存键中
    if fromBefore == 1 then

        -- 没过期的数据，要移动到新cacheKey
        redis.call("hdel", beforeCacheKey, _hashField)
        redis.call("hset", currentCacheKey, _hashField, hashValue)
        redis.call("expire", currentCacheKey, cacheKeyExpire)
    end

    -- 返回没有更新
    return 0
end

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

    -- 当HashKey不存在时设置Hash值
    ret = ret + hashSetNx(hashField, hashValueTimestamp, hashValueVersion, hashValueData)
end

return ret