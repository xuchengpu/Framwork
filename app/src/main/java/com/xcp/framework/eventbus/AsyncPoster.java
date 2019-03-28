package com.xcp.framework.eventbus;

/**
 * Created by 许成谱 on 2019/3/21 18:10.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class AsyncPoster implements Runnable {
    private Subscription subscription;
    private Object event;

    public AsyncPoster(Subscription subscription, Object event) {
        this.subscription = subscription;
        this.event = event;
    }

    @Override
    public void run() {
        try {
            //通过反射去执行
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
