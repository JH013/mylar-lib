-- 分布式锁 - 可重入 - 添加锁

-- 锁定键
local lockKey = KEYS[1];

-- 锁定值
local lockValue = ARGV[1];

-- 锁过期时间
local expireTime = ARGV[2];

-- 锁不存在
if (redis.call('exists', lockKey) == 0) then

    -- 添加锁并设置重入次数为 1
    redis.call('hset', lockKey, lockValue, '1');

    -- 设置过期时间
    redis.call('expire', lockKey, expireTime);

    -- 返回加锁成功
    return 1;
end;

-- 锁已存在且由当前线程持有
if (redis.call('hexists', lockKey, lockValue) == 1) then

    -- 重入次数加 1
    redis.call('hincrby', lockKey, lockValue, '1');

    -- 设置过期时间
    redis.call('expire', lockKey, expireTime);

    -- 返回加锁成功
    return 1;
end;

-- 返回加锁失败
return 0;