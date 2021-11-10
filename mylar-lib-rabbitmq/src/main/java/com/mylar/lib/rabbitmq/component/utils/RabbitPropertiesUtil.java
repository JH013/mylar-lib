package com.mylar.lib.rabbitmq.component.utils;

import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.Environment;

/**
 * 属性配置工具类
 *
 * @author wangz
 * @date 2021/11/7 0007 22:30
 */
public class RabbitPropertiesUtil {

    /**
     * 读取配置文件并注入到实体
     *
     * @param environment environment
     * @param prefix      前缀
     * @param clazz       实体类型
     * @param <T>         泛型
     * @return 实体
     */
    public static <T> T dataBind(Environment environment, String prefix, Class<T> clazz) {

        Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
        Binder binder = new Binder(sources);
        return binder.bind(prefix, Bindable.of(clazz)).get();
    }
}
