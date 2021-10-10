package com.mylar.lib.guava.simple;

import com.mylar.lib.guava.simple.user.UserInfo;
import com.mylar.lib.guava.simple.user.UserMemoryCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内存缓存测试控制器
 *
 * @author wangz
 * @date 2021/9/25 0025 20:15
 */
@RestController
@RequestMapping(value = "/guava/cache")
public class GuavaCacheController {

    /**
     * 会员内存缓存
     */
    @Autowired
    private UserMemoryCache userMemoryCache;

    /**
     * 查询
     *
     * @param key key
     * @return value
     */
    @RequestMapping(value = "/query")
    public String query(String key) {
        return DemoGuavaCache.get(key);
    }

    /**
     * 查询
     *
     * @param username username
     * @return value
     */
    @RequestMapping(value = "/queryUser")
    public UserInfo queryUser(String username) {
        return this.userMemoryCache.get(username);
    }
}
