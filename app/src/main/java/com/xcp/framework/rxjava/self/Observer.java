package com.xcp.framework.rxjava.self;

import io.reactivex.annotations.NonNull;

/**
 * Created by 许成谱 on 2019/4/4 17:15.
 * qq:1550540124
 * 热爱生活每一天！
 */
public interface Observer<T> {
    void onSubscribe();
    void onNext(@NonNull T item);
    void onError(@NonNull Throwable e);
    void onComplete();
}
