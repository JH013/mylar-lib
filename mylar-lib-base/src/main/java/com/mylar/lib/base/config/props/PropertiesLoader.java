package com.mylar.lib.base.config.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

/**
 * 属性配置加载器
 *
 * @author wangz
 * @date 2023/3/19 0019 17:14
 */
public class PropertiesLoader {

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(PropertiesLoader.class);

    // endregion

    // region 公共方法

    /**
     * 从属性配置文件中加载
     *
     * @param propertiesFile 属性配置文件名称
     * @return 属性配置
     */
    public Properties loadPropertiesFromFile(String propertiesFile) {
        Properties props = new Properties();
        InputStream in = null;
        try {

            ClassLoader cl = getClass().getClassLoader();
            if (cl == null) {
                cl = this.findClassloader();
            }

            if (cl == null) {
                log.error("Unable to find a class loader on the current thread or class.");
                return props;
            }

            in = cl.getResourceAsStream(propertiesFile);
            if (in == null) {
                log.error("Properties file {} not found in class path", propertiesFile);
                return props;
            }

            props.load(in);
        } catch (Exception e) {
            log.error("Load properties file {} failed.", propertiesFile, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) { /* ignore */ }
            }
        }

        return this.overrideWithSysProps(props);
    }

    // endregion

    // region 私有方法

    /**
     * 获取 Class Loader
     *
     * @return 结果
     */
    private ClassLoader findClassloader() {
        if (Thread.currentThread().getContextClassLoader() == null && getClass().getClassLoader() != null) {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        }

        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 重写系统属性配置
     *
     * @param props 属性配置
     * @return 重写后结果
     */
    private Properties overrideWithSysProps(Properties props) {
        Properties sysProps = null;
        try {
            sysProps = System.getProperties();
        } catch (AccessControlException e) {

        }

        if (sysProps != null) {
            props.putAll(sysProps);
        }

        return props;
    }

    // endregion
}
