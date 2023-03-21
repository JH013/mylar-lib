package com.mylar.sample.modules.thread.pool;

import com.mylar.lib.base.enhance.SpringResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 测试线程池
 *
 * @author wangz
 * @date 2023/3/22 0022 0:18
 */
@RestController
@RequestMapping(value = "/test/thread/pool")
public class TestThreadPoolController {

    /**
     * 根据枚举值获取枚举对象
     *
     * @param value 值
     * @return 名称
     */
    @RequestMapping(value = "/submitCommon")
    public String submitCommon(Integer value) throws ExecutionException, InterruptedException {
        CommonThreadPool threadPool = SpringResolver.resolve(CommonThreadPool.class);
        Future<Integer> future = threadPool.submit(() -> {
            System.out.println(String.format("[%s] [%s] start.", LocalDateTime.now(), Thread.currentThread().getName()));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            System.out.println(String.format("[%s] [%s] end.", LocalDateTime.now(), Thread.currentThread().getName()));
            return value;
        });

        return "success";
    }
}
