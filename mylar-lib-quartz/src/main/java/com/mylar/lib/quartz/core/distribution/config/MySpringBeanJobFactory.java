package com.mylar.lib.quartz.core.distribution.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * Spring Bean Job Factory
 *
 * @author wangz
 * @date 2021/9/12 0012 21:37
 */
public class MySpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    /**
     * 自动注入工厂
     */
    private transient AutowireCapableBeanFactory beanFactory;

    /**
     * 设置应用程序上下文
     *
     * @param context 上下文
     */
    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    /**
     * 创建job实例，将job实例交付给spring ioc
     *
     * @param bundle 触发器
     * @return job
     * @throws Exception 异常
     */
    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        this.beanFactory.autowireBean(job);
        return job;
    }
}
