package com.sunladder.test.rx1.round;

import com.sunladder.test.rx1.RxRound;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action1;

public class RxCreate implements RxRound {

    @Override
    public void run() {
        create1();
        //carsh
        create2();
        //crash
        create3();
        //complete
        create4();
    }

    private void create1() {
        Observable
                .create(new OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onNext("");
                    }
                })
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {

                    }
                });
    }

    private void create2() {
        Observable
                .create(new OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onNext("");
                        int error = 1 / 0;
                    }
                })
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {

                    }
                });
    }

    private void create3() {
        Observable
                .create(new OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onNext("");
                        subscriber.onError(null);
                    }
                })
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {

                    }
                });
    }

    private void create4() {
        Observable
                .create(new OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onNext("");
                        subscriber.onError(null);
                    }
                })
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
