package com.mylar.lib.base.dynamic.field.data;

import java.util.List;

/**
 * 动态字段结果体
 *
 * @author wangz
 * @date 2023/4/22 0022 20:01
 */
public class DynamicFieldResultBody {

    /**
     * 内容集合
     */
    private List<DynamicFieldResultItem> items;

    // region getter & setter

    public List<DynamicFieldResultItem> getItems() {
        return items;
    }

    public void setItems(List<DynamicFieldResultItem> items) {
        this.items = items;
    }

    // endregion
}
