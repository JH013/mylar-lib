-- 分布式锁 - 可重入 - 释放锁

-- 锁定键
local lockKey = KEYS[1];

-- 当前线程Id
local currentThread = ARGV[1];

-- 锁不存在：返回 0，表示锁自动过期释放
if (redis.call('hexists', lockKey, currentThread) == 0) then
    return 0;
end;

-- 锁已存在：重入次数减 1，当重入次数为 0 时删除锁
if (redis.call('hincrby', lockKey, currentThread, -1) == 0) then
    redis.call('del', lockKey);
end;

-- 返回 1，表示释放锁成功
return 1;

