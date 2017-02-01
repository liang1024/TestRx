package com.example.rxscheduler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Schedulers：线程调度器
 */
public class MainActivity extends AppCompatActivity {
    /**
     * 1: Schedulers.immediate():直接再当前线程运行，相当于不指定线程，这是默认的Scheduler
     * 2: Schedulers.io():I/O操作(读写文件.读写数据库.网络信息交互等)所使用的Scheduler。行为模式和newThread()差不多，区别在于io()的内部实现是用一个
     * 无数量上限的线程池，可以重用空闲的线程，因此多数情况下io()比newThread()更有效率，不要把计算工作放在io()中，可以避免创建不必要的线程
     * 3: Schedulers.computation():计算所使用的Scheduler.这个计算指的是CPU密集型计算，即不会被I/O等操作限制性能的操作，例如图形的计算，
     * 这个Scheduler使用的固定的线程池，大小为CPU的核数，不要把I/O操作放在computation()中，否则I/O操作的等待时间会浪费CPU。
     * 4: AndroidSchedulers.mainThread():指定操作在Android主线程运行
     */
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mText_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText_show = (TextView) findViewById(R.id.text_show);

        //TODO         1.常见的场景：为了不阻塞UI,在子线程加载数据，在主线程显示数据
/*        Observable.just("1", "2", "3")
                .subscribeOn(Schedulers.io()) //指定Subscribe() 发生再IO线程
                .observeOn(AndroidSchedulers.mainThread()) //指定Subscriber的回调发生再主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mText_show.setText(s);
                        Log.e(TAG, "Schedulers:" + s);
                    }
                });*/
        //结果：
        //        01-25 04:45:34.734 4491-4491/com.example.rxscheduler E/PRETTYLOGGER: ║ Schedulers:1
        //        01-25 04:45:34.738 4491-4491/com.example.rxscheduler E/PRETTYLOGGER: ║ Schedulers:2
        //        01-25 04:45:34.740 4491-4491/com.example.rxscheduler E/PRETTYLOGGER: ║ Schedulers:3

        //数据"1"、"2"、"3"将在io线程中发出，在android主线程中接收数据。这种【后台获取数据，前台显示数据】模式适用于大多数的程序策略。
/*

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
*/

        //        例子1
        example1();
        //        例子2
        example2();
        //        通过例1和例2，说明，Rxjava默认运行在当前线程中。如果当前线程是子线程，则rxjava运行在子线程；同样，当前线程是主线程，则rxjava运行在主线程

        //        例子3
        example3();

        //        例子4
        example4();
        /**
         * 通过例3、例4 可以看出  .subscribeOn(Schedulers.io())  和 .observeOn(AndroidSchedulers.mainThread()) 写的位置不一样，造成的结果也不一样。从例4中可以看出 map() 操作符默认运行在事件产生的线程之中。事件消费只是在 subscribe（） 里面。
         */

        /**
         *  create() , just() , from()   等                 --- 事件产生
         *  map() , flapMap() , scan() , filter()  等       --- 事件加工
         *  subscribe()                                     --- 事件消费
         */
        /**
         *   事件产生：默认运行在当前线程，可以由 subscribeOn()  自定义线程
         *   事件加工：默认跟事件产生的线程保持一致, 可以由 observeOn() 自定义线程
         *   事件消费：默认运行在当前线程，可以有observeOn() 自定义
         */
        //        例子5
        example5();
        //        例子6
        example6();
        //        例子7
        example7();
        //        例子8
        example8();
        //        例子9
        example9();

    }

    /**
     * Transformer的使用
     *把一种类型的Observable转换成另一种类型的Observable
     */
    private void example9() {


        final String tag = "example9";
        Observable.range(1, 5)
                .compose(new Observable.Transformer<Integer, Integer>() {
                    @Override
                    public Observable<Integer> call(Observable<Integer> integerObservable) {
                        return integerObservable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(tag, "integer:" + integer + "    currentThread:" + Thread.currentThread().getName());
                    }
                });

        // 结果：
        //        02-01 13:46:15.468 1185-1185/com.example.rxscheduler E/example9: integer:1    currentThread:main
        //        02-01 13:46:15.468 1185-1185/com.example.rxscheduler E/example9: integer:2    currentThread:main
        //        02-01 13:46:15.468 1185-1185/com.example.rxscheduler E/example9: integer:3    currentThread:main
        //        02-01 13:46:15.468 1185-1185/com.example.rxscheduler E/example9: integer:4    currentThread:main
        //        02-01 13:46:15.468 1185-1185/com.example.rxscheduler E/example9: integer:5    currentThread:main

        //      封装成Utils
        Observable.range(1, 5)
                .compose(RxUtil.<Integer>applySchedulers())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(tag, "integer:" + integer + "    currentThread:" + Thread.currentThread().getName());
                    }
                });
        //        结果同上
    }

    /**
     * Io线程产生事件，main线程消费事件
     */
    private void example8() {
        final String tag = "example8";
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Log.e(tag + "_call", "currentThread:" + Thread.currentThread().getName());
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(tag + "_subscribe", "integer:" + integer + "  currentThread:" + Thread.currentThread().getName());
                    }
                });
        //        结果：
        //        02-01 13:31:50.088 18866-18906/com.example.rxscheduler E/example8_call: currentThread:RxIoScheduler-3
        //        02-01 13:31:50.098 18866-18866/com.example.rxscheduler E/example8_subscribe: integer:1  currentThread:main
        //        02-01 13:31:50.108 18866-18866/com.example.rxscheduler E/example8_subscribe: integer:2  currentThread:main
        //        02-01 13:31:50.108 18866-18866/com.example.rxscheduler E/example8_subscribe: integer:3  currentThread:main

    }

    /**
     * 只指定事件消费的线程
     */
    private void example7() {
        final String tag = "example7";
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e(tag + "_call", Thread.currentThread().getName());
                subscriber.onNext("cc");
                subscriber.onCompleted();
            }
        })
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(tag + "_subscribe", Thread.currentThread().getName());
                    }
                });
        //结果：
        //  02-01 13:22:17.658 9090-9090/com.example.rxscheduler E/example7_call: main                           --主线程
        //  02-01 13:22:17.658 9090-9128/com.example.rxscheduler E/example7_subscribe: RxNewThreadScheduler-2    --new新线程
    }

    /**
     * 只指定了事件产生的线程
     */
    private void example6() {
        final String tag = "example6";
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e(tag + "_call", Thread.currentThread().getName());
                subscriber.onNext("cc");
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(tag + "_subscribe", Thread.currentThread().getName());
                    }
                });
        //结果：
        //        02-01 13:17:21.458 4118-4150/com.example.rxscheduler E/example6_call: RxIoScheduler-3        --IO线程
        //        02-01 13:17:21.458 4118-4150/com.example.rxscheduler E/example6_subscribe: RxIoScheduler-3   --IO线程

    }

    /**
     * 多次切换线程
     */
    private void example5() {
        final String tag = "example5";
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e(tag + "_call", Thread.currentThread().getName());
                subscriber.onNext("cc");
                subscriber.onCompleted();
            }
        })
                .observeOn(Schedulers.newThread())  //map的线程

                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.e(tag + "_map", Thread.currentThread().getName());
                        return s + "66";
                    }
                })
                .observeOn(Schedulers.io())    //filter的线程

                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        Log.e(tag + "_filter", Thread.currentThread().getName());
                        return s != null;
                    }
                })
                .subscribeOn(Schedulers.io())    //事件产生的线程
                .observeOn(AndroidSchedulers.mainThread())  //事件订阅的线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(tag + "_subscribe", Thread.currentThread().getName());
                    }
                });
        //结果：
        //  02-01 13:07:29.798 25686-25736/com.example.rxscheduler E/example5_call: RxIoScheduler-2        --IO线程
        //  02-01 13:07:29.798 25686-25738/com.example.rxscheduler E/example5_map: RxNewThreadScheduler-1  --new新线程
        //  02-01 13:07:29.798 25686-25737/com.example.rxscheduler E/example5_filter: RxIoScheduler-3      --IO线程
        //  02-01 13:07:29.808 25686-25686/com.example.rxscheduler E/example5_subscribe: main              --主线程

    }

    /**
     * 子线程产生事件+加工，主线程消费事件
     */
    private void example4() {
        final String tag = "example4";
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e(tag + "_call", Thread.currentThread().getName());
                subscriber.onNext("cc");
                subscriber.onCompleted();
            }
        })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.e(tag + "_map", Thread.currentThread().getName());
                        return s + "66";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(tag + "_subscribe", Thread.currentThread().getName());
                    }
                });
        //        结果：
        //        02-01 12:51:36.848 9344-9401/? E/example4_call: RxIoScheduler-2          --IO线程
        //        02-01 12:51:36.848 9344-9401/? E/example4_map: RxIoScheduler-2           --IO线程
        //        02-01 12:51:36.858 9344-9344/? E/example4_subscribe: main                --主线程
    }

    /**
     * 子线程产生事件，主线程加工以及消费
     */
    private void example3() {
        final String tag = "example3";
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e(tag + "_call", Thread.currentThread().getName());
                subscriber.onNext("cc");
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.e(tag + "_map", Thread.currentThread().getName());
                        return s + "66";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(tag + "_subscribe", Thread.currentThread().getName());
                    }
                });
        //        结果：
        //        02-01 12:48:42.718 6112-6148/com.example.rxscheduler E/example3_call: RxIoScheduler-2         --IO线程
        //        02-01 12:48:42.738 6112-6112/com.example.rxscheduler E/example3_map: main                     --主线程
        //        02-01 12:48:42.738 6112-6112/com.example.rxscheduler E/example3_subscribe: main               --主线程
    }

    /**
     * 子线程运行
     */
    private void example2() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                rx();

            }
        }.start();
        //        结果：  子线程
        //        02-01 12:37:38.998 26618-26645/? E/example2_call: Thread-1038         --子线程
        //        02-01 12:37:38.998 26618-26645/? E/example2_map: Thread-1038          --子线程
        //        02-01 12:37:38.998 26618-26645/? E/example2_subscribe: Thread-1038    --子线程
    }

    private void rx() {
        final String tag = "example2";
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                Log.e(tag + "_call", Thread.currentThread().getName());
                subscriber.onNext("cc");
                subscriber.onCompleted();
            }
        })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.e(tag + "_map", Thread.currentThread().getName());
                        return s + "66";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(tag + "_subscribe", Thread.currentThread().getName());
                    }
                });

    }

    /**
     * 主线程运行
     */
    private void example1() {
        final String tag = "example1";
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                Log.e(tag + "_call", Thread.currentThread().getName());
                subscriber.onNext("cc");
                subscriber.onCompleted();
            }
        })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.e(tag + "_map", Thread.currentThread().getName());
                        return s + "66";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(tag + "_subscribe", Thread.currentThread().getName());
                    }
                });

        //        结果： 主线程
        //        02-01 12:20:15.818 9106-9106/com.example.rxscheduler E/example1_call: main              --主线程
        //        02-01 12:20:15.818 9106-9106/com.example.rxscheduler E/example1_map: main               --主线程
        //        02-01 12:20:15.818 9106-9106/com.example.rxscheduler E/example1_subscribe: main         --主线程

    }
}
