package com.lz.rx_rxbinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Button的示例
 */
public class ButtonActivity extends AppCompatActivity {
    private static final String TAG = ButtonActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);


        /**
         * 1.设置点击事件
         */
        RxView.clicks(findViewById(R.id.bt)).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.e(TAG, "被点击了");
                Toast.makeText(ButtonActivity.this, "被点击了", Toast.LENGTH_SHORT).show();
            }
        });


        /**
         *  2.三秒之内只取一个点击事件
         */
        RxView.clicks(findViewById(R.id.bt_seconds))
                .throttleFirst(3, TimeUnit.SECONDS)//三秒之内只取一个点击事件
                .subscribe(new Action1<Void>() {
                               @Override
                               public void call(Void aVoid) {
                                   Log.e(TAG, "3秒之内只响应一次哦！！！");
                                   Toast.makeText(ButtonActivity.this, "3秒之内只响应一次哦！！！", Toast.LENGTH_SHORT).show();
                               }
                           }

                );
        //结果：  秒数
        //        02-01 09:24:  31.668   9361-9361/com.lz.rx_rxbinding E/MainActivity: 3秒之内只响应一次哦！！！
        //        02-01 09:24:  34.798   9361-9361/com.lz.rx_rxbinding E/MainActivity: 3秒之内只响应一次哦！！！
        //        02-01 09:24:  37.878   9361-9361/com.lz.rx_rxbinding E/MainActivity: 3秒之内只响应一次哦！！！
        //        02-01 09:24:  41.048   9361-9361/com.lz.rx_rxbinding E/MainActivity: 3秒之内只响应一次哦！！！


        /**
         * 3.设置长按事件
         */
        RxView.longClicks(findViewById(R.id.bt_long))
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(ButtonActivity.this, "长按了。。。", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "长按了。。。");
                    }
                });
        //        结果：
        //        02-01 09:30:49.248 14217-14217/com.lz.rx_rxbinding E/MainActivity: 长按了。。。


    }
}
