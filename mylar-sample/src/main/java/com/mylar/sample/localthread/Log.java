package com.mylar.sample.localthread;

/**
 * @author wangz
 * @date 2022/3/31 0031 23:38
 */
public class Log {

    private static final ThreadLocal<TSLog> tsLogCollection = new ThreadLocal<TSLog>();

    public static void println(String s) {
        getTSLog().println(s);
    }

    public static void close() {
        getTSLog().close();
    }

    private static TSLog getTSLog() {
        TSLog tsLog = (TSLog) tsLogCollection.get();
        if (tsLog == null) {
            tsLog = new TSLog(Thread.currentThread().getName() + "-log.txt");
            tsLogCollection.set(tsLog);
        }
        return tsLog;
    }
}
