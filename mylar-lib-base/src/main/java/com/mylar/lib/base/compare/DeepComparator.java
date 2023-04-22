package com.mylar.lib.base.compare;

import com.mylar.lib.base.utils.ObjectExtUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 深度比较器
 * <p>
 * 业务说明：
 * 1、比较器提供递归深度比较流程控制，与比较逻辑解耦
 * 2、支持用户自定义差异比较，可随意扩展
 * 3、支持循环引用检测，避免死循环
 * 4、支持通过特性进行扩展
 * 5、整体设计思路参考序列化器的实现方式
 *
 * @author wangz
 * @date 2023/4/22 0022 19:49
 */
public class DeepComparator {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param compareDiff 比较差异
     */
    public DeepComparator(ICompareDiff compareDiff) {
        this.compareDiff = compareDiff;
        this.compareFeatures = new ArrayList<>();
    }

    // endregion

    // 变量

    /**
     * 差异比较
     */
    private final ICompareDiff compareDiff;

    /**
     * 比较特性
     */
    private final List<CompareFeature> compareFeatures;

    // endregion

    // region 公共方法

    /**
     * 比较
     *
     * @param o1 对象1
     * @param o2 对象2
     * @throws IllegalAccessException 异常
     */
    public void compare(Object o1, Object o2) throws IllegalAccessException {

        // 对象全部为空
        if (o1 == null || o2 == null) {
            return;
        }

        // 封装引用上下文
        ReferenceContext o1Context = new ReferenceContext(null, o1);
        ReferenceContext o2Context = new ReferenceContext(null, o2);

        // 遍历属性字段
        List<Field> declaredFields = this.getDeclaredFields(o1.getClass());
        for (Field declaredField : declaredFields) {

            // 属性可见
            declaredField.setAccessible(true);

            // 封装属性字段引用上下文
            ReferenceContext o1FieldContext = new ReferenceContext(o1Context, declaredField.get(o1));
            ReferenceContext o2FieldContext = new ReferenceContext(o2Context, declaredField.get(o2));

            // 比较属性字段
            this.compareField(declaredField, o1FieldContext, o2FieldContext);
        }
    }

    /**
     * 添加比较特性
     *
     * @param features 特性
     */
    public void addCompareFeatures(CompareFeature... features) {
        this.compareFeatures.addAll(Arrays.asList(features));
    }

    // endregion

    // 私有方法

    /**
     * 比较属性字段
     *
     * @param field     属性字段
     * @param o1Context 对象1引用上下文
     * @param o2Context 对象2引用上下文
     * @throws IllegalAccessException 异常
     */
    private void compareField(Field field, ReferenceContext o1Context, ReferenceContext o2Context) throws IllegalAccessException {

        // 是否忽略此字段比较
        if (this.compareDiff.ignore(field)) {
            return;
        }

        // 获取待比较对象
        Object o1 = o1Context.getCurrent();
        Object o2 = o2Context.getCurrent();

        // 全部为空时结束比较
        if (o1 == null && o2 == null) {
            return;
        }

        // 获取非空对象
        Object notNullObj = o1 != null ? o1 : o2;

        // 对象是基础类型：直接执行比较
        if (ObjectExtUtils.isBaseType(notNullObj)) {
            this.compareDiff.doCompare(field, o1, o2);
            return;
        }

        // 对象非基础类型，但不再继续递归比较属性字段：直接执行比较
        if (!this.compareDiff.continueCompareFields(field, o1, o2)) {
            this.compareDiff.doCompare(field, o1, o2);
            return;
        }

        // 遍历属性字段
        List<Field> declaredFields = this.getDeclaredFields(notNullObj.getClass());
        for (Field declaredField : declaredFields) {

            // 属性可见
            declaredField.setAccessible(true);

            // 封装属性字段引用上下文
            ReferenceContext o1FieldContext = new ReferenceContext(o1Context, o1 == null ? null : declaredField.get(o1));
            ReferenceContext o2FieldContext = new ReferenceContext(o2Context, o2 == null ? null : declaredField.get(o2));

            // 循环引用检测
            if (!this.compareFeatures.contains(CompareFeature.DisableCircularReferenceDetect)) {
                if (this.existCircularReference(o1FieldContext) || this.existCircularReference(o2FieldContext)) {
                    continue;
                }
            }

            // 递归比较属性字段
            this.compareField(declaredField, o1FieldContext, o2FieldContext);
        }
    }

    /**
     * 获取属性字段
     *
     * @param clazz 当前类型
     * @return 结果
     */
    private List<Field> getDeclaredFields(Class<?> clazz) {

        // 获取当前类属性字段
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList<>(Arrays.asList(declaredFields));

        // 禁用基类属性字段比较
        if (this.compareFeatures.contains(CompareFeature.DisableSuperDeclaredFieldCompare)) {
            return fields;
        }

        // 迭代获取基类属性字段
        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null) {
            Field[] superDeclareFields = superclass.getDeclaredFields();
            fields.addAll(Arrays.asList(superDeclareFields));
            superclass = superclass.getSuperclass();
        }

        return fields;
    }

    /**
     * 是否存在循环引用
     *
     * @param context 上下文
     * @return 是否存在
     */
    private boolean existCircularReference(ReferenceContext context) {

        // 校验
        if (context == null || context.getCurrent() == null || context.getParent() == null) {
            return false;
        }

        // 当前对象
        Object current = context.getCurrent();

        // 是否存在循环引用
        boolean flag = false;

        // 当前对象的父级上下文
        ReferenceContext parent = context.getParent();
        for (; ; ) {

            // 父级为空，跳出循环
            if (parent == null || parent.getCurrent() == null) {
                break;
            }

            // 当前对象与父级对象引用相同，说明存在循环引用，跳出循环
            if (current == parent.getCurrent()) {
                flag = true;
                break;
            }

            // 继续向父级追溯
            parent = parent.getParent();
        }

        return flag;
    }

    // endregion
}
