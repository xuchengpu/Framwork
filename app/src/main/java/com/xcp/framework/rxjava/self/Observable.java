package com.xcp.framework.rxjava.self;


/**
 * Created by 许成谱 on 2019/4/4 17:13.
 * qq:1550540124
 * 热爱生活每一天！
 */
public  abstract class Observable<T> implements ObservableSource<T> {


    public static <T>Observable just(T item) {

        return onAssembly(new ObservableJust<T>(item));
    }

    private static <T> Observable onAssembly(ObservableJust<T> sourse) {
        //留出来方便扩展加工
        return sourse;
    }

    @Override
    public void subscribe(Observer<T> observer) {
        subscribeActual(observer);
    }

    public abstract void subscribeActual(Observer< T> observer);
}
