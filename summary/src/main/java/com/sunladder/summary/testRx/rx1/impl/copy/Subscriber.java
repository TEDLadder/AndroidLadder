package com.sunladder.summary.testRx.rx1.impl.copy;

public abstract class Subscriber<T> implements Observer<T>, Subscription {

    private Producer producer;

    public void setProducer(Producer producer) {
        this.producer = producer;
    }
}
