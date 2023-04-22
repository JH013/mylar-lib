package com.mylar.lib.base.compare;

/**
 * 引用上下文
 *
 * @author wangz
 * @date 2023/4/22 0022 19:50
 */
public class ReferenceContext {

    /**
     * 构造方法
     *
     * @param parent  父级上下文
     * @param current 当前对象
     */
    public ReferenceContext(ReferenceContext parent, Object current) {
        this.parent = parent;
        this.current = current;
    }

    /**
     * 父级上下文
     */
    private ReferenceContext parent;

    /**
     * 当前对象
     */
    private Object current;

    // region getter & setter

    public ReferenceContext getParent() {
        return parent;
    }

    public void setParent(ReferenceContext parent) {
        this.parent = parent;
    }

    public Object getCurrent() {
        return current;
    }

    public void setCurrent(Object current) {
        this.current = current;
    }

    // endregion
}
