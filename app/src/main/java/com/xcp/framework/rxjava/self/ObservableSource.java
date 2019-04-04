package com.xcp.framework.rxjava.self;

/**
 * Created by 许成谱 on 2019/4/4 17:12.
 * qq:1550540124
 * 热爱生活每一天！
 */
public interface ObservableSource<T> {
    void subscribe(Observer< T> observer);
}
