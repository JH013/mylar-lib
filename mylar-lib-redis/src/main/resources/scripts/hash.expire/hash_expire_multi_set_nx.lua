-- 支持过期的hash批量set，不存在时才set
-- User: chuff
-- Date: 2021/11/29 14:25
--

local cacheKey = KEYS[1]
-- 缓存键过期时间
local cacheKeyExpire = tonumber(ARGV[1])
-- 当前时间
local currentTime = tonumber(ARGV[2])

-- 缓存键的key后缀
local cacheKeyTail = math.floor(currentTime / cacheKeyExpire)

local cacheKeyWithTail = cacheKey .. cacheKeyTail
local cacheKeyWithBefore = cacheKey .. (cacheKeyTail - 1)

-- 数据格式:{时间戳内容}#{数据版本内容}#{数据内容}
local function analyzeAllHashValue(data)
    local timestamp
    local version
    local value
    local startIndex = 1
    local endIndex = string.find(data, "#", 1)
    timestamp = string.sub(data, startIndex, endIndex - 1)

    startIndex = endIndex + 1
    endIndex = string.find(data, "#", startIndex)
    version = string.sub(data, startIndex, endIndex - 1)

    local startIndex = endIndex + 1
    value = string.sub(data, startIndex, -1)

    return timestamp, version, value
end

-- 数据格式:{时间戳内容}#{数据版本内容}#{数据内容}
local function toHashValue(timestamp, version, data)
    return string.format("%s#%s#%s", timestamp, version, data)
end

local function hashSetNx(_cacheKeyWithTail, _cacheKeyWithBefore, _cacheKeyExpire, _newTimestamp, _version, _realValue, _hashField)

    local fromBefore = 0

    -- 查询当前缓存
    local hashValue = redis.call("hget", _cacheKeyWithTail, _hashField) or nil
    if hashValue == nil then
        hashValue = redis.call("hget", _cacheKeyWithBefore, _hashField) or nil
        if hashValue == nil then
            redis.call("hset", _cacheKeyWithTail, _hashField, toHashValue(_newTimestamp, _version, _realValue))
            redis.call("expire", _cacheKeyWithTail, _cacheKeyExpire)
            return 1
        end
        fromBefore = 1
    end

    -- 解析过期时间
    local t, v, d = analyzeAllHashValue(hashValue)
    local timestamp = tonumber(t)
    if _currentTime > timestamp then
        -- 已过期
        if fromBefore == 1 then
            -- 已过期或者要被更新的旧cacheKey数据，要删除
            redis.call("hdel", _cacheKeyWithBefore, _hashField)
        end
        redis.call("hset", _cacheKeyWithTail, _hashField, toHashValue(_newTimestamp, _version, _realValue))
        redis.call("expire", _cacheKeyWithTail, _cacheKeyExpire)
        -- 返回更新成功
        return 1;
    end

    if fromBefore == 1 then
        -- 没过期的数据，要移动到新cacheKey
        redis.call("hdel", _cacheKeyWithBefore, _hashField)
        redis.call("hset", _cacheKeyWithTail, _hashField, hashValue)
        redis.call("expire", _cacheKeyWithTail, _cacheKeyExpire)
    end
    -- 返回没有更新
    return 0
end

local len = #ARGV
local ret = 0
for i = 3, len, 4 do
    -- 同参数对应取值
    local hashField = ARGV[i]
    local hashFieldExpire = tonumber(ARGV[i + 1])
    local realValue = ARGV[i + 2]
    local newVersion = ARGV[i + 3]
    local newTimestamp = currentTime + hashFieldExpire;
    -- 循环更新
    ret = ret + hashSetNx(cacheKeyWithTail, cacheKeyWithBefore, cacheKeyExpire, newTimestamp, newVersion, realValue, hashField)
end
return ret