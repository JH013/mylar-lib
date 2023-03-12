package com.mylar.lib.base.data;

/**
 * Lambda 表达式对象
 *
 * @author wangz
 * @date 2023/2/26 0026 18:55
 */
public class LambdaExpressionObject<T> {

    /**
     * 需要赋值的对象
     */
    private T obj = null;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}