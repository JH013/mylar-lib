package com.mylar.lib.quartz.core.distribution.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.quartz.SchedulerException;
import org.quartz.utils.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Druid连接池
 *
 * @author wangz
 * @date 2021/9/14 0014 21:24
 */
public class DruidConnectionProvider implements ConnectionProvider {

    // region 常量配置，与quartz.properties文件的key保持一致(去掉前缀)，同时提供set方法，Quartz框架自动注入值

    /**
     * 驱动
     */
    public String driver;

    /**
     * 连接串
     */
    public String URL;

    /**
     * 用户名
     */
    public String user;

    /**
     * 密码
     */
    public String password;

    /**
     * 最大连接数
     */
    public int maxConnection;

    /**
     * 数据库SQL查询每次连接返回执行到连接池，以确保它仍然是有效的
     */
    public String validationQuery;

    private boolean validateOnCheckout;

    private int idleConnectionValidationSeconds;

    public String maxCachedStatementsPerConnection;

    private String discardIdleConnectionsSeconds;

    public static final int DEFAULT_DB_MAX_CONNECTIONS = 10;

    public static final int DEFAULT_DB_MAX_CACHED_STATEMENTS_PER_CONNECTION = 120;

    // endregion

    /**
     * Druid连接池
     */
    private DruidDataSource dataSource;

    // region 接口实现

    /**
     * 获取连接
     *
     * @return 连接
     * @throws SQLException 异常
     */
    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 关闭连接
     */
    @Override
    public void shutdown() {
        dataSource.close();
    }

    /**
     * 初始化
     *
     * @throws SQLException 异常
     */
    @Override
    public void initialize() throws SQLException {
        if (this.URL == null) {
            throw new SQLException("DBPool could not be created: DB URL cannot be null");
        }

        if (this.driver == null) {
            throw new SQLException("DBPool driver could not be created: DB driver class name cannot be null!");
        }

        if (this.maxConnection < 0) {
            throw new SQLException("DBPool maxConnectins could not be created: Max connections must be greater than zero!");
        }

        dataSource = new DruidDataSource();
        try {
            dataSource.setDriverClassName(this.driver);
        } catch (Exception e) {
            try {
                throw new SchedulerException("Problem setting driver class name on datasource: " + e.getMessage(), e);
            } catch (SchedulerException ignored) {
            }
        }

        dataSource.setUrl(this.URL);
        dataSource.setUsername(this.user);
        dataSource.setPassword(this.password);
        dataSource.setMaxActive(this.maxConnection);
        dataSource.setMinIdle(1);
        dataSource.setMaxWait(0);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(DEFAULT_DB_MAX_CACHED_STATEMENTS_PER_CONNECTION);

        if (this.validationQuery != null) {
            dataSource.setValidationQuery(this.validationQuery);
            if (!this.validateOnCheckout) {
                dataSource.setTestOnReturn(true);
            } else {
                dataSource.setTestOnBorrow(true);
            }
            dataSource.setValidationQueryTimeout(this.idleConnectionValidationSeconds);
        }
    }

    // endregion

    // region getter & setter

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isValidateOnCheckout() {
        return validateOnCheckout;
    }

    public void setValidateOnCheckout(boolean validateOnCheckout) {
        this.validateOnCheckout = validateOnCheckout;
    }

    public int getIdleConnectionValidationSeconds() {
        return idleConnectionValidationSeconds;
    }

    public void setIdleConnectionValidationSeconds(int idleConnectionValidationSeconds) {
        this.idleConnectionValidationSeconds = idleConnectionValidationSeconds;
    }

    public DruidDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DruidDataSource dataSource) {
        this.dataSource = dataSource;
    }

    // endregion
}
