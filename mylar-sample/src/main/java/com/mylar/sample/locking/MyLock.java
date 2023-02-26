package com.mylar.sample.locking;

import sun.awt.Mutex;

import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangz
 * @date 2022/1/3 0003 23:54
 */
public class MyLock {

    Mutex mutex;

    AbstractQueuedSynchronizer abstractQueuedSynchronizer;

    ReentrantLock reentrantLock;

//    SpinLock spinLock;

    AtomicStampedReference atomicStampedReference;
}
