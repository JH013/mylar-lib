package com.mylar.sample.redis.cache;

import com.mylar.lib.redis.enhance.AbstractHashCache;
import com.mylar.sample.redis.data.MyRedisEntity;
import org.springframework.stereotype.Component;

/**
 * 自定义缓存 - Hash
 *
 * @author wangz
 * @date 2023/2/26 0026 23:59
 */
@Component
public class MyHashCache extends AbstractHashCache<MyRedisEntity> {

    /**
     * 获取值类型
     *
     * @return 值类型
     */
    @Override
    public Class<MyRedisEntity> getValueClazz() {
        return MyRedisEntity.class;
    }
}
