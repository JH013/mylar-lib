-- 分布式锁 - 基础 - 释放锁

-- 锁定键
local lockKey = KEYS[1];

-- 锁的值
local lockValue = ARGV[1];

-- 当前锁的值
if redis.call('get', lockKey) == lockValue then

    -- 与入参锁的值相同：删除锁，并返回 1，表示释放锁成功
    return redis.call('del', lockKey)
else

    -- 与入参锁的值不同：返回 0，表示锁自动过期释放
    return 0
end