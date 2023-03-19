package com.mylar.lib.base.config.props;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.Properties;

/**
 * 属性配置工具类
 *
 * @author wangz
 * @date 2023/3/19 0019 19:53
 */
public class PropertiesUtils {

    // region 公共方法

    /**
     * 根据属性配置赋值
     *
     * @param obj   实体对象
     * @param props 属性配置
     * @throws NoSuchMethodException                       异常
     * @throws IllegalAccessException                      异常
     * @throws java.lang.reflect.InvocationTargetException 异常
     * @throws IntrospectionException                      异常
     */
    public static void setBeanProps(Object obj, Properties props)
            throws NoSuchMethodException, IllegalAccessException,
            java.lang.reflect.InvocationTargetException,
            IntrospectionException {
        props.remove("class");
        props.remove("provider");

        BeanInfo bi = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propDescs = bi.getPropertyDescriptors();
        PropertiesParser pp = new PropertiesParser(props);

        java.util.Enumeration<Object> keys = props.keys();
        while (keys.hasMoreElements()) {
            String name = (String) keys.nextElement();
            String c = name.substring(0, 1).toUpperCase(Locale.US);
            String methName = "set" + c + name.substring(1);

            java.lang.reflect.Method setMeth = getSetMethod(methName, propDescs);

            try {
                if (setMeth == null) {
                    throw new NoSuchMethodException(
                            "No setter for property '" + name + "'");
                }

                Class<?>[] params = setMeth.getParameterTypes();
                if (params.length != 1) {
                    throw new NoSuchMethodException(
                            "No 1-argument setter for property '" + name + "'");
                }

                if (params[0].equals(int.class)) {
                    setMeth.invoke(obj, new Object[]{Integer.valueOf(pp.getIntProperty(name))});
                } else if (params[0].equals(long.class)) {
                    setMeth.invoke(obj, new Object[]{Long.valueOf(pp.getLongProperty(name))});
                } else if (params[0].equals(float.class)) {
                    setMeth.invoke(obj, new Object[]{Float.valueOf(pp.getFloatProperty(name))});
                } else if (params[0].equals(double.class)) {
                    setMeth.invoke(obj, new Object[]{Double.valueOf(pp.getDoubleProperty(name))});
                } else if (params[0].equals(boolean.class)) {
                    setMeth.invoke(obj, new Object[]{Boolean.valueOf(pp.getBooleanProperty(name))});
                } else if (params[0].equals(String.class)) {
                    setMeth.invoke(obj, new Object[]{pp.getStringProperty(name)});
                } else {
                    throw new NoSuchMethodException(
                            "No primitive-type setter for property '" + name
                                    + "'");
                }
            } catch (NumberFormatException nfe) {
                throw new RuntimeException("Could not parse property '"
                        + name + "' into correct data type: " + nfe.toString());
            }
        }
    }

    // endregion

    // region 私有方法

    /**
     * 获取方法
     *
     * @param name  set 方法
     * @param props 属性解释器
     * @return 结果
     */
    private static java.lang.reflect.Method getSetMethod(String name, PropertyDescriptor[] props) {
        for (PropertyDescriptor prop : props) {
            java.lang.reflect.Method wMeth = prop.getWriteMethod();
            if (wMeth != null && wMeth.getName().equals(name)) {
                return wMeth;
            }
        }

        return null;
    }

    // endregion
}
