package com.mylar.lib.job.single.control;

import com.mylar.lib.base.enhance.InstanceCache;
import com.mylar.lib.base.utils.AnnotationUtils;
import com.mylar.lib.job.single.annotation.SingleJobControlConfig;
import com.mylar.lib.job.single.core.AbstractSingleJob;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 单机任务控制解析器
 *
 * @author wangz
 * @date 2023/3/12 0012 5:12
 */
public class SingleJobControlResolver {

    // region 构造方法

    /**
     * 构造方法
     */
    private SingleJobControlResolver() {

    }

    // endregion

    // region 变量 & 常量

    /**
     * 实例缓存
     */
    private static final InstanceCache instanceCache = new InstanceCache();

    // endregion

    // region 公共方法

    /**
     * 解析任务控制项
     *
     * @param singleJobClazz 单机任务类型
     * @return 结果
     */
    public static SingleJobControlParam resolveJobControl(Class<? extends AbstractSingleJob> singleJobClazz) {

        // 任务控制参数
        SingleJobControlParam controlParam = new SingleJobControlParam();

        // 获取全部注解
        Annotation[] annotations = singleJobClazz.getAnnotations();
        for (Annotation annotation : annotations) {

            // 注解类型为 "任务控制配置"
            if (annotation.annotationType().isAssignableFrom(SingleJobControlConfig.class)) {

                // 遍历任务控制配置
                SingleJobControlConfig controlConfig = (SingleJobControlConfig) annotation;
                for (Class<? extends ISingleJobControl> jobControl : controlConfig.controls()) {
                    controlParam.addItem(instanceCache.getInstance(jobControl));
                }
            }

            // 注解类型为 "任务控制配置" 的子类
            if (annotation.annotationType().isAnnotationPresent(SingleJobControlConfig.class)) {

                // 获取控制参数
                Map<String, Object> controlParams = AnnotationUtils.resolveProperties(annotation);

                // 遍历任务控制配置
                SingleJobControlConfig controlConfig = annotation.annotationType().getAnnotation(SingleJobControlConfig.class);
                for (Class<? extends ISingleJobControl> jobControl : controlConfig.controls()) {
                    controlParam.addItem(instanceCache.getInstance(jobControl), controlParams);
                }
            }
        }

        return controlParam;
    }

    // endregion
}
