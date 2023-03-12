package com.mylar.sample.modules.blocking.queue;

import java.util.PriorityQueue;
import java.util.concurrent.*;

/**
 * @author wangz
 * @date 2022/1/23 0023 23:20
 */
public class BlockingTest {

    PriorityQueue<Object> priorityQueue;

    DelayQueue delayQueue;

    ArrayBlockingQueue<Object> arrayBlockingQueue;

    LinkedBlockingQueue<Object> linkedBlockingQueue;

    PriorityBlockingQueue<Object> priorityBlockingQueue;

    SynchronousQueue<Object> synchronousQueueTest;

    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    LinkedTransferQueue<Object> linkedTransferQueue;

    LinkedBlockingDeque<Object> linkedBlockingDeque;

}
