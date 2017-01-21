package com.lz.rx_basis2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * @author
 * @time 2017/1/21
 * @Dec
 */

public class DoOnNextActivity extends AppCompatActivity {
    private static final String TAG = DoOnNextActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_onnext);
        //        检查摄像头的权限是否可用
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //            不可用
            Log.e(TAG, "不可用");
        } else {
            //            可用
            Log.e(TAG, "可用");
        }
    }
}
