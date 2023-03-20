package com.mylar.sample.modules.common.data;

/**
 * Mylar DTO
 *
 * @author wangz
 * @date 2023/3/20 0020 22:08
 */
public class MylarDto {

    /**
     * 名称
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 类型
     */
    private MylarEnum type;

    // region getter & setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public MylarEnum getType() {
        return type;
    }

    public void setType(MylarEnum type) {
        this.type = type;
    }

    // endregion
}
