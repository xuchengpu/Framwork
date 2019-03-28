package com.xcp.framework.architect_day25.okhttp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hcDarren on 2017/11/18.
 */

public class RealCall implements Call{
    private final Request orignalRequest;
    private final OkHttpClient client;
    public RealCall(Request request, OkHttpClient client) {
        orignalRequest = request;
        this.client = client;
    }

    public static Call newCall(Request request, OkHttpClient client) {
        return new RealCall(request,client);
    }

    @Override
    public void enqueue(Callback callback) {
        // 异步的
        AsyncCall asyncCall = new AsyncCall(callback);
        // 交给线程池
        client.dispatcher.enqueue(asyncCall);
    }

    @Override
    public Response execute() {
        return null;
    }

    final class AsyncCall extends NamedRunnable {
        Callback callback;
        public AsyncCall(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected void execute() {
            // 来这里，开始访问网络 Request -> Response
            Log.e("TAG","execute");
            // Volley xUtils Afinal AsyHttpClient
            // 基于 HttpUrlConnection , OkHttp = Socket + okio(IO)
            final Request request = orignalRequest;
            try {
                URL url = new URL(request.url);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if(urlConnection instanceof HttpsURLConnection){
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
                    // https 的一些操作
                    // httpsURLConnection.setHostnameVerifier();
                    // httpsURLConnection.setSSLSocketFactory();
                }
                // urlConnection.setReadTimeout();

                // 写东西
                urlConnection.setRequestMethod(request.method.name);
                urlConnection.setDoOutput(request.method.doOutput());

                RequestBody requestBody = request.requestBody;
                if(requestBody != null){
                    // 头信息
                    urlConnection.setRequestProperty("Content-Type",requestBody.getContentType());
                    urlConnection.setRequestProperty("Content-Length",Long.toString(requestBody.getContentLength()));
                }

                urlConnection.connect();

                // 写内容
                if(requestBody != null){
                    requestBody.onWriteBody(urlConnection.getOutputStream());
                }

                int statusCode = urlConnection.getResponseCode();
                if(statusCode == 200) {
                    InputStream inputStream = urlConnection.getInputStream();
                    Response response = new Response(inputStream);
                    callback.onResponse(RealCall.this,response);
                }
                // 进行一些列操作，状态码 200
            } catch (IOException e) {
                callback.onFailure(RealCall.this,e);
            }
        }
    }
}
