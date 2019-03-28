package com.xcp.framework.okhttp;



/**
 * Created by 许成谱 on 2019/3/28 12:12.
 * qq:1550540124
 * 热爱生活每一天！
 */
public interface Call {
    void enqueue(CallBack callBack);

    Response execute();
}
