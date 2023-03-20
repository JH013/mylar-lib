package com.mylar.sample.modules.common.controller;

import com.mylar.lib.base.utils.JsonUtils;
import com.mylar.sample.modules.common.data.MylarDto;
import com.mylar.sample.modules.common.data.MylarEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试枚举
 *
 * @author wangz
 * @date 2023/3/20 0020 22:05
 */
@RestController
@RequestMapping(value = "/test/enum")
public class TestEnumController {

    /**
     * 根据枚举值获取枚举对象
     *
     * @param value 值
     * @return 名称
     */
    @RequestMapping(value = "/convert")
    public String convert(Integer value) {
        MylarEnum mylarEnum = MylarEnum.create(value);
        return mylarEnum == null ? null : mylarEnum.getName();
    }

    /**
     * 反序列化
     * <p>
     * json 示例：{"name":"wz","level":100,"type":"SYSTEM SETTINGS"}
     *
     * @param json json
     * @return 结果
     */
    @RequestMapping(value = "/deserialize")
    public String deserialize(String json) {
        MylarDto mylarDto = JsonUtils.deJson(json, MylarDto.class);
        return "success.";
    }
}
