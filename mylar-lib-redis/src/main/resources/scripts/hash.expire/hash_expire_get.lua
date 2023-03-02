-- 支持过期的hash查询

local cacheKey = KEYS[1]
local hashField = ARGV[1]
-- 缓存键过期时间
local cacheKeyExpire = tonumber(ARGV[2])
-- 当前时间
local currentTime = tonumber(ARGV[3])

-- 缓存键的key后缀
local cacheKeyTail = math.floor(currentTime / cacheKeyExpire)

local cacheKeyWithTail = cacheKey .. cacheKeyTail
local cacheKeyWithBefore = cacheKey .. (cacheKeyTail - 1)
local fromBefore = 0
-- 查询当前缓存
local hashValue = redis.call("hget", cacheKeyWithTail, hashField) or nil
if hashValue == nil then
    hashValue = redis.call("hget", cacheKeyWithBefore, hashField) or nil
    if hashValue == nil then
        return nil
    end
    fromBefore = 1
end

-- 数据格式:{时间戳长度}#{时间戳内容}{数据版本长度}#{数据版本内容}{数据长度}#{数据内容}
-- dataType:数据类型 0-时间戳 1-数据版本 2-数据内容
local function analyzeHashValue(data, dataType)
    local indexStart = 1
    local indexSplit = 0
    local dataLength = 0
    for i = 0, dataType, 1 do
        indexSplit = string.find(data, "#", indexStart)
        dataLength = string.sub(data, indexStart, indexSplit - 1)
        indexStart = indexSplit + dataLength + 1
    end
    return string.sub(data, indexSplit + 1, indexSplit + dataLength)
end

-- 解析过期时间
local timestamp = tonumber(analyzeHashValue(hashValue, 0))
if currentTime > timestamp then
    -- 已过期
    if fromBefore == 1 then
        redis.call("hdel", cacheKeyWithBefore, hashField)
    else
        redis.call("hdel", cacheKeyWithTail, hashField)
    end
    -- 过期时返回空
    return nil;
end

-- 返回结果
return analyzeHashValue(hashValue, 2)