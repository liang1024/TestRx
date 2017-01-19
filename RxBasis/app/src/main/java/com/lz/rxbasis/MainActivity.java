package com.lz.rxbasis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 创建观察者2种方式
         */
        // 1.Observer
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };

        // 2.Subscriber        Subscriber继承ObServer,对ObServer类做了扩展
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }

            /**
             * onStart(): 这是 Subscriber 增加的方法。它会在 subscribe 刚开始，而事件还未发送之前被调用，可以用于做一些准备工作，
             * 例如数据的清零或重置。这是一个可选方法，默认情况下它的实现为空。需要注意的是，如果对准备工作的线程有要求（例如弹出一个显示进度的对话框，这必须在主线程执行），
             * onStart() 就不适用了，因为它总是在 subscribe 所发生的线程被调用，而不能指定线程。要在指定的线程来做准备工作，
             * 可以使用 doOnSubscribe() 方法
             */
            @Override
            public void onStart() {
                super.onStart();
            }
        };


        /**
         * 创建被观察者
         */
        //   1.create方法
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {

            /**
             * 当 Observable 被订阅的时候，OnSubscribe 的 call() 方法会自动被调用，事件序列就会依照设定依次触发
             * @param subscriber
             */
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("aa");
                subscriber.onNext("bb");
                subscriber.onNext("cc");
                subscriber.onCompleted();

            }
        });

        // 2.just       just方法，最多支持10个数据
        Observable<String> just = Observable.just("aa", "bb", "cc");
        //      just方法结果：
        //        将会依次调用:
        //        onNext("aa");
        //        onNext("bb");
        //        onNext("cc");
        //        onCompleted();

        //3.from方式

        //  3-1.集合
        ArrayList<Object> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        Observable<Object> from1 = Observable.from(list);

        //   3-2.数组
        String[] words = {"aa", "bb", "cc"};
        Observable<String> from2 = Observable.from(words);


        /**
         *  订阅
         */
        observable.subscribe(observer);
        observable.subscribe(subscriber);


    }
}
