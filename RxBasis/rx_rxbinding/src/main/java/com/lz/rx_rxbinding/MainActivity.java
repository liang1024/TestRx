package com.lz.rx_rxbinding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *   1.Button的事件
         */
        findViewById(R.id.bt_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ButtonActivity.class));
            }
        });

        /**
         * 2.ListView的点击事件+长按事件
         */
        RxView.clicks(findViewById(R.id.bt_listview))
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(MainActivity.this, ListViewActivity.class));
                    }
                });

        /**
         *   3.checkbox示例：
         */
        RxView.clicks(findViewById(R.id.bt_checkbox))
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(MainActivity.this, CheckboxActivity.class));
                    }
                });
        /**
         * 4.debounce()示例-关键词联想
         */
        RxView.clicks(findViewById(R.id.bt_debounce))
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(MainActivity.this, DebounceActivity.class));
                    }
                });


    }
}
