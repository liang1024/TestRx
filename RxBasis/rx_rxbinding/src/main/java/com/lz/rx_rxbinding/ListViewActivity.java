package com.lz.rx_rxbinding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * ListView的示例
 */
public class ListViewActivity extends AppCompatActivity {

    private static final String TAG = ListViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);


        ListView mListview = (ListView) findViewById(R.id.listview);

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            list.add("item" + i);
        }

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1);
        stringArrayAdapter.addAll(list);


        mListview.setAdapter(stringArrayAdapter);


        RxAdapterView.itemClicks(mListview)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Toast.makeText(ListViewActivity.this, "item" + integer + "被点击了。。。", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "item" + integer + "被点击了。。。");
                    }
                });

        RxAdapterView.itemLongClicks(mListview)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Toast.makeText(ListViewActivity.this, "item" + integer + "被长按了。。。", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "item" + integer + "被长按了。。。");
                    }
                });
        //        结果：
        //        02-01 09:44:40.628 27301-27301/com.lz.rx_rxbinding E/MainActivity: item0被点击了。。。
        //        02-01 09:44:42.148 27301-27301/com.lz.rx_rxbinding E/MainActivity: item0被长按了。。。
        //        02-01 09:44:44.058 27301-27301/com.lz.rx_rxbinding E/MainActivity: item1被点击了。。。
        //        02-01 09:44:45.868 27301-27301/com.lz.rx_rxbinding E/MainActivity: item1被长按了。。。
        //        02-01 09:44:50.168 27301-27301/com.lz.rx_rxbinding E/MainActivity: item39被点击了。。。
        //        02-01 09:44:51.888 27301-27301/com.lz.rx_rxbinding E/MainActivity: item39被长按了。。。

    }
}
