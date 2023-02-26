package com.mylar.lib.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.util.StringUtils;

/**
 * Redis 配置属性
 *
 * @author wangz
 * @date 2023/2/25 0025 11:04
 */
public abstract class BaseRedisProperties {

    /**
     * 单点地址
     */
    private String host;

    /**
     * 单点端口
     */
    private int port;

    /**
     * 密码
     */
    private String password;

    /**
     * 单点数据库索引
     */
    private int database;

    /**
     * 集群节点，示例：192.168.99.133:6380,192.168.99.133:6381,192.168.99.133:6382
     */
    private String nodes;

    /**
     * 跨集群执行命令时要遵循的最大重定向数量
     */
    private int maxRedirects = 5;

    /**
     * 连接池的最大连接数
     */
    private int maxTotal = GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;

    /**
     * 连接池中的最大空闲连接
     */
    private int maxIdle = GenericObjectPoolConfig.DEFAULT_MAX_IDLE;

    /**
     * 连接池中的最小空闲连接
     */
    private int minIdle = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;

    /**
     * 是否开启ssl
     */
    private Boolean ssl = false;

    /**
     * 连接超时时间（毫秒）
     */
    private int timeout = 10000;

    /**
     * 是否集群
     */
    public boolean redisCluster() {
        return StringUtils.hasLength(nodes);
    }



    // region getter & setter

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    // endregion
}
