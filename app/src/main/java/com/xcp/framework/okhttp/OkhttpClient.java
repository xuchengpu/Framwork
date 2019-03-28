package com.xcp.framework.okhttp;

/**
 * Created by 许成谱 on 2019/3/28 11:33.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class OkhttpClient {


    public final Dispatcher dispatcher;

    public OkhttpClient(Builder builder) {
        this.dispatcher=builder.dispatcher;
    }
    public OkhttpClient(){
        this(new Builder());
    }

    public Call newCall(Request request) {

        return RealCall.newRealCall(this,request);
    }
    public static class Builder{
        Dispatcher dispatcher;
        // 链接超时
        // https 证书的一些参数
        // 拦截器
        // 等等
        public Builder(){
            dispatcher=new Dispatcher();
        }
        public OkhttpClient build(){
            return new OkhttpClient(this);
        }
    }

}
