package com.mylar.lib.guava.simple.user;

import java.util.UUID;

/**
 * 用户信息
 *
 * @author wangz
 * @date 2021/10/10 0010 22:51
 */
public class UserInfo {

    /**
     * 构造方法
     *
     * @param name   会员名
     * @param status 状态
     */
    public UserInfo(String name, Integer status) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.status = status;
    }

    /**
     * id
     */
    private String id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 状态
     */
    private Integer status;

    // region getter & setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // endregion
}
