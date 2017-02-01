package com.example.rxscheduler;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/2/1.
 */

public class RxUtil {

    private final static Observable.Transformer sTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static <T>Observable.Transformer<T, T> applySchedulers() {
        return sTransformer;
    }
}
