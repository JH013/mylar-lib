package com.mylar.lib.elasticjob.simple;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 调度中心注册配置
 *
 * @author wangz
 * @date 2021/9/19 0019 20:20
 */
@Configuration
public class CoordinatorRegistryConfig {

    /**
     * Zookeeper连接串
     */
    private static final String ZOOKEEPER_CONNECTION = "111.229.159.137:2181";

    /**
     * 定时任务命名空间
     */
    private static final String JOB_NAMESPACE = "elastic-job-boot-java";

    /**
     * Zookeeper调度中心注册
     *
     * @return 调度中心
     */
    @Bean(initMethod = "init")
    public CoordinatorRegistryCenter registry() {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(ZOOKEEPER_CONNECTION, JOB_NAMESPACE);
        zookeeperConfiguration.setSessionTimeoutMilliseconds(10000);
        zookeeperConfiguration.setConnectionTimeoutMilliseconds(50000);
        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }
}
