package com.lz.rx_basis2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author
 * @time 2017/1/21
 * @Dec 10. interval轮询操作符，循环发送数据，数据从0开始递增
 */

public class IntervalActivity extends AppCompatActivity {

    private static final String TAG = IntervalActivity.class.getSimpleName();

    private Subscription mSubscribe;
    private TextView mText_show;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval);
        mText_show = (TextView) findViewById(R.id.text_show);
        //      参数：1:延时时间，2：间隔时间 3.时间单位
        Observable<Long> observable = Observable.interval(3, 3, TimeUnit.SECONDS);
        //数据从0开始递增
        mSubscribe = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.e(TAG, "  interval--" + aLong);
                mText_show.setText("每隔3秒刷新一次:" + aLong);
            }
        });
        //      结果：
        //        01-21 04:08:50.659 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--0
        //        01-21 04:08:53.658 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--1
        //        01-21 04:08:56.658 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--2
        //        01-21 04:08:59.659 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--3
        //        01-21 04:09:02.658 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--4
        //        01-21 04:09:05.659 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--5
        //        01-21 04:09:08.658 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--6
        //        01-21 04:09:11.658 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--7
        //        01-21 04:09:14.658 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--8
        //        01-21 04:09:17.659 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--9
        //        01-21 04:09:20.658 2353-2453/com.lz.rx_basis2 E/IntervalActivity:   interval--10
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mSubscribe) {
            mSubscribe.unsubscribe();
        }
    }
}
