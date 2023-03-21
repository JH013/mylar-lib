package com.mylar.sample.modules.thread.pool;

import com.mylar.lib.thread.pool.annotation.ThreadPoolBasicConfig;
import com.mylar.lib.thread.pool.core.AbstractThreadPool;

/**
 * 通用线程池
 *
 * @author wangz
 * @date 2023/3/22 0022 0:12
 */
@ThreadPoolBasicConfig(
        key = "mylar-tp-common",
        description = "mylar common thread pool",
        corePoolSize = 2,
        maximumPoolSize = 5,
        blockingQueueCapacity = Integer.MAX_VALUE
)
public class CommonThreadPool extends AbstractThreadPool {
}
