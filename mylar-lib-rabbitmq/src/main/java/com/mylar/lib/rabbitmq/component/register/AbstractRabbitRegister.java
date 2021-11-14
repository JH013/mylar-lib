package com.mylar.lib.rabbitmq.component.register;

import com.mylar.lib.rabbitmq.component.configuration.RabbitElementCache;
import com.mylar.lib.rabbitmq.component.constant.RabbitConstant;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * RabbitMQ注册抽象类
 *
 * @author wangz
 * @date 2021/11/10 0010 23:17
 */
public abstract class AbstractRabbitRegister {

    /**
     * 环境配置
     */
    protected final Environment environment;

    /**
     * 应用上下文
     */
    protected final ApplicationContext applicationContext;

    /**
     * RabbitMQ相关配置缓存
     */
    protected final RabbitElementCache rabbitElementCache;

    /**
     * 构造方法
     *
     * @param environment        环境配置
     * @param applicationContext 应用上下文
     */
    public AbstractRabbitRegister(Environment environment, ApplicationContext applicationContext) {
        this.environment = environment;
        this.applicationContext = applicationContext;
        this.rabbitElementCache = this.applicationContext.getBean(RabbitConstant.BEAN_RABBIT_ELEMENT_CACHE, RabbitElementCache.class);
    }

    /**
     * 注册
     */
    public abstract void register();
}
