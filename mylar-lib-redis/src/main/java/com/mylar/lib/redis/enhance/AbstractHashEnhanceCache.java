package com.mylar.lib.redis.enhance;

import com.mylar.lib.base.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * HASH缓存增强
 * <p>
 * 说明：
 * 1、支持从缓存查询不到数据时，从其它数据源查询并更新
 * 2、支持缓存键设置过期时间
 *
 * @author wangz
 * @date 2023/2/26 0026 22:18
 */
public abstract class AbstractHashEnhanceCache<T> extends AbstractHashCache<T> implements IHashEnhanceCache<T> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param cacheKey 缓存键
     */
    public AbstractHashEnhanceCache(String cacheKey) {
        this.cacheKey = cacheKey;

        try {
            this.afterPropertiesSet();
        } catch (Exception e) {
            log.error("初始化hash缓存实例失败", e);
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
     * 缺省字段
     */
    protected String defaultEmptyHashField = "$DEFAULT_FIELD$";

    /**
     * 缺省值
     */
    protected String defaultEmptyHashValue = "$DEFAULT_VALUE$";

    /**
     * 缺省字段（标识全部）
     */
    protected String defaultAllHashField = "$DEFAULT_ALL_FIELD$";

    /**
     * 缺省值（标识全部）
     */
    protected String defaultAllHashValue = "$DEFAULT_ALL_VALUE$";

    // endregion

    // region 接口实现

    /**
     * 查询缓存
     * <p>
     * 1、单个查询
     * 2、缓存不存在时从数据源获取并同步到缓存
     *
     * @param hashField 字段
     * @return 值
     */
    @Override
    public T getAndSyncIfAbsent(String hashField) {

        // 校验入参
        if (!StringUtils.hasLength(hashField)) {
            return null;
        }

        // 查询缓存
        String hashValueStr = this.redisOperations.opsHash().hashGetStr(this.cacheKey, hashField);

        // 缓存值为缺省值：返回空
        if (this.defaultEmptyHashValue.equals(hashValueStr)) {
            return null;
        }
        // 缓存值为空：从数据源获取数据
        else if (hashValueStr == null) {

            // 从数据源获取数据
            T source = this.loadSource(hashField);

            // 同步缓存
            this.hashSingleSet(this.cacheKey, hashField, source);
            return source;
        }
        // 缓存值不为空且不为缺省值：反序列化值并返回
        else {
            return JsonUtils.deJson(hashValueStr, this.getValueClazz());
        }
    }

    /**
     * 查询缓存
     * <p>
     * 1、批量查询
     * 2、缓存不存在时从数据源获取并同步到缓存
     *
     * @param hashFields 字段集合
     * @return 值集合
     */
    @Override
    public Map<String, T> getAndSyncIfAbsent(String... hashFields) {

        // 结果集
        Map<String, T> results = new HashMap<>(hashFields.length);

        // 需要从数据源获取数据的field集合
        List<String> needLoadSourceFields = new ArrayList<>();

        // 批量查询缓存
        Map<String, String> hashValues = this.redisOperations.opsHash().hashGetStr(this.cacheKey, Arrays.stream(hashFields).collect(Collectors.toList()));
        for (String hashField : hashFields) {
            String hashValue = hashValues.getOrDefault(hashField, null);

            // 缓存值为缺省值：添加空到结果集中
            if (this.defaultEmptyHashValue.equals(hashValue)) {
                results.put(hashField, null);
            }
            // 缓存值为空：需要从数据源获取数据，暂存到集合中，批量查询后更新缓存
            else if (hashValue == null) {
                needLoadSourceFields.add(hashField);
            }
            // 缓存值不为空且不为缺省值：反序列化值并添加到结果集中
            else {
                results.put(hashField, JsonUtils.deJson(hashValue, this.getValueClazz()));
            }
        }

        // 批量查询数据源，更新缓存，并添加查询结果到结果集中
        if (!CollectionUtils.isEmpty(needLoadSourceFields)) {

            // 从数据源获取数据
            Map<String, T> sources = this.loadSource(needLoadSourceFields);
            for (String hashField : needLoadSourceFields) {

                // 补全数据源查询结果
                if (!sources.containsKey(hashField)) {
                    sources.put(hashField, null);
                }

                // 添加到结果集中
                results.put(hashField, sources.get(hashField));
            }

            // 同步缓存
            this.hashMultiSet(this.cacheKey, sources);
        }

        return results;
    }

    /**
     * 查询缓存
     * <p>
     * 1、查询全部
     * 2、缓存不存在时从数据源获取并同步到缓存
     *
     * @return 值集合
     */
    @Override
    public Map<String, T> getAndSyncIfAbsent() {

        // 分页查询
        Map<String, String> hashValues = new HashMap<>();
        this.redisOperations.opsHash().hashScanAll(this.cacheKey, 100, (key, value) -> {
            hashValues.put(key, JsonUtils.deJson(value, String.class));
        });

        // 查询结果为空
        if (hashValues.isEmpty()) {

            // 从数据源获取数据，并更新到缓存
            return this.loadSourceAndSyncAll();
        }
        // 查询结果不为空
        else {

            // 过滤掉【空缺省值】
            hashValues.entrySet().removeIf(entry -> !StringUtils.hasLength(entry.getKey())
                    || this.defaultEmptyHashField.equals(entry.getKey()) || this.defaultEmptyHashValue.equals(entry.getValue()));

            // 不存在【全部缺省值】：从数据源获取数据，并更新到缓存
            if (!hashValues.containsKey(this.defaultAllHashField)) {
                return this.loadSourceAndSyncAll();
            }
            // 存在【全部缺省值】：过滤掉【全部缺省值】
            else {
                hashValues.entrySet().removeIf(entry -> !StringUtils.hasLength(entry.getKey())
                        || this.defaultAllHashField.equals(entry.getKey()) || this.defaultAllHashValue.equals(entry.getValue()));
            }
        }

        Map<String, T> results = new HashMap<>();
        hashValues.forEach((key, value) -> results.put(key, JsonUtils.deJson(value, this.getValueClazz())));
        return results;
    }

    /**
     * 设置过期时间
     *
     * @param cacheKey 缓存键
     */
    @Override
    protected void setExpire(String cacheKey) {
        Long expire = this.redisOperations.opsKey().getExpire(cacheKey);
        if (expire == null || expire < 0) {
            super.setRandomExpire(cacheKey);
        }
    }

    // endregion

    // region 供子类重写

    /**
     * 获取数据源
     *
     * @param hashField 字段
     * @return 值
     */
    protected abstract T loadSource(String hashField);

    /**
     * 批量获取数据源
     *
     * @param hashFields 字段集合
     * @return 值集合
     */
    protected abstract Map<String, T> loadSource(List<String> hashFields);

    /**
     * 获取全部数据源
     *
     * @return 值集合
     */
    protected abstract Map<String, T> loadSource();

    // endregion

    // region 私有方法

    /**
     * 单个更新
     *
     * @param cacheKey  缓存键
     * @param hashField 字段
     * @param hashValue 值
     */
    private void hashSingleSet(String cacheKey, String hashField, T hashValue) {

        // 更新缓存
        this.redisOperations.opsHash().hashSyncStr(cacheKey, hashField, this.formatHashValue(hashValue));

        // 设置缓存过期时间
        this.setExpire(cacheKey);
    }

    /**
     * 批量更新
     *
     * @param cacheKey   缓存键
     * @param hashValues 值集合
     */
    private void hashMultiSet(String cacheKey, Map<String, T> hashValues) {

        // 转换待更新数据
        Map<String, String> syncItems = new HashMap<>(hashValues.size());
        for (Map.Entry<String, T> entry : hashValues.entrySet()) {
            syncItems.put(entry.getKey(), this.formatHashValue(entry.getValue()));
        }

        // 更新缓存
        this.redisOperations.opsHash().hashSyncStr(cacheKey, syncItems);

        // 设置缓存过期时间
        this.setExpire(cacheKey);
    }

    /**
     * 获取值
     *
     * @param hashValue 原值
     * @return 新值
     */
    private String formatHashValue(T hashValue) {

        // 缓存值为空：设置默认缺省值
        if (hashValue == null) {
            return this.defaultEmptyHashValue;
        }

        return JsonUtils.toJson(hashValue);
    }

    /**
     * 加载数据并全部同步到缓存
     *
     * @return 结果
     */
    private Map<String, T> loadSourceAndSyncAll() {

        // 从数据源获取数据
        Map<String, T> sources = this.loadSource();

        // 如果数据源结果集为空，则插入一条【空缺省值】
        if (sources == null || sources.isEmpty()) {
            this.hashSingleSet(this.cacheKey, this.defaultEmptyHashField, null);
        }
        // 同步缓存
        else {

            // 同步数据
            this.hashMultiSet(this.cacheKey, sources);

            // 额外同步一条数据【全部缺省值】
            this.setDefaultAllHashField();
        }

        return sources;
    }

    /**
     * 新增全部缺省值
     */
    private void setDefaultAllHashField() {

        // 更新缓存
        this.redisOperations.opsHash().hashSyncStr(this.cacheKey, this.defaultAllHashField, this.defaultAllHashValue);

        // 设置缓存过期时间
        this.setExpire(cacheKey);
    }

    // endregion
}