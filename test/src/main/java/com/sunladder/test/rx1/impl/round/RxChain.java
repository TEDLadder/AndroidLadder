package com.sunladder.test.rx1.impl.round;

import com.sunladder.common.log.Logger;

import com.sunladder.test.rx1.RxRound;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxChain implements RxRound {

    @Override
    public void run() {
        roundTwo_callChain();
    }

    // TODO: 2018/6/11  如何形成调用链
    private void roundTwo_callChain() {
        Observable<Integer> observable1 = Observable.just(0);
        Logger.printVar("observable1", observable1);
        Observable<Integer> observable2 = observable1
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        String tag = "step1";
                        Logger.printCurrentThread(tag);
                        Logger.printVar(tag, integer);
                        return ++integer;
                    }
                });
        Logger.printVar("observable2", observable2 == observable1);
        Observable<Integer> observable3 = observable2
                .subscribeOn(Schedulers.newThread());
        Logger.printVar("observable3", observable3 == observable2);
        Observable<Integer> observable4 = observable3
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        String tag = "step2";
                        Logger.printCurrentThread(tag);
                        Logger.printVar(tag, integer);
                        return ++integer;
                    }
                });
        Logger.printVar("observable4", observable4 == observable3);
        Observable<Integer> observable5 = observable4.subscribeOn(Schedulers.newThread());
        Logger.printVar("observable5", observable5 == observable4);
        observable5
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        String tag = "finish";
                        Logger.printCurrentThread(tag);
                        Logger.printVar(tag, integer);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
