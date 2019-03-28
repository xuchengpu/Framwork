package com.xcp.framework.architect_day25;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xcp.framework.R;
import com.xcp.framework.architect_day25.okhttp.Call;
import com.xcp.framework.architect_day25.okhttp.Callback;
import com.xcp.framework.architect_day25.okhttp.OkHttpClient;
import com.xcp.framework.architect_day25.okhttp.Request;
import com.xcp.framework.architect_day25.okhttp.RequestBody;
import com.xcp.framework.architect_day25.okhttp.Response;

import java.io.File;
import java.io.IOException;

public class FFActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = new File("");

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new RequestBody()
                .type(RequestBody.FORM)
                .addParam("file", RequestBody.create(file))
                .addParam("file2", RequestBody.create(file))
                .addParam("pageSize", 1 + "");

        Request request = new Request.Builder()
                .url("https://api.saiwuquan.com/api/appv2/sceneModel")
                .post(requestBody).build();

        RequestBody body = new RequestBody()
                .type(RequestBody.FORM)
                .addParam("username", "cup")
                .addParam("password", "123456");
        Request request2 = new Request.Builder()
                .post(body)
                .url("https://www.wanandroid.com/user/login")
                .build();

        Call call = client.newCall(request2);

        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", response.string());
                //Log.e("TAG",response.string());
            }
        });
    }

    // 设计的类比较多，写出来代码的思想（UML）, 体现的调用形式搭起来，把里面的细节填好
}
