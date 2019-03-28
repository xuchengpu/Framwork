package com.xcp.framework.okhttp;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by 许成谱 on 2019/3/28 12:16.
 * qq:1550540124
 * 热爱生活每一天！
 */
public class RealCall implements Call {
    private final OkhttpClient client;
    private final Request orignalRequest;

    private RealCall(OkhttpClient okhttpClient, Request request) {
        this.client = okhttpClient;
        this.orignalRequest = request;
    }

    public static Call newRealCall(OkhttpClient okhttpClient, Request request) {
        return new RealCall(okhttpClient, request);
    }

    @Override
    public void enqueue(CallBack callBack) {
        AsyncRunable asyncRunable = new AsyncRunable(callBack);
        client.dispatcher.enqueue(asyncRunable);
    }

    @Override
    public Response execute() {
        return null;
    }

    class AsyncRunable extends NameRunable {
        private final CallBack callBack;

        public AsyncRunable(CallBack callBack) {
            this.callBack=callBack;
        }

        @Override
        protected void execute() {
            Log.e("TAG", "AsyncRunable--execute()");
            //这里我们使用HttpURLConnection实现以下。okhttp使用的是okio+socket。
            final Request request = orignalRequest;
            try {
                URL url=new URL(request.url);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                //对https进行一些处理
                if(connection instanceof HttpsURLConnection) {
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
//                    httpsURLConnection.setSSLSocketFactory();
//                    httpsURLConnection.setHostnameVerifier();
                }
                connection.setConnectTimeout(8000);//设置超时
                //写一些东西
                connection.setRequestMethod(request.method.name());
                connection.setDoOutput(request.method.doOutput());

                RequestBody requestBody = request.requestBody;
                if(requestBody!=null) {
                    // 头信息
                    connection.setRequestProperty("Content-Type",requestBody.getContentType());
                    connection.setRequestProperty("Content-Length",Long.toString(requestBody.getContentLength()));
                }
                //连接
                connection.connect();
                // 写内容
                if( requestBody!=null) {
                    requestBody.onWriteBody(connection.getOutputStream());
                }
                //响应码
                int responseCode = connection.getResponseCode();
                if(responseCode==200||responseCode==406) {
                    Response response=new Response(connection.getInputStream());
                    callBack.onResponse(RealCall.this,response);
                }
            } catch (Exception e) {
                callBack.onFailure(RealCall.this, (IOException) e);
            }
        }
    }
}
