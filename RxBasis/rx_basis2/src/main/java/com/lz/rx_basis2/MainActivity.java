package com.lz.rx_basis2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  1.Merge:操作符，合并观察对象
        merge();
        //  2.zip:操作符,合并多个观察对象的数据，并且允许Func2()函数重新发送合并后的数据
        zip();
        //  3.scan:累加器操作符的使用
        scan();
        //  4.filter:过滤操作符的使用
        filter();
        //  5.消息数量过滤操作符的使用
        // 1.take:取前n个数据  2.takeLast:取后n个数据  3.frist：只发送第一个数据  4.skip()跳过前n个数据发送后面的数据  5.skipLast()跳过最后n个数据
        messageOperation();

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
                Log.e(TAG, "  ");
            }
        });

        //        TODO 未完成
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
