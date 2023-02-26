package com.mylar.sample.future;

/**
 * @author wangz
 * @date 2022/3/30 0030 23:51
 */
public class FutureData implements Data {

    private RealData realdata = null;
    private boolean ready = false;

    public synchronized void setRealData(RealData realdata) {
        if (ready) {
            return;
        }
        this.realdata = realdata;
        this.ready = true;
        this.notifyAll();
    }

    public synchronized String getContent() {
        while (!ready) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        return realdata.getContent();
    }
}
