package com.mylar.lib.redis.enhance;

import com.mylar.lib.base.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LIST缓存增强抽象类
 * <p>
 * 说明：
 * 1、支持从缓存查询不到数据时，从其它数据源查询并更新
 * 2、支持缓存键设置过期时间
 *
 * @author wangz
 * @date 2023/2/26 0026 23:34
 */
public abstract class AbstractListEnhanceCache<T> extends AbstractListCache<T> implements IListEnhanceCache<T> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param cacheKey 缓存键
     */
    public AbstractListEnhanceCache(String cacheKey) {
        this.cacheKey = cacheKey;

        try {
            this.afterPropertiesSet();
        } catch (Exception e) {
            log.error("初始化list缓存实例失败", e);
        }
    }

    // endregion

    // region 变量

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 缓存键
     */
    protected String cacheKey;

    /**
     * 缺省值
     */
    protected String defaultEmptyValue = "$DEFAULT_VALUE$";

    // endregion

    // region 接口实现

    /**
     * 查询缓存
     * <p>
     * 1、查询全部
     * 2、缓存不存在时从数据源获取并同步到缓存
     *
     * @return 值集合
     */
    @Override
    public List<T> getAndSyncIfAbsent() {

        // 查询全部
        List<String> strValues = this.redisOperations.opsList().listGetByRange(this.cacheKey, 0L, -1L);

        // 查询结果不为空
        if (!CollectionUtils.isEmpty(strValues)) {

            // 过滤掉缺省值
            strValues.removeIf(t -> this.defaultEmptyValue.equals(t));

            // 反序列化结果并返回
            return strValues.stream().map(t -> JsonUtils.deJson(t, this.getValueClazz())).collect(Collectors.toList());
        }

        // 获取全部数据源
        List<T> source = this.loadSource();

        // 数据源为空
        if (CollectionUtils.isEmpty(source)) {

            // 全量同步缓存（缺省值）
            this.syncCacheFullWithStringValues(this.cacheKey, Collections.singletonList(this.defaultEmptyValue));
            return new ArrayList<>();
        }

        // 全量同步缓存
        this.syncCacheFull(this.cacheKey, source);
        return source;
    }

    // endregion

    // region 供子类重写

    /**
     * 获取全部数据源
     *
     * @return 结果
     */
    protected abstract List<T> loadSource();

    // endregion
}