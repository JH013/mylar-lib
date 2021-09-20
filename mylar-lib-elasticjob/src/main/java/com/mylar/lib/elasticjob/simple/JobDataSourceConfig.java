package com.mylar.lib.elasticjob.simple;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 任务数据源配置
 *
 * @author wangz
 * @date 2021/9/20 0020 3:04
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "elasticjob.datasource")
public class JobDataSourceConfig {

    private String url;

    private String username;

    private String password;

    private String driver_class_name;

    @Bean
    public HikariDataSource hikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        dataSource.setDriverClassName(this.driver_class_name);
        return dataSource;
    }
}
