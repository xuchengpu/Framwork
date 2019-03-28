package com.xcp.framework.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xcp.framework.R;

import java.io.IOException;

public class OkhttpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        initData();
    }

    private void initData() {
        OkhttpClient client = new OkhttpClient();

        RequestBody body = new RequestBody()
                .type(RequestBody.FORM)
                .addParams("mobile", "2NwhVvZTbiLupvY3lsZeBw==")
                .addParams("verify_code", "5424")
                .addParams("device_type", "1");
        Request request = new Request.Builder()
                .post(body)
                .url("https://app.preova.com/api/app-user-login")
                .build();
//        Request request2 = new Request.Builder()
//                .url("https://www.baidu.com")
//                .build();

        Call call = client.newCall(request);
        call.enqueue(new CallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure=="+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", "onResponse=="+response.getString());
            }
        });


    }
}
