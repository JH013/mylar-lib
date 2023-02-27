-- 支持过期的hash测试
-- User: chuff
-- Date: 2021/11/29 14:25
--
local cacheKey = KEYS[1]
local time = redis.call("time")
return time[1],time[2],cacheKey