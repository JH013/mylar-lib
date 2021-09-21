package com.mylar.lib.simple.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Application Resolver
 *
 * @author wangz
 * @date 2021/9/21 0021 20:42
 */
@Component
public class SpringContextResolver implements ApplicationContextAware {

    /**
     * 应用程序上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置上下文
     *
     * @param applicationContext 应用程序上下文
     * @throws BeansException 异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextResolver.applicationContext = applicationContext;
    }

    /**
     * 获取应用程序上下文
     *
     * @return 应用程序上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取Bean
     *
     * @param name Bean名称
     * @return Bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 获取Bean
     *
     * @param clazz Bean类型
     * @param <T>   泛型
     * @return Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 获取Bean
     *
     * @param name  Bean名称
     * @param clazz Bean类型
     * @param <T>   泛型
     * @return Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
