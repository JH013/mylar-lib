package com.mylar.lib.thread.pool.core;

import com.mylar.lib.thread.pool.annotation.ThreadPoolBasicConfig;
import com.mylar.lib.thread.pool.scheduler.ThreadPoolScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Map;

/**
 * 线程池自动配置
 *
 * @author wangz
 * @date 2023/3/21 0021 21:46
 */
@Configuration
@ConditionalOnProperty(prefix = "mylar.thread.pool", name = {"enable"}, havingValue = "true", matchIfMissing = true)
@Import({ThreadPoolScheduler.class})
public class ThreadPoolAutoConfig implements ApplicationRunner {

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(ThreadPoolAutoConfig.class);

    /**
     * 应用上下文
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 线程池调度器
     */
    @Autowired
    private ThreadPoolScheduler threadPoolScheduler;

    // endregion

    // region 接口实现

    /**
     * 运行
     *
     * @param args 参数
     */
    @Override
    public void run(ApplicationArguments args) {

        // 初始化全部线程池
        this.initAllThreadPools();
    }

    // endregion

    // region 私有方法

    /**
     * 初始化全部线程池
     */
    private void initAllThreadPools() {

        // 获取全部实例
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(ThreadPoolBasicConfig.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {

            // 实例
            Object instance = entry.getValue();
            if (AbstractThreadPool.class.isAssignableFrom(instance.getClass())) {

                // 初始化线程池
                this.initThreadPool((AbstractThreadPool) instance);
            }
        }
    }

    /**
     * 初始化线程池（仅缓存配置，不实例化）
     *
     * @param threadPool 线程池实例
     */
    private void initThreadPool(AbstractThreadPool threadPool) {

        // 获取基础配置
        ThreadPoolBasicConfig basicConfig = threadPool.getClass().getAnnotation(ThreadPoolBasicConfig.class);
        if (basicConfig == null) {
            return;
        }

        // 添加基础配置到线程池调度器
        this.threadPoolScheduler.addThreadPoolConfig(basicConfig);

        // 设置线程池键
        threadPool.setKey(basicConfig.key());
    }

    // endregion
}