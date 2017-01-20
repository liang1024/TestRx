package com.lz.my_rxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import rx.Observable;
import rx.functions.Action1;

/**
 * Rxbus简单使用
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String tag = "tag";
    private Observable<String> mOb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOb = RxBus.get().register(tag, String.class);
        mOb.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, "接收到内容了，内容是:" + s);
            }
        });

    }

    public void onclick(View view) {
        RxBus.get().post(tag, "我是Rxbus");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(tag, mOb);
    }
}
