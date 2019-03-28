package com.xcp.framework.okhttp;

/**
 * Created by 许成谱 on 2019/3/28 12:57.
 * qq:1550540124
 * 热爱生活每一天！
 */
public abstract class NameRunable implements Runnable {
    @Override
    public void run() {
        execute();
    }

    protected abstract void execute() ;
}
