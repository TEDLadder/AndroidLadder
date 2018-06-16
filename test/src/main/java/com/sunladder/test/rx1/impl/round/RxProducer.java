package com.sunladder.test.rx1.impl.round;

import com.sunladder.common.log.Logger;

import com.sunladder.test.rx1.RxRound;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.observables.SyncOnSubscribe;

public class RxProducer implements RxRound {

    public static int NORMAL_NUM = 0;

    /**
     * create&just producer差异
     */
    @Override
    public void run() {
        testCreate();
        normal();
    }

    /**
     * {@link rx.internal.util.ScalarSynchronousObservable.WeakSingleProducer#request(long)}
     * <p>
     * WeakSingleProducer仅调用一次
     */
    private void testCreate() {
        Observable.just(NORMAL_NUM)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Logger.printVar("testCreate subscriber:", integer);
                    }
                });
    }

    /**
     * {@link SyncOnSubscribe.SubscriptionProducer#request}
     * {@link SyncOnSubscribe.SubscriptionProducer#slowPath(long)}
     * {@link SyncOnSubscribe.SubscriptionProducer#fastPath()}
     * <p>
     * SyncOnSubscribe.SubscriptionProducer for循环,无限next,除非被阻断
     */
    private void normal() {
        final Observable<Integer> observable = Observable
                .create(new SyncOnSubscribe<Integer, Integer>() {
                    @Override
                    protected Integer generateState() {
                        Logger.printCurrentMethod();
                        return NORMAL_NUM;
                    }

                    @Override
                    protected Integer next(Integer state, Observer<? super Integer> observer) {
                        Logger.printCurrentMethod();
                        Logger.printVar("state", state);

                        observer.onNext(state);
                        return null;
                    }
                });

        observable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer s) {
                Logger.printVar("subscriber 1:", s);
            }
        });
        observable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer s) {
                Logger.printVar("subscriber 2:", s);
            }
        });
    }
}
