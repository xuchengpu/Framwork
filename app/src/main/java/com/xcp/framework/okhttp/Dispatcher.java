package com.xcp.framework.okhttp;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 许成谱 on 2019/3/28 12:33.
 * qq:1550540124
 * 热爱生活每一天！
 * 线程池
 */
public class Dispatcher {
    private ThreadPoolExecutor executorService;

    public void enqueue(RealCall.AsyncRunable runable) {
        executorService().execute(runable);
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r, "OkHttp");
                    thread.setDaemon(false);
                    return thread;
                }
            });
        }
        return executorService;
    }

}
