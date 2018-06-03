package com.sunladder.common.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sunladder.common.R;
import com.sunladder.common.log.Logger;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.observables.SyncOnSubscribe;

public class RxTestAct extends AppCompatActivity {

    public static int normal_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test);

        roundOne();
    }

    /**
     * create&just producer差异
     */
    private void roundOne() {
        normal();
        testCreate();
    }

    /**
     * {@link rx.internal.util.ScalarSynchronousObservable.WeakSingleProducer#request(long)}
     * <p>
     * WeakSingleProducer仅调用一次
     */
    private void testCreate() {
        Observable.just(normal_num)
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
                        return normal_num;
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
