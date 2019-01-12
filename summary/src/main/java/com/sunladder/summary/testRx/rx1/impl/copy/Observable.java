package com.sunladder.summary.testRx.rx1.impl.copy;


public class Observable<T> {

    private OnSubscribe<T> onSubscribe;

    private Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public final void subscribe(Subscriber<? super T> subscriber) {
        onSubscribe.call(subscriber);
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<T>(onSubscribe);
    }

    /**
     * Invoked when Observable.subscribe is called.
     *
     * @param <T> the output value type
     */
    public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
        // cover for generics insanity
    }

    /**
     * Operator function for lifting into an Observable.
     *
     * @param <T> the upstream's value type (input)
     * @param <R> the downstream's value type (output)
     */
    public interface Operator<R, T> extends Func1<Subscriber<? super R>, Subscriber<? super T>> {
        // cover for generics insanity
    }
}
