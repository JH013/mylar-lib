package com.mylar.lib.guava.simple.user;

import com.mylar.lib.guava.core.AbstractMemoryCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 用户内存缓存
 *
 * @author wangz
 * @date 2021/10/10 0010 22:50
 */
@Component
public class UserMemoryCache extends AbstractMemoryCache<String, UserInfo> {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(UserMemoryCache.class);

    /**
     * 模拟数据库
     */
    @Autowired
    private SimulateDatabase simulateDatabase;

    /**
     * 缓存过期时间
     *
     * @return 缓存过期时间
     */
    @Override
    protected long expireDuration() {
        return 10;
    }

    /**
     * 加载数据
     *
     * @param key 键
     * @return 值
     */
    @Override
    protected UserInfo loadSource(String key) {
        logger.info(String.format("load source, key: %s", key));
        return this.simulateDatabase.queryByName(key);
    }
}
