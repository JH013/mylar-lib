package com.mylar.lib.xxljob.simple;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangz
 * @date 2021/9/20 0020 21:28
 */
@RestController
@RequestMapping(value = "/xxl")
public class XxlJobController {

    @RequestMapping(value = "/test")
    public Long test() {
        return 0L;
    }
}
