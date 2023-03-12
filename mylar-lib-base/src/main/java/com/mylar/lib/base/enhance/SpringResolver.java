package com.mylar.lib.base.enhance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Spring 解析
 *
 * @author wangz
 * @date 2023/2/26 0026 15:05
 */
@Component
public class SpringResolver implements ApplicationContextAware {

    // region 变量 & 常量

    /**
     * Servlet Context
     */
    private static ApplicationContext servletContext;

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(SpringResolver.class);

    // endregion

    // region 接口实现

    /**
     * 设置应用上下文
     *
     * @param applicationContext 应用上下文
     * @throws BeansException 异常
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringResolver.servletContext == null) {
            SpringResolver.servletContext = applicationContext;
        }
    }

    // endregion

    // region 公共方法

    /**
     * Spring Boot对象反转
     *
     * @param type 对象类型
     * @return 反转对象
     */
    public static <T> T resolve(Class<T> type) {
        return resolve(type, null, null);
    }

    /**
     * 解析 Bean
     *
     * @param beanName Bean 别名
     * @return 结果
     */
    public static <T> T resolve(String beanName) {
        return resolve(null, beanName, null);
    }

    /**
     * 解析 Bean
     *
     * @param type     Bean 类型
     * @param beanName Bean 别名
     * @return 结果
     */
    public static <T> T resolve(Class<T> type, String beanName) {
        return resolve(type, beanName, null);
    }

    /**
     * 解析 Bean
     *
     * @param type Bean 类型
     * @param args 构造器参数
     * @return 结果
     */
    public static <T> T resolve(Class<T> type, Object... args) {
        return resolve(type, null, args);
    }

    /**
     * 解析 Bean
     *
     * @param type     Bean 类型
     * @param beanName Bean 别名
     * @param args     构造器参数
     * @return 结果
     */
    public static <T> T resolve(Class<T> type, String beanName, Object... args) {
        try {
            if (StringUtils.hasLength(beanName)) {
                //通过别名获取。
                return servletContext.getBean(beanName, type);
            } else {
                if (null != args && args.length > 0) {
                    //有参构造。
                    return servletContext.getBean(type, args);
                } else {
                    //无参构造。
                    return servletContext.getBean(type);
                }
            }
        } catch (Exception e) {
            log.error(String.format("Spring resolve failed, type: %s, name: %s", type, beanName), e);
            throw e;
        }
    }

    // endregion
}

