package com.lz.rx_rxbinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * debounce()示例
 */
public class DebounceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debounce);

        AppCompatEditText edittext = (AppCompatEditText) findViewById(R.id.edittext);
        ListView listview = (ListView) findViewById(R.id.listviews);

        final ArrayList<String> errorList = new ArrayList<>();
        errorList.add("未找到相关用户");
        final ArrayList<String> defalutList = new ArrayList<>();
        defalutList.add("请输入要查找的关键词");


        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1);
        listview.setAdapter(stringArrayAdapter);


        RxTextView.textChanges(edittext)
                .debounce(600, TimeUnit.MILLISECONDS) //无操作600毫秒之后进行请求
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        //  get The Keyword
                        return charSequence.toString();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String keyWord) {
                        // get List
                        ArrayList<String> datalist = new ArrayList<>();
                        if (!TextUtils.isEmpty(keyWord)) {
                            for (String s : getData()) {
                                if (s != null) {
                                    if (s.contains(keyWord)) {
                                        datalist.add(s);
                                    }
                                }
                            }
                        }
                        return keyWord.length() > 0 ? datalist.size() > 0 ? datalist : errorList : defalutList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        stringArrayAdapter.clear();
                        stringArrayAdapter.addAll(strings);
                        stringArrayAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * 从数据库查询数据
     *
     * @return 包含数据的集合
     */
    public ArrayList<String> getData() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("ab");
        list.add("bc");
        list.add("cd");
        list.add("de");
        return list;
    }
}
