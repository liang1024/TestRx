package com.lz.rx_rxlifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.RxActivity;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Subscription mSubscribe1;
    private Subscription mSubscribe2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO    1.与Activity的生命周期进行绑定 ，1-1:Activity需要继承RxActivity,1-2:Fragment需要继承RxFragment
        //        composeBindToLifecycle();
        //        finish();

        //TODO    2.bindUntilEvent()  ActivityEvent类，对应activity的生命周期
        //        composeBindUntilEvent();


    }

    /**
     * 2.bindUntilEvent()
     * 使用ActivityEvent类，其中的CREATE、START、 RESUME、PAUSE、STOP、 DESTROY分别对应生命周期内的方法。使用bindUntilEvent指定在哪个生命周期方法调用时取消订阅。
     */
    private void composeBindUntilEvent() {
        //                .compose(this.bindUntilEvent(ActivityEvent.PAUSE))
        mSubscribe2 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                int i = 0;
                while (i < 1000000000) {
                    i++;
                }
                subscriber.onNext(String.valueOf(i));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                //        .compose(this.bindUntilEvent(ActivityEvent.PAUSE))
                //          指定bindUtilEvent在哪个生命周期方法调用时取消订阅

                .compose(this.<String>bindUntilEvent(ActivityEvent.PAUSE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "onNext:" + s);
                    }
                });

        //        结果：
        //          未加compose
        //        01-25 16:22:41.000 28270-28270/com.lz.rx_rxlifecycle E/MainActivity: onNext:1000000000
        //        加了compose，并且执行了onPuase方法之后(按home键)，无打印

    }

    /**
     * 1.与Activity的生命周期进行绑定
     */
    private void composeBindToLifecycle() {
        mSubscribe1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                int i = 0;
                while (i < 1000000000) {
                    i++;
                }
                subscriber.onNext("Scheduler:" + i);
                subscriber.onCompleted();
            }
        })
                //        加了之后，当前组件生命周期结束时，自动取消对Observable订阅。
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "onNext:" + s);
                    }
                });
        //        结果:
        //        未加compose
        //        01-25 08:02:05.793 4623-4623/com.lz.rx_rxlifecycle E/MainActivity: mSubscribe是否已经解绑了:false
        //        01-25 08:02:06.722 4623-4623/com.lz.rx_rxlifecycle E/MainActivity: onNext:Scheduler:1000000000
        //        加了compose   虽然还是打印了false，但并不会onNext ,打印1000000000,说明Observable的订阅已经被取消了
        //        01-25 08:04:09.063 4834-4834/com.lz.rx_rxlifecycle E/MainActivity: mSubscribe是否已经解绑了:false

    }

    public void onclick(View view) {
        startActivity(new Intent(this, Main2Activity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mSubscribe1) {
            Log.e(TAG, "mSubscribe是否已经解绑了:" + mSubscribe1.isUnsubscribed());
        }
        if (null != mSubscribe2) {
            Log.e(TAG, "mSubscribe2是否已经解绑了:" + mSubscribe2.isUnsubscribed());
        }
    }
}
