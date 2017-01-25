package com.lz.rx_rxlifecycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 继承RxFragment
 */
public class BlankFragment extends RxFragment {

    private static final String TAG = BlankFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Observable.create(new Observable.OnSubscribe<String>() {
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
                //                .compose(this.<String>bindUntilEvent(FragmentEvent.PAUSE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG, "call:" + s);
                    }
                });
        //结果： 添加compse之后，执行onPause方法之后将取消Observable的订阅

        //        未加compose
        //        01-25 16:45:08.930 16690-16690/com.lz.rx_rxlifecycle E/BlankFragment: call:1000000000
        //        back键
        //        01-25 16:45:29.050 16690-16690/com.lz.rx_rxlifecycle E/BlankFragment: Fargment执行了onPause
        //        01-25 16:45:29.420 16690-16690/com.lz.rx_rxlifecycle E/BlankFragment: Fargment执行了onDestroyView
        //        再次进入之后并按home键,
        //        01-25 16:45:32.210 16690-16690/com.lz.rx_rxlifecycle E/BlankFragment: Fargment执行了onPause
        //        01-25 16:45:36.920 16690-16690/com.lz.rx_rxlifecycle E/BlankFragment: call:1000000000

        //         加了compose
        //        01-25 16:42:54.980 14433-14433/com.lz.rx_rxlifecycle E/BlankFragment: call:1000000000
        //        back键
        //        01-25 16:42:57.110 14433-14433/com.lz.rx_rxlifecycle E/BlankFragment: Fargment执行了onPause
        //        01-25 16:42:57.490 14433-14433/com.lz.rx_rxlifecycle E/BlankFragment: Fargment执行了onDestroyView
        //        再次进入之后并按home键
        //        01-25 16:43:02.700 14433-14433/com.lz.rx_rxlifecycle E/BlankFragment: Fargment执行了onPause


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "Fargment执行了onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "Fargment执行了onDestroyView");
    }
}
