package com.lz.rxbasis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 创建观察者2种方式
         */
        // 1.Observer
      /*  Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "observer  onNext:" + s);
            }
        };*/

        // 2.Subscriber        Subscriber继承ObServer,对ObServer类做了扩展
       /* Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "subscriber  onNext:" + s);
            }

            *//**
         * onStart(): 这是 Subscriber 增加的方法。它会在 subscribe 刚开始，而事件还未发送之前被调用，可以用于做一些准备工作，
         * 例如数据的清零或重置。这是一个可选方法，默认情况下它的实现为空。需要注意的是，如果对准备工作的线程有要求（例如弹出一个显示进度的对话框，这必须在主线程执行），
         * onStart() 就不适用了，因为它总是在 subscribe 所发生的线程被调用，而不能指定线程。要在指定的线程来做准备工作，
         * 可以使用 doOnSubscribe() 方法
         *//*
            @Override
            public void onStart() {
                super.onStart();
            }
        };*/


        /**
         * 创建被观察者
         */
        //   1.create方法
    /*    Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {

            *//**
         * 当 Observable 被订阅的时候，OnSubscribe 的 call() 方法会自动被调用，事件序列就会依照设定依次触发
         * @param subscriber
         *//*
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("create1");
                subscriber.onNext("create2");
                subscriber.onNext("create3");
                subscriber.onCompleted();

            }
        });*/

        // 2.just       just方法，最多支持10个数据
        //        Observable<String> just = Observable.just("just1", "just2", "just3");

        //3.from方式

        //  3-1.集合
     /*   ArrayList<String> list = new ArrayList<>();
        list.add("fromList1");
        list.add("fromList2");
        list.add("fromList3");
        Observable<String> fromList = Observable.from(list);*/

        //           3-2.数组
        //        String[] words = {"array1", "array2", "array3"};
        //        Observable<String> fromArray = Observable.from(words);


        /**
         *  订阅
         */
        //        1.create方法
        //        observable.subscribe(observer);
        //        observable.subscribe(subscriber);
        //        结果：
        //        01-20 03:00:35.572 32193-32193/com.lz.rxbasis E/MainActivity: observer  onNext:create1
        //        01-20 03:00:35.572 32193-32193/com.lz.rxbasis E/MainActivity: observer  onNext:create2
        //        01-20 03:00:35.572 32193-32193/com.lz.rxbasis E/MainActivity: observer  onNext:create3
        //        01-20 03:00:35.572 32193-32193/com.lz.rxbasis E/MainActivity: subscriber  onNext:create1
        //        01-20 03:00:35.572 32193-32193/com.lz.rxbasis E/MainActivity: subscriber  onNext:create2
        //        01-20 03:00:35.572 32193-32193/com.lz.rxbasis E/MainActivity: subscriber  onNext:create3

        //       2.jsut方法：
        //        just.subscribe(observer);
        //        just.subscribe(subscriber);
        //        结果：
        //        01-20 03:01:11.633 335-335/com.lz.rxbasis E/MainActivity: observer  onNext:just1
        //        01-20 03:01:11.633 335-335/com.lz.rxbasis E/MainActivity: observer  onNext:just2
        //        01-20 03:01:11.633 335-335/com.lz.rxbasis E/MainActivity: observer  onNext:just3
        //        01-20 03:01:11.633 335-335/com.lz.rxbasis E/MainActivity: subscriber  onNext:just1
        //        01-20 03:01:11.633 335-335/com.lz.rxbasis E/MainActivity: subscriber  onNext:just2
        //        01-20 03:01:11.633 335-335/com.lz.rxbasis E/MainActivity: subscriber  onNext:just3

        //       3. fromList方法
        //        fromList.subscribe(observer);
        //        fromList.subscribe(subscriber);
        //        结果:
        //        01-20 03:02:34.373 1476-1476/com.lz.rxbasis E/MainActivity: observer  onNext:fromList1
        //        01-20 03:02:34.373 1476-1476/com.lz.rxbasis E/MainActivity: observer  onNext:fromList2
        //        01-20 03:02:34.373 1476-1476/com.lz.rxbasis E/MainActivity: observer  onNext:fromList3
        //        01-20 03:02:34.373 1476-1476/com.lz.rxbasis E/MainActivity: subscriber  onNext:fromList1
        //        01-20 03:02:34.373 1476-1476/com.lz.rxbasis E/MainActivity: subscriber  onNext:fromList2
        //        01-20 03:02:34.373 1476-1476/com.lz.rxbasis E/MainActivity: subscriber  onNext:fromList3

        //        fromArray.subscribe(observer);
        //        fromArray.subscribe(subscriber);
        //       结果：
        //        01-20 03:03:59.984 1476-1476/com.lz.rxbasis E/MainActivity: observer  onNext:array1
        //        01-20 03:03:59.984 1476-1476/com.lz.rxbasis E/MainActivity: observer  onNext:array2
        //        01-20 03:03:59.984 1476-1476/com.lz.rxbasis E/MainActivity: observer  onNext:array3
        //        01-20 03:03:59.985 1476-1476/com.lz.rxbasis E/MainActivity: subscriber  onNext:array1
        //        01-20 03:03:59.985 1476-1476/com.lz.rxbasis E/MainActivity: subscriber  onNext:array2
        //        01-20 03:03:59.985 1476-1476/com.lz.rxbasis E/MainActivity: subscriber  onNext:array3


    }
}
