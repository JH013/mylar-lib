package com.mylar.lib.job.single.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单机任务控制参数
 *
 * @author wangz
 * @date 2023/3/12 0012 5:49
 */
public class SingleJobControlParam {

    /**
     * 构造方法
     */
    public SingleJobControlParam() {
        this.items = new ArrayList<>();
    }

    /**
     * 任务控制项集合
     */
    private List<Item> items;

    /**
     * 添加任务控制项
     *
     * @param control 控制
     * @param params  参数
     */
    public void addItem(ISingleJobControl control, Map<String, Object> params) {
        this.items.add(new Item(control, params));
    }

    /**
     * 添加任务控制项
     *
     * @param control 控制
     */
    public void addItem(ISingleJobControl control) {
        this.items.add(new Item(control));
    }

    // region getter & setter

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    // endregion

    /**
     * 任务控制项
     */
    public static class Item {

        /**
         * 构造方法
         *
         * @param control 控制
         * @param params  参数
         */
        public Item(ISingleJobControl control, Map<String, Object> params) {
            this.control = control;
            if (params == null) {
                this.params = new HashMap<>();
            } else {
                this.params = params;
            }
        }

        /**
         * 构造方法
         *
         * @param control 控制
         */
        public Item(ISingleJobControl control) {
            this.control = control;
            this.params = new HashMap<>();
        }

        /**
         * 控制
         */
        private ISingleJobControl control;

        /**
         * 参数
         */
        private Map<String, Object> params;

        // region getter & setter

        public ISingleJobControl getControl() {
            return control;
        }

        public void setControl(ISingleJobControl control) {
            this.control = control;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

        // endregion
    }
}
