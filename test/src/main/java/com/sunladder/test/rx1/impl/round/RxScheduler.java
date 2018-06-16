package com.sunladder.test.rx1.impl.round;

import com.sunladder.test.rx1.RxRound;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxScheduler implements RxRound {

    @Override
    public void run() {
        roundThree_scheduler();
    }

    // TODO: 2018/6/11  如何进行线程调度
    private void roundThree_scheduler() {
        Observable.just(0)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return ++integer;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Integer, Observable<?>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        return Observable.just(++integer)
                                .subscribeOn(Schedulers.newThread());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                    }
                });
    }
}
