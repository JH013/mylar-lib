package com.mylar.lib.guava.simple.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟数据库
 *
 * @author wangz
 * @date 2021/10/10 0010 22:54
 */
@Component
public class SimulateDatabase {

    /**
     * 构造方法
     */
    public SimulateDatabase() {
        this.userInfoList = new ArrayList<>();
        this.userInfoList.add(new UserInfo("user_1", 1));
        this.userInfoList.add(new UserInfo("user_2", 2));
        this.userInfoList.add(new UserInfo("user_3", 3));
        this.userInfoList.add(new UserInfo("user_4", 4));
        this.userInfoList.add(new UserInfo("user_5", 5));
    }

    /**
     * 数据源
     */
    private final List<UserInfo> userInfoList;

    /**
     * 根据名称查询
     *
     * @param name 用户名
     * @return 用户信息
     */
    public UserInfo queryByName(String name) {
        return this.userInfoList.stream().filter(t -> name.equals(t.getName())).findFirst().orElse(null);
    }
}
