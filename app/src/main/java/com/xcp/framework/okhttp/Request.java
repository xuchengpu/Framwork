package com.xcp.framework.okhttp;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by 许成谱 on 2019/3/28 11:34.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class Request {
    public final String url;
    public final Method method;
    public final Map<String, String> headers;
    public final RequestBody requestBody;

    public Request(Builder builder) {
        this.url=builder.url;
        this.method=builder.method;
        this.headers=builder.headers;
        this.requestBody=builder.requestBody;
    }

    public static class Builder {
        private String url;
        private Method method;
        Map<String, String> headers;
        RequestBody requestBody;

        public Builder() {
            method = Method.GET;
            headers = new HashMap<>();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder header(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder get() {
            method = Method.GET;
            return this;
        }

        public Builder post(RequestBody body) {
            method = Method.POST;
            requestBody = body;
            return this;
        }

        public Request build(){
            return  new Request(this);
        }
    }
}
