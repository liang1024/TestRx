package com.liangzai.rxsubscription;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 简单演示 Subscription
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSubscription = Observable.just("123").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, " s:" + s);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mSubscription) {
            Log.e(TAG, "解绑了");
            mSubscription.unsubscribe();
        }
    }

    //    结果：
    //    01-24 23:31:00.045 15414-15414/com.liangzai.rxsubscription E/MainActivity:  s:123
    //    01-24 23:31:06.785 15414-15414/com.liangzai.rxsubscription E/MainActivity: 解绑了
}
