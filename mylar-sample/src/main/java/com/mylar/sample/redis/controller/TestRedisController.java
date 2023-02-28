package com.mylar.sample.redis.controller;

import com.mylar.lib.base.SpringResolver;
import com.mylar.lib.base.utils.JsonUtils;
import com.mylar.lib.redis.operations.IRedisAggregateOperations;
import com.mylar.lib.redis.script.SampleScript;
import com.mylar.sample.redis.cache.MyHashCache;
import com.mylar.sample.redis.cache.MyHashEnhanceCache;
import com.mylar.sample.redis.cache.MyHashExpireCache;
import com.mylar.sample.redis.cache.MyListEnhanceCache;
import com.mylar.sample.redis.data.MyRedisEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Redis Test
 *
 * @author wangz
 * @date 2023/2/26 0026 14:29
 */
@RestController
@RequestMapping(value = "/test/redis")
public class TestRedisController {

    /**
     * 自定义缓存 - Hash
     */
    @Autowired
    private MyHashCache myCache;

    /**
     * Hash Get
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @return Hash值
     */
    @RequestMapping(value = "/hashGet")
    public String hashGet(String cacheKey, String hashKey) {
        MyRedisEntity entity = this.myCache.getCache(cacheKey, hashKey);
        return JsonUtils.toJson(entity);
    }

    /**
     * Hash Set
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @param name     名称
     * @param value    值
     * @return 结果
     */
    @RequestMapping(value = "/setCache")
    public String hashSet(String cacheKey, String hashKey, String name, String value) {
        this.myCache.syncCache(cacheKey, hashKey, new MyRedisEntity(name, value));
        return "success";
    }

    /**
     * Enhance Hash Get
     *
     * @param cacheKey 缓存键
     * @return 结果
     */
    @RequestMapping(value = "/enhanceHashGet")
    public String enhanceHashGet(String cacheKey, String hashKey) {
        MyHashEnhanceCache myHashEnhanceCache = new MyHashEnhanceCache(cacheKey);
        MyRedisEntity entity = myHashEnhanceCache.getAndSyncIfAbsent(hashKey);
        return JsonUtils.toJson(entity);
    }

    /**
     * Enhance Hash Get
     *
     * @param cacheKey 缓存键
     * @return 结果
     */
    @RequestMapping(value = "/expireHashGet")
    public String expireHashGet(String cacheKey, String hashKey) {
        MyHashExpireCache myHashExpireCache = new MyHashExpireCache(cacheKey);
        MyRedisEntity entity = myHashExpireCache.getAndSyncIfAbsent(hashKey);
        return JsonUtils.toJson(entity);
    }

    /**
     * Enhance Hash Batch Get
     *
     * @param cacheKey 缓存键
     * @return 结果
     */
    @RequestMapping(value = "/expireHashBatchGet")
    public String expireHashBatchGet(String cacheKey, String hashKeys) {
        MyHashExpireCache myHashExpireCache = new MyHashExpireCache(cacheKey);
        String[] split = hashKeys.split(",");
        Map<String, MyRedisEntity> entityMap = myHashExpireCache.getAndSyncIfAbsent(split);
        return JsonUtils.toJson(entityMap);
    }

    /**
     * Enhance List Get
     *
     * @param cacheKey 缓存键
     * @return 结果
     */
    @RequestMapping(value = "/enhanceListGet")
    public String enhanceListGet(String cacheKey) {
        MyListEnhanceCache myListEnhanceCache = new MyListEnhanceCache(cacheKey);
        List<MyRedisEntity> entities = myListEnhanceCache.getAndSyncIfAbsent();
        return JsonUtils.toJson(entities);
    }

    /**
     * Hash Get
     *
     * @return Hash值
     */
    @RequestMapping(value = "/luaListGet")
    public String luaListGet() {

        IRedisAggregateOperations redisOperations = SpringResolver.resolve(IRedisAggregateOperations.class);

        List<String> keys = new ArrayList<>();
        keys.add("Test");

        Object luaResult = redisOperations.opsScript().executeScript(SampleScript.singleton().luaListRetGet(), keys);

        List<String> lst = (List<String>) luaResult;

        for (String s : lst) {
            System.out.println(s == null);
            System.out.println(s);
        }

        return JsonUtils.toJson(lst);
    }
}
