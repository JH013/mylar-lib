package com.mylar.lib.base.enhance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 应用上下文持有
 *
 * @author wangz
 * @date 2023/3/12 0012 20:19
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    // region 变量 & 常量

    /**
     * Servlet Context
     */
    private static ApplicationContext servletContext;

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(SpringContextHolder.class);

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
        if (SpringContextHolder.servletContext == null) {
            SpringContextHolder.servletContext = applicationContext;
        }
    }

    // endregion

    // region 公共方法

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public static ApplicationContext getContext() {
        return SpringContextHolder.servletContext;
    }

    // endregion
}

