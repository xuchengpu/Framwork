package com.xcp.framework.eventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xcp.framework.R;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void change(View view) {
        MyEventBus.getDefault().post(new MessageBean(0,"手写EventBus"));
    }
}
