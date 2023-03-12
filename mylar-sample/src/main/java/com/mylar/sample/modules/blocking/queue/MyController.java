package com.mylar.sample.modules.blocking.queue;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangz
 * @date 2021/12/27 0027 0:20
 */
@RestController
@RequestMapping("/my")
public class MyController {

    @GetMapping("/test")
    public int callSync(String id) throws InterruptedException {
        MyQueue.run();
        return 1;
    }
}
