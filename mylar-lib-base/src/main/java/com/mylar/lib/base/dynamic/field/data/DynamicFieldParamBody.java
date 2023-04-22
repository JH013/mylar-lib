package com.mylar.lib.base.dynamic.field.data;

import java.util.List;

/**
 * 动态字段参数体
 *
 * @author wangz
 * @date 2023/4/22 0022 20:00
 */
public class DynamicFieldParamBody {

    /**
     * 内容集合
     */
    private List<DynamicFieldParamItem> items;

    /**
     * 添加
     *
     * @param item item
     */
    public void addItem(DynamicFieldParamItem item) {
        this.items.add(item);
    }

    // region getter & setter

    public List<DynamicFieldParamItem> getItems() {
        return items;
    }

    public void setItems(List<DynamicFieldParamItem> items) {
        this.items = items;
    }

    // endregion
}
