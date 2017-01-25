package com.example.rxscheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Schedulers：线程调度器
 * TODO 1: Schedulers.immediate():直接再当前线程运行，相当于不指定线程，这是默认的Scheduler
 * TODO 2: Schedulers.io():I/O操作(读写文件.读写数据库.网络信息交互等)所使用的Scheduler。行为模式和newThread()差不多，区别在于io()的内部实现是用一个
 * 无数量上限的线程池，可以重用空闲的线程，因此多数情况下io()比newThread()更有效率，不要把计算工作放在io()中，可以避免创建不必要的线程
 * TODO 3: Schedulers.computation():计算所使用的Scheduler.这个计算指的是CPU密集型计算，即不会被I/O等操作限制性能的操作，例如图形的计算，
 * 这个Scheduler使用的固定的线程池，大小为CPU的核数，不要把I/O操作放在computation()中，否则I/O操作的等待时间会浪费CPU。
 * TODO 4: AndroidSchedulers.mainThread():指定操作在Android主线程运行
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mText_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText_show = (TextView) findViewById(R.id.text_show);

        //TODO         1.常见的场景：为了不阻塞UI,在子线程加载数据，在主线程显示数据
        Observable.just("1", "2", "3")
                .subscribeOn(Schedulers.io()) //指定Subscribe() 发生再IO线程
                .observeOn(AndroidSchedulers.mainThread()) //指定Subscriber的回调发生再主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mText_show.setText(s);
                        Log.e(TAG, "Schedulers:" + s);
                    }
                });
        //结果：
        //        01-25 04:45:34.734 4491-4491/com.example.rxscheduler E/PRETTYLOGGER: ║ Schedulers:1
        //        01-25 04:45:34.738 4491-4491/com.example.rxscheduler E/PRETTYLOGGER: ║ Schedulers:2
        //        01-25 04:45:34.740 4491-4491/com.example.rxscheduler E/PRETTYLOGGER: ║ Schedulers:3

        //数据"1"、"2"、"3"将在io线程中发出，在android主线程中接收数据。这种【后台获取数据，前台显示数据】模式适用于大多数的程序策略。

        Func1<Integer, Object> func1 = new Func1<Integer, Object>() {
            @Override
            public Object call(Integer integer) {
                Log.e(TAG, "Scheduler func1线程切换：integer:" + integer);
                return null;
            }
        };

        Func1<Object, Object> func2 = new Func1<Object, Object>() {
            @Override
            public Object call(Object object) {
                return null;
            }
        };


        //TODO          2.Scheduler自由多次切换线程
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())  //IO线程，由SubscribeOn()指定

                .observeOn(Schedulers.newThread())
                .map(func1)                     //新线程，由observeOn()指定

                .observeOn(Schedulers.io())
                .map(func2)                     //IO线程，由observeOn()指定

                .observeOn(AndroidSchedulers.mainThread()) //Android主线程，由observeOn指定
                .subscribe();

        //        observeOn() 可以调用多次来切换线程，observeOn 决定他下面的方法执行时所在的线程
        //        subscribeOn() 用来确定数据发射所在的线程，位置放在哪里都可以，但它是只能调用一次的。

    }
}
