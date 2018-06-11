package com.sunladder.test.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.sunladder.common.log.Logger;
import com.sunladder.test.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

public class RxTestAct extends AppCompatActivity {

    public static int normal_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test);

//        roundOne_producer();
        roundTwo_callChain();
//        roundThree_scheduler();
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


    /**
     * create&just producer差异
     */
    private void roundOne_producer() {
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
