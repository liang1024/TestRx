package com.lz.rx_basis2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    /**
     * 13.throttleFirst 操作符
     */
    private void throttleFirst() {

        Observable.interval(1, TimeUnit.SECONDS)
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e(TAG, "throttleFirst --" + aLong);
                    }
                });

    }

    /**
     * 12.Buffer操作符
     */
    private void Buffer() {
        //        Buffer( int n )      把n个数据打成一个list包，然后再次发送。
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
