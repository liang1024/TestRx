package com.lz.rx_rxbinding;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;

import rx.functions.Action1;

/**
 * CheckBox的示例
 */
public class CheckboxActivity extends AppCompatActivity {

    private CheckBox mCheckbox;
    private Button mBt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox);
        mCheckbox = (CheckBox) findViewById(R.id.checkbox);
        mBt_login = (Button) findViewById(R.id.bt_login);

        RxCompoundButton.checkedChanges(mCheckbox)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {

                        mBt_login.setEnabled(aBoolean);
                        mBt_login.setBackgroundColor(aBoolean ? Color.BLUE : Color.GRAY);
                        mBt_login.setText(aBoolean ? "点我进行登陆(可以进行登录)" : "点我进行登陆(不可以进行登录)");

                    }
                });


        RxView.clicks(mBt_login)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(CheckboxActivity.this, "进行了登录操作~", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
