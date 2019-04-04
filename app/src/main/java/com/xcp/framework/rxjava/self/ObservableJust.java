package com.xcp.framework.rxjava.self;

/**
 * Created by 许成谱 on 2019/4/4 17:23.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class ObservableJust<T> extends Observable<T> {

    private final T item;

    public ObservableJust(T item) {
        this.item = item;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        //用一个代理来实现 方便扩展
        ScalarDisposable scalarDisposable = new ScalarDisposable(observer, item);
        observer.onSubscribe();
        scalarDisposable.run();

    }

    private static class ScalarDisposable<T> {

        private final Observer<T> observer;
        private final T item;

        public ScalarDisposable(Observer<T> observer, T item) {
            this.observer = observer;
            this.item = item;
        }

        public void run() {
            try {
                observer.onNext(item);
                observer.onComplete();
            } catch (Exception e) {
                observer.onError(e);
            }

        }
    }
}
