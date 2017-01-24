package com.lz.rx_basis2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText mEdit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdit_text = (EditText) findViewById(R.id.edit_text);

        //TODO 1.Merge:操作符，合并观察对象
        merge();
        //TODO 2.zip:操作符,合并多个观察对象的数据，并且允许Func2()函数重新发送合并后的数据
        zip();
        //TODO 3.scan:累加器操作符的使用
        scan();
        //TODO 4.filter:过滤操作符的使用
        filter();
        //TODO 5.消息数量过滤操作符的使用
        // 1.take:取前n个数据  2.takeLast:取后n个数据  3.frist：只发送第一个数据  4.skip()跳过前n个数据发送后面的数据  5.skipLast()跳过最后n个数据
        messageOperation();
        //TODO 6.elementAt.elementAtOrDefault 查询元素
        elementAt();
        //TODO 7.startWith()插入数据
        startWith();
        //TODO 8.delay操作符，延时数据发送
        delay();
        //TODO 9.Timer延时操作符的使用
        Timer();
        //         delay 、timer 总结：　
        //        相同点：delay 、 timer 都是延时操作符。
        //        不同点：delay  延时一次，延时完成后，可以连续发射多个数据。timer延时一次，延时完成后，只发射一次数据。

        //TODO 11.doOnNext()操作符
        doOnNext();
        //TODO 12.Buffer操作符
        Buffer();
        //TODO 13.throttleFirst 操作符
        throttleFirst();
        //TODO 14.distinct 过滤重复的数据
        distinct();
        //TODO 15.distinctUntilChanged() 过滤连续重复的数据
        distinctUntilChanged();
        //TODO 16.debounce() 操作符
        debounce();
        //TODO 17.doOnSubscribe() 使用场景： 可以在事件发出之前做一些初始化的工作，比如弹出进度条等等
        doOnsubscribe();
        //TODO 18.range(int start, int count)   输出范围内数据
        range();
        //TODO 19.defer() defer是在订阅者订阅时才创建Observable，此时才进行真正的赋值操作。
        defer();
    }

    private String i = "1";

    /**
     * 19.defer :
     * 可以看到，just操作符是在创建Observable就进行了赋值操作，而defer是在订阅者订阅时才创建Observable，此时才进行真正的赋值操作。
     */
    private void defer() {
        i = "2";

        /**
         * 创建 defer
         */
        Observable<String> defer = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just(i);
            }
        });
        /**
         * 创建just
         */
        Observable<String> just = Observable.just(i);


        i = "3";
        /**
         * 打印 just
         */
        just.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, "defer-- just:" + s);
            }
        });
        /**
         *  打印defer
         */
        defer.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, "defer-- defer:" + s);
            }
        });

        //        结果:
        //        01-24 10:27:21.933 7217-7217/com.lz.rx_basis2 E/MainActivity: defer-- just:2
        //        01-24 10:27:21.936 7217-7217/com.lz.rx_basis2 E/MainActivity: defer-- defer:3

    }

    /**
     * 18.range()    如果将第二个参数设为0，将导致Observable不发射任何数据（如果设置为负数，会抛异常）
     */
    private void range() {
        /**
         * 参数1:起始值,参数2:范围的数据的数目    (可选)参数3:执行的线程
         */
        Observable.range(10, 9/*,AndroidSchedulers.mainThread()*/).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG, "range-- " + integer);
            }
        });
        //        结果：
        //        01-24 10:11:33.948 6928-6928/com.lz.rx_basis2 E/MainActivity: range-- 10
        //        01-24 10:11:33.948 6928-6928/com.lz.rx_basis2 E/MainActivity: range-- 11
        //        01-24 10:11:33.948 6928-6928/com.lz.rx_basis2 E/MainActivity: range-- 12
        //        01-24 10:11:33.948 6928-6928/com.lz.rx_basis2 E/MainActivity: range-- 13
        //        01-24 10:11:33.948 6928-6928/com.lz.rx_basis2 E/MainActivity: range-- 14
        //        01-24 10:11:33.948 6928-6928/com.lz.rx_basis2 E/MainActivity: range-- 15
        //        01-24 10:11:33.948 6928-6928/com.lz.rx_basis2 E/MainActivity: range-- 16
        //        01-24 10:11:33.949 6928-6928/com.lz.rx_basis2 E/MainActivity: range-- 17
        //        01-24 10:11:33.949 6928-6928/com.lz.rx_basis2 E/MainActivity: range-- 18

    }

    /**
     * 17.doOnSubscribe()
     * 使用场景： 可以在事件发出之前做一些初始化的工作，比如弹出进度条等等
     */
    private void doOnsubscribe() {
        /**
         *    1.doOnSubscribe() 默认运行在事件产生的线程里面，然而事件产生的线程一般都会运行在 io 线程里。那么这个时候做一些，更新UI的操作，是线程不安全的。
         *    所以如果事件产生的线程是io线程，但是我们又要在doOnSubscribe() 更新UI ， 这时候就需要线程切换。
         *    2.如果在 doOnSubscribe() 之后有 subscribeOn() 的话，它将执行在离它最近的 subscribeOn() 所指定的线程。
         *    3.subscribeOn() 事件产生的线程 ； observeOn() : 事件消费的线程
         */
        /**
         * 同一条线程时,会先执行被订阅者再执行订阅，不同线程时，会被订阅按照顺序执行
         */

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e(TAG, "doOnSubscribe-- 创建了事件1");
                Log.e(TAG, "doOnSubscribe-- 创建了事件2");
                subscriber.onNext("doOnsubscribe-- 执行了onNext");
                Log.e(TAG, "doOnSubscribe-- 创建了事件3");

            }
        }).subscribeOn(AndroidSchedulers.mainThread())//事件订阅的线程
                .observeOn(AndroidSchedulers.mainThread())//事件消费的线程
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "doOnSubscribe-- onNext:" + s);
                    }
                });
        //结果
        //        01-24 09:59:16.736 6097-6097/com.lz.rx_basis2 E/MainActivity: doOnSubscribe-- 创建了事件
        //        01-24 09:59:16.911 6097-6097/com.lz.rx_basis2 E/MainActivity: doOnSubscribe-- onNext:doOnsubscribe-- 执行了onNext
    }

    /**
     * 16.debounce(); 操作符
     */
    private void debounce() {
       /* final ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        mEdit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e(TAG, "debounce-- 文本改变之前");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "debounce-- 文本改变中");

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "debounce-- 文本改变之后");
                Observable.from(list).debounce(5000, TimeUnit.SECONDS).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG, "debounce-- 发送了:" + s);
                    }
                });
            }
        });*/

    }

    /**
     * 15.distinctUntilChanged  过滤掉连续重复的数据
     */
    private void distinctUntilChanged() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("2");
        list.add("2");
        list.add("2");
        list.add("1");
        list.add("1");
        list.add("3");
        list.add("3");
        list.add("4");
        list.add("4");
        list.add("2");
        list.add("2");
        list.add("1");
        list.add("1");
        Observable.from(list)
                .distinctUntilChanged()
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG, "distinctUntilChanged--" + s);
                    }
                });
        // 结果：
        //        01-24 08:00:46.977 3969-3969/com.lz.rx_basis2 E/MainActivity: distinctUntilChanged--1
        //        01-24 08:00:46.977 3969-3969/com.lz.rx_basis2 E/MainActivity: distinctUntilChanged--2
        //        01-24 08:00:46.977 3969-3969/com.lz.rx_basis2 E/MainActivity: distinctUntilChanged--1
        //        01-24 08:00:46.977 3969-3969/com.lz.rx_basis2 E/MainActivity: distinctUntilChanged--3
        //        01-24 08:00:46.977 3969-3969/com.lz.rx_basis2 E/MainActivity: distinctUntilChanged--4
        //        01-24 08:00:46.977 3969-3969/com.lz.rx_basis2 E/MainActivity: distinctUntilChanged--2
        //        01-24 08:00:46.977 3969-3969/com.lz.rx_basis2 E/MainActivity: distinctUntilChanged--1


    }

    /**
     * 14.distinct 过滤重复的数据
     */
    private void distinct() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("1");
        list.add("3");
        list.add("4");
        list.add("2");
        list.add("1");
        list.add("1");
        Observable
                .from(list)
                .distinct() //过滤重复的数据
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG, "distinct--" + s);
                    }
                });
        //        结果:
        //        01-24 07:45:37.286 3633-3633/com.lz.rx_basis2 E/MainActivity: distinct--1
        //        01-24 07:45:37.286 3633-3633/com.lz.rx_basis2 E/MainActivity: distinct--2
        //        01-24 07:45:37.286 3633-3633/com.lz.rx_basis2 E/MainActivity: distinct--3
        //        01-24 07:45:37.286 3633-3633/com.lz.rx_basis2 E/MainActivity: distinct--4

    }

    /**
     * 13.throttleFirst 操作符
     * 在一段时间内，只取第一个事件，然后其他事件都丢弃。
     * 使用场景：1、button按钮防抖操作，防连续点击   2、百度关键词联想，在一段时间内只联想一次，防止频繁请求服务器
     */
    private void throttleFirst() {
        /**
         * interval(1, TimeUnit.SECONDS)         每秒打印一次
         * throttleFirst(2, TimeUnit.SECONDS)   截取间隔2秒的值
         */
        Observable.interval(1, TimeUnit.SECONDS)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e(TAG, "throttleFirst --" + aLong);
                    }
                });
        //       结果：
        //        01-24 07:34:46.208 3457-3476/com.lz.rx_basis2 E/MainActivity: throttleFirst --0
        //        01-24 07:34:49.207 3457-3476/com.lz.rx_basis2 E/MainActivity: throttleFirst --3
        //        01-24 07:34:51.207 3457-3476/com.lz.rx_basis2 E/MainActivity: throttleFirst --5
        //        01-24 07:34:53.207 3457-3476/com.lz.rx_basis2 E/MainActivity: throttleFirst --7
        //        01-24 07:34:55.207 3457-3476/com.lz.rx_basis2 E/MainActivity: throttleFirst-- 9
        //        01-24 07:34:58.208 3457-3476/com.lz.rx_basis2 E/MainActivity: throttleFirst --12

    }

    /**
     * 12.Buffer操作符
     * 使用场景：一个按钮每点击3次，弹出一个toast
     */
    private void Buffer() {
        //        Buffer( int n )  把n个数据打成一个list包，然后再次发送。
        //        Buffer( int n , int skip)   把n个数据打成一个list包，然后跳过第skip个数据。

        // 使用场景：一个按钮每点击3次,弹出一个Toast

        //把每两个数据为一组打成一个包，然后发送
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }
        Observable<String> observable = Observable.from(list);
        observable
                .buffer(2)
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        Log.e(TAG, "Buffer-------------------------");
                        Observable.from(strings).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Log.e(TAG, "Buffer data--" + s);
                            }
                        });
                    }
                });
        //        结果：
        //        01-21 04:30:05.802 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer-------------------------
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer data--0
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer data--1
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer-------------------------
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer data--2
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer data--3
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer-------------------------
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer data--4
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer data--5
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer-------------------------
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer data--6
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer data--7
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer-------------------------
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer data--8
        //        01-21 04:30:05.803 3554-3554/com.lz.rx_basis2 E/MainActivity: Buffer data--9

        //        跳过第三个下标的参数：2
        observable
                .buffer(2, 3)
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        Log.e(TAG, "Buffer2-------------------------");
                        Observable.from(strings).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Log.e(TAG, "Buffer2 data--" + s);
                            }
                        });
                    }
                });
        //结果:
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2-------------------------
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2 data--0
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2 data--1
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2-------------------------
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2 data--3
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2 data--4
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2-------------------------
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2 data--6
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2 data--7
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2-------------------------
        //        01-21 04:32:54.061 3656-3656/com.lz.rx_basis2 E/MainActivity: Buffer2 data--9
    }

    /**
     * 11.doOnNext()操作符,在每次OnNext()方法被调用前执行
     * 使用场景：从网络请求数据，在数据被展示前，缓存到本地
     */
    private void doOnNext() {
        //使用场景：从网络请求数据，在数据被展示前，缓存到本地
        Observable<String> observable = Observable.just("1", "2", "3", "4");
        observable.doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, "doOnNext--缓存数据：" + s);
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, " onNext--：" + s);
            }
        });
        // 结果
        //        01-21 04:17:41.359 3330-3330/com.lz.rx_basis2 E/MainActivity: doOnNext--缓存数据：1
        //        01-21 04:17:41.359 3330-3330/com.lz.rx_basis2 E/MainActivity:  onNext--：1
        //        01-21 04:17:41.360 3330-3330/com.lz.rx_basis2 E/MainActivity: doOnNext--缓存数据：2
        //        01-21 04:17:41.360 3330-3330/com.lz.rx_basis2 E/MainActivity:  onNext--：2
        //        01-21 04:17:41.360 3330-3330/com.lz.rx_basis2 E/MainActivity: doOnNext--缓存数据：3
        //        01-21 04:17:41.360 3330-3330/com.lz.rx_basis2 E/MainActivity:  onNext--：3
        //        01-21 04:17:41.360 3330-3330/com.lz.rx_basis2 E/MainActivity: doOnNext--缓存数据：4
        //        01-21 04:17:41.360 3330-3330/com.lz.rx_basis2 E/MainActivity:  onNext--：4


    }

    /**
     * TODO 10. interval轮询操作符，循环发送数据，数据从0开始递增
     */
    public void interval(View view) {
        startActivity(new Intent(this, IntervalActivity.class));
        //        startActivity(new Intent(this, DoOnNextActivity.class));

    }

    /**
     * 9.Timer延时操作符的使用
     */
    private void Timer() {
        //        延时5秒后，显示一个ImageView
        //        .observeOn(AndroidSchedulers.mainThread())   切换至主线程
        Observable.timer(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.e(TAG, " 显示了 timer--" + aLong);
                findViewById(R.id.img_show).setVisibility(View.VISIBLE);
            }
        });
        //结果：
        // 01-21 03:31:09.613 4369-4369/com.lz.rx_basis2 E/MainActivity:  显示了 timer--0
    }

    /**
     * 8.delay操作符，延时数据发送
     */
    private void delay() {
        Observable<String> observable = Observable.just("1", "2", "3", "4", "5", "6", "7", "8");

        //延时3秒  可选时间单位
        observable.delay(3, TimeUnit.SECONDS)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG, " delay--" + s);
                    }
                });
        //        结果：
        //        01-21 03:09:38.377 4189-4212/com.lz.rx_basis2 E/MainActivity:  delay--1
        //        01-21 03:09:38.377 4189-4212/com.lz.rx_basis2 E/MainActivity:  delay--2
        //        01-21 03:09:38.377 4189-4212/com.lz.rx_basis2 E/MainActivity:  delay--3
        //        01-21 03:09:38.377 4189-4212/com.lz.rx_basis2 E/MainActivity:  delay--4
        //        01-21 03:09:38.377 4189-4212/com.lz.rx_basis2 E/MainActivity:  delay--5
        //        01-21 03:09:38.377 4189-4212/com.lz.rx_basis2 E/MainActivity:  delay--6
        //        01-21 03:09:38.378 4189-4212/com.lz.rx_basis2 E/MainActivity:  delay--7
        //        01-21 03:09:38.378 4189-4212/com.lz.rx_basis2 E/MainActivity:  delay--8
    }

    /**
     * 7.startWith()插入数据
     */
    private void startWith() {
        //      插入普通数据
        //      startWith 数据序列的开头插入一条指定的项，最多插入9条数据
        Observable<String> observable = Observable.just("aa", "bb", "cc");
        observable.startWith("11", "22").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, " startWith-data-- " + s);
            }
        });
        //结果：
        //        01-21 03:05:27.986 3989-3989/com.lz.rx_basis2 E/MainActivity:  startWith-data-- 11
        //        01-21 03:05:27.986 3989-3989/com.lz.rx_basis2 E/MainActivity:  startWith-data-- 22
        //        01-21 03:05:27.986 3989-3989/com.lz.rx_basis2 E/MainActivity:  startWith-data-- aa
        //        01-21 03:05:27.986 3989-3989/com.lz.rx_basis2 E/MainActivity:  startWith-data-- bb
        //        01-21 03:05:27.986 3989-3989/com.lz.rx_basis2 E/MainActivity:  startWith-data-- cc
        //  插入Observable对象
        ArrayList<String> list = new ArrayList<>();
        list.add("ww");
        list.add("tt");
        observable.startWith(Observable.from(list)).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, " startWith-observable--" + s);
            }
        });
        //结果：
        //        01-21 03:05:27.987 3989-3989/com.lz.rx_basis2 E/MainActivity:  startWith-observable--ww
        //        01-21 03:05:27.987 3989-3989/com.lz.rx_basis2 E/MainActivity:  startWith-observable--tt
        //        01-21 03:05:27.987 3989-3989/com.lz.rx_basis2 E/MainActivity:  startWith-observable--aa
        //        01-21 03:05:27.987 3989-3989/com.lz.rx_basis2 E/MainActivity:  startWith-observable--bb
        //        01-21 03:05:27.987 3989-3989/com.lz.rx_basis2 E/MainActivity:  startWith-observable--cc

    }

    /**
     * 查询元素
     * 6.elementAt.elementAtOrDefault
     */
    private void elementAt() {
        /**
         * elementAt()：发送数据序列中第n个数据，序列号从0开始
         * 如果该序列号大于数据序列中的最大序列号，则会抛出异常，程序崩溃
         */
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7);
        observable.elementAt(3).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

                Log.e(TAG, " elementAt--" + integer);

            }
        });
        //  结果：
        //        01-21 02:42:39.070 3699-3699/com.lz.rx_basis2 E/MainActivity:  elementAt--4

        /**
         * elementAtOrDefault(int index, T defaultValue) ,发送数据序列中第N个数据，序列号从0开始
         * 如果序列号中没有该序列号，则发送默认值
         */
        observable.elementAtOrDefault(8, 666).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG, " elementAtOrDefault--" + integer);
            }
        });
        // 结果：
        //        01-21 02:42:39.070 3699-3699/com.lz.rx_basis2 E/MainActivity:  elementAtOrDefault--666

    }

    /**
     * 5.消息数量过滤操作符的使用
     * 1.take:取前n个数据  2.takeLast:取后n个数据  3.frist：只发送第一个数据  4.skip()跳过前n个数据发送后面的数据  5.skipLast()跳过最后n个数据
     */
    private void messageOperation() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //take 发送前面3个数据
        observable.take(3).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG, " take-- " + integer);
            }
        });
        // 结果：
        //        01-21 02:16:07.353 3349-3349/? E/MainActivity:  take-- 1
        //        01-21 02:16:07.353 3349-3349/? E/MainActivity:  take-- 2
        //        01-21 02:16:07.353 3349-3349/? E/MainActivity:  take-- 3

        // takeLast 发送最后3个数据
        observable.takeLast(3).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

                Log.e(TAG, " takeLast-- " + integer);
            }
        });
        // 结果：
        //        01-21 02:16:07.354 3349-3349/? E/MainActivity:  takeLast-- 8
        //        01-21 02:16:07.354 3349-3349/? E/MainActivity:  takeLast-- 9
        //        01-21 02:16:07.354 3349-3349/? E/MainActivity:  takeLast-- 10

        //  first()：只发送第一个数据
        observable.first().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

                Log.e(TAG, " first-- " + integer);
            }
        });
        // 结果：
        //        01-21 02:16:07.356 3349-3349/? E/MainActivity:  first-- 1

        //  last():只发送最后一个数据
        observable.last().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

                Log.e(TAG, " last-- " + integer);
            }
        });
        // 结果：
        //        01-21 02:16:07.359 3349-3349/? E/MainActivity:  last-- 10

        // skip(2):跳过最前面2个数据发送后面的数据
        observable.skip(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

                Log.e(TAG, " skip-- " + integer);
            }
        });
        // 结果：
        //        01-21 02:16:07.360 3349-3349/? E/MainActivity:  skip-- 3
        //        01-21 02:16:07.360 3349-3349/? E/MainActivity:  skip-- 4
        //        01-21 02:16:07.360 3349-3349/? E/MainActivity:  skip-- 5
        //        01-21 02:16:07.360 3349-3349/? E/MainActivity:  skip-- 6
        //        01-21 02:16:07.360 3349-3349/? E/MainActivity:  skip-- 7
        //        01-21 02:16:07.360 3349-3349/? E/MainActivity:  skip-- 8
        //        01-21 02:16:07.360 3349-3349/? E/MainActivity:  skip-- 9
        //        01-21 02:16:07.360 3349-3349/? E/MainActivity:  skip-- 10


        //skipLast() 跳过最后两个数据，发送前面的数据
        observable.skipLast(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

                Log.e(TAG, " skipLast（）-- " + integer);
            }
        });
        //        结果：
        //        01-21 02:16:07.363 3349-3349/? E/MainActivity:  skipLast（）-- 1
        //        01-21 02:16:07.363 3349-3349/? E/MainActivity:  skipLast（）-- 2
        //        01-21 02:16:07.363 3349-3349/? E/MainActivity:  skipLast（）-- 3
        //        01-21 02:16:07.363 3349-3349/? E/MainActivity:  skipLast（）-- 4
        //        01-21 02:16:07.363 3349-3349/? E/MainActivity:  skipLast（）-- 5
        //        01-21 02:16:07.363 3349-3349/? E/MainActivity:  skipLast（）-- 6
        //        01-21 02:16:07.363 3349-3349/? E/MainActivity:  skipLast（）-- 7
        //        01-21 02:16:07.363 3349-3349/? E/MainActivity:  skipLast（）-- 8


    }

    /**
     * 4.filter:过滤操作符的使用
     */
    private void filter() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        observable.filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                // 只有被2整除的数才会被发送
                return integer % 2 == 0;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG, " filter: " + integer);
            }
        });

        //结果:
        //        01-20 09:58:13.758 7629-7629/? E/MainActivity:  filter: 2
        //        01-20 09:58:13.759 7629-7629/? E/MainActivity:  filter: 4
        //        01-20 09:58:13.759 7629-7629/? E/MainActivity:  filter: 6
        //        01-20 09:58:13.759 7629-7629/? E/MainActivity:  filter: 8
        //        01-20 09:58:13.759 7629-7629/? E/MainActivity:  filter: 10


    }


    /**
     * 3.scan:累加器操作符的使用
     */

    private void scan() {

        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        observable.scan(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {

                return integer + integer2;
            }

        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

                Log.e(TAG, " scan:  " + integer);

            }
        });

        //结果：
        //        01-20 09:53:30.256 7552-7552/? E/MainActivity:  scan:  1
        //        01-20 09:53:30.256 7552-7552/? E/MainActivity:  scan:  3
        //        01-20 09:53:30.256 7552-7552/? E/MainActivity:  scan:  6
        //        01-20 09:53:30.256 7552-7552/? E/MainActivity:  scan:  10
        //        01-20 09:53:30.256 7552-7552/? E/MainActivity:  scan:  15
        //        01-20 09:53:30.256 7552-7552/? E/MainActivity:  scan:  21
        //        01-20 09:53:30.256 7552-7552/? E/MainActivity:  scan:  28
        //        01-20 09:53:30.256 7552-7552/? E/MainActivity:  scan:  36
        //        01-20 09:53:30.256 7552-7552/? E/MainActivity:  scan:  45
        //        01-20 09:53:30.256 7552-7552/? E/MainActivity:  scan:  55

    }


    /**
     * 2.zip操作符,合并多个观察对象的数据，并且允许Func2()函数重新发送合并后的数据
     */
    private void zip() {

        ArrayList<Object> zipList1 = new ArrayList<>();
        zipList1.add("我是zip1-1");
        zipList1.add("我是zip1-2");
        zipList1.add("我是zip1-3");

        ArrayList<Object> zipList2 = new ArrayList<>();
        zipList2.add("我是zip2-1");
        zipList2.add("我是zip2-2");
        zipList2.add("我是zip2-3");

        Observable<Object> observable1 = Observable.from(zipList1);
        Observable<Object> observable2 = Observable.from(zipList2);

        Observable<String> observable3 = Observable.zip(observable1, observable2, new Func2<Object, Object, String>() {
            @Override
            public String call(Object o, Object o2) {

                return o.toString() + "--" + o2.toString();
            }
        });
        observable3.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                Log.e(TAG, " zip:   " + s);

            }
        });
        //结果：
        //        01-20 09:45:51.564 7420-7420/? E/MainActivity:  zip:   我是zip1-1--我是zip2-1
        //        01-20 09:45:51.564 7420-7420/? E/MainActivity:  zip:   我是zip1-2--我是zip2-2
        //        01-20 09:45:51.564 7420-7420/? E/MainActivity:  zip:   我是zip1-3--我是zip2-3
    }


    /**
     * 1.Merge操作符，合并观察对象
     */
    private void merge() {

        ArrayList<String> List1 = new ArrayList<>();

        List1.add("我是merge1-1");
        List1.add("我是merge1-2");
        List1.add("我是merge1-3");

        ArrayList<String> List2 = new ArrayList<>();

        List2.add("我是merge2-1");
        List2.add("我是merge2-2");
        List2.add("我是merge2-3");

        Observable<String> from1 = Observable.from(List1);
        Observable<String> from2 = Observable.from(List2);

        //  合并数据,先发送from2的全部数据,然后发送from1的全部数据
        Observable<String> observable = Observable.merge(from2, from1);

        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, " merge:  " + s);
            }
        });
        //结果：
        //        01-20 09:47:52.244 7420-7420/com.lz.rx_basis2 E/MainActivity:  merge:  我是merge2-1
        //        01-20 09:47:52.244 7420-7420/com.lz.rx_basis2 E/MainActivity:  merge:  我是merge2-2
        //        01-20 09:47:52.244 7420-7420/com.lz.rx_basis2 E/MainActivity:  merge:  我是merge2-3
        //        01-20 09:47:52.244 7420-7420/com.lz.rx_basis2 E/MainActivity:  merge:  我是merge1-1
        //        01-20 09:47:52.245 7420-7420/com.lz.rx_basis2 E/MainActivity:  merge:  我是merge1-2
        //        01-20 09:47:52.245 7420-7420/com.lz.rx_basis2 E/MainActivity:  merge:  我是merge1-3
    }
}
