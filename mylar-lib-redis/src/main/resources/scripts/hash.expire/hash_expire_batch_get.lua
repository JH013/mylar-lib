-- 支持 HashKey 过期 - 批量查询

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

-- 解析Hash值，数据格式：{时间戳}#{版本号}#{数据}
local function analyzeHashValue(data, dataType)

    -- 截取时间戳
    if dataType == 0 then
        local startIndex = 1
        local endIndex = string.find(data, "#", 1)
        return string.sub(data, startIndex, endIndex - 1)
    end

    -- 截取版本号
    if dataType == 1 then
        local startIndex = string.find(data, "#", 1) + 1
        local endIndex = string.find(data, "#", startIndex)
        return string.sub(data, startIndex, endIndex - 1)
    end

    -- 截取数据
    local startIndex = string.find(data, "#", 1) + 1
    startIndex = string.find(data, "#", startIndex) + 1
    return string.sub(data, startIndex, -1)
end

-- 获取Hash值
local function hashGet(currentCacheKey, beforeCacheKey, currentTime, hashField)

    -- Hash值是否存前任缓存键中查出
    local fromBefore = 0

    -- 查询当前缓存键
    local hashValue = redis.call("hget", currentCacheKey, hashField) or nil
    if hashValue == nil then

        -- 查询前任缓存键
        hashValue = redis.call("hget", beforeCacheKey, hashField) or nil
        if hashValue == nil then
            return nil
        end

        -- 标识Hash值是从前任缓存键中查出
        fromBefore = 1
    end

    -- 解析过期时间
    local timestamp = tonumber(analyzeHashValue(hashValue, 0))

    -- HashKey已过期
    if currentTime > timestamp then

        -- 从前任缓存键中删除HashKey
        if fromBefore == 1 then
            redis.call("hdel", beforeCacheKey, hashField)

        -- 从当前缓存键中删除HashKey
        else
            redis.call("hdel", currentCacheKey, hashField)
        end

        -- HashKey过期时返回空
        return nil;
    end

    -- 解析Hash值数据
    return analyzeHashValue(hashValue, 2)
end

-- 遍历参数
local len = #ARGV
local ret = {}
for i = 3, len, 1 do

    -- 获取Hash值
    -- ret[i - 2] = hashGet(currentCacheKey, beforeCacheKey, currentTime, ARGV[i])

    -- 获取Hash值
    local hValue = hashGet(currentCacheKey, beforeCacheKey, currentTime, ARGV[i])
    if hValue == nil then
        table.insert(ret, false)
    else
        table.insert(ret, hValue)
    end

end
return ret

