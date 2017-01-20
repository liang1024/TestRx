package com.lz.my_rxbus;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;


/**
 * ====================
 *
 * @author liangzai
 * @time 2017/1/20
 * @dec 简单RxBus
 */
public class RxBus {

    private static final String TAG = RxBus.class.getSimpleName();
    private static RxBus instance;
    public static boolean DEBUG = true;

    public static RxBus get() {
        if (null == instance) {
            synchronized (RxBus.class) {
                if (null == instance) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    private RxBus() {
    }

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }
        Subject<T, T> subject;
        subjectList.add(subject = PublishSubject.create());
        if (DEBUG) {
            Log.e(TAG, "[注册了]subjectMapper:" + subjectMapper);
        }
        return subject;
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null != subjectList) {
            if (null != observable && subjectList.contains(observable)) {
                subjectList.remove(observable);
            }
            if (isEmpty(subjectList)) {
                subjectMapper.remove(tag);
            }
        }
        if (DEBUG) {
            Log.e(TAG, "[注销了]subjectMapper:" + subjectMapper);
        }
    }

    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (!isEmpty(subjectList)) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
        if (DEBUG) {
            Log.e(TAG, "[发送了]subjectMapper:" + subjectMapper);
        }
    }


    private boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

}
