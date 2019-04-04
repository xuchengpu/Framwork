package com.xcp.framework.rxjava.self;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xcp.framework.R;

public class RxSelfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_self);
        Observable.just("")
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe() {
                        Log.e("TAG", "onSubscribe");
                    }

                    @Override
                    public void onNext(Object item) {
                        Log.e("TAG", "onNext");
                        Integer.parseInt("xx");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete");
                    }
                });
    }
}
