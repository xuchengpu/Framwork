package com.xcp.framework.eventbus;

import android.os.Handler;
import android.os.Looper;

import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 许成谱 on 2019/3/21 16:37.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class MyEventBus {
    private static volatile MyEventBus instance;
    // subscriptionsByEventType 这个集合存放的是？
    // key 是 Event 参数的类
    // value 存放的是 Subscription 的集合列表
    // Subscription 包含两个属性，一个是 subscriber 订阅者（反射执行对象），一个是 SubscriberMethod 注解方法的所有属性参数值
    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    // typesBySubscriber 这个集合存放的是？
    // key 是所有的订阅者
    // value 是所有订阅者里面方法的参数的class
    private final Map<Object, List<Class<?>>> typesBySubscriber;
    private ExecutorService executorService= Executors.newCachedThreadPool();

    private MyEventBus() {
        typesBySubscriber = new HashMap<Object, List<Class<?>>>();
        subscriptionsByEventType = new HashMap<>();
    }

    public static MyEventBus getDefault() {
        if (instance == null) {
            synchronized (MyEventBus.class) {
                if (instance == null) {
                    instance = new MyEventBus();
                }
            }
        }
        return instance;
    }

    public void register(Object subscriber) {
        // 1. 解析所有方法封装成 SubscriberMethod 的集合
        List<SubscriberMethod> subscriberMethods = new ArrayList<>();
        Class<?> aClass = subscriber.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            Subscribe annotation = method.getAnnotation(Subscribe.class);
            if (annotation != null) {
                // 所有的Subscribe属性 解析出来
                Class<?>[] parameterTypes = method.getParameterTypes();
                SubscriberMethod subscriberMethod = new SubscriberMethod(method, parameterTypes[0], annotation.threadMode(), annotation.priority(), annotation.sticky());
                subscriberMethods.add(subscriberMethod);
            }
        }
        // 2. 按照规则存放到 subscriptionsByEventType 里面去
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
            subscribe(subscriber, subscriberMethod);
        }


    }

    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        Class<?> eventType = subscriberMethod.eventType;
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType, subscriptions);
        }
        // 判断优先级 （不写）
        //把封装后的对象添加进去
        Subscription subscription = new Subscription(subscriber, subscriberMethod);
        subscriptions.add(subscription);

        //添加进typesBySubscriber中，方便解注册时用
        List<Class<?>> eventTypes = typesBySubscriber.get(subscriber);
        if (eventTypes == null) {
            eventTypes = new ArrayList<>();
            typesBySubscriber.put(subscriber, eventTypes);
        }
        if (!eventTypes.contains(eventType)) {
            eventTypes.add(eventType);
        }
    }

    public void unRegister(Object subscriber) {
        //查找当前注册的类中含有多少个用于通讯Event 参数的类
        List<Class<?>> eventTypes = typesBySubscriber.get(subscriber);
        if (eventTypes != null) {
            for (Class<?> eventType : eventTypes) {
                removeSubscriber(subscriber, eventType);
            }
        }
    }

    private void removeSubscriber(Object subscriber, Class<?> eventType) {
        //根据Event参数的类，在subscriptionsByEventType中查找包含有多少个封装过对应method的Subscription对象
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions != null) {
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                Subscription subscription = subscriptions.get(i);
                if (subscription.subscriber == subscriber) {
                    subscriptions.remove(i);//移除当前解除册类的所有封装对象
                    i--;
                    size--;
                }
            }
        }
    }

    public void post(Object event) {
        // 遍历 subscriptionsByEventType，找到符合的方法调用方法的 method.invoke() 执行。要注意线程切换
        Class<?> eventType = event.getClass();
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if(subscriptions!=null) {
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                executeMethod(subscriptions.get(i), event);
            }
        }
    }

    private void executeMethod(final Subscription subscription, final Object event) {
        ThreadMode threadMode = subscription.subscriberMethod.threadMode;
        boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();
        switch (threadMode) {
            case POSTING: //与调用线程在相同线程
                invokeMethod(subscription, event);
                break;
            case MAIN:
                if (isMainThread) {
                    invokeMethod(subscription, event);
                } else {
                    Handler handler=new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invokeMethod(subscription, event);
                        }
                    });
                }
                break;
            case BACKGROUND: //不在主线程即可
                if(!isMainThread) {
                   executorService.execute(new AsyncPoster(subscription,event));
                }
                break;
            case ASYNC: //与调用线程异步
                executorService.execute(new AsyncPoster(subscription,event));
                break;
        }
    }

    private void invokeMethod(Subscription subscription, Object event) {
        try {
            //通过反射去执行
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
