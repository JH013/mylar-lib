package com.mylar.lib.redis.enhance;

import java.util.List;

/**
 * LIST缓存增强
 *
 * @author wangz
 * @date 2023/2/26 0026 23:33
 */
public interface IListEnhanceCache<T> {

    /**
     * 查询缓存
     * <p>
     * 1、查询全部
     * 2、缓存不存在时从数据源获取并同步到缓存
     *
     * @return 值集合
     */
    List<T> getAndSyncIfAbsent();
}
