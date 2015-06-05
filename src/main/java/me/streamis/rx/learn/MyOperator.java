package me.streamis.rx.learn;

import rx.Observable;
import rx.Subscriber;

/**
 *
 */
public class MyOperator<T, R> implements Observable.Operator<T, R> {


  @Override
  public Subscriber<? super R> call(final Subscriber<? super T> subscriber) {
    subscriber.unsubscribe();
    return new Subscriber<R>(subscriber) {
      @Override
      public void onCompleted() {
        if (!subscriber.isUnsubscribed()) {
          System.out.println("operator on completed");
          subscriber.onCompleted();
        }
      }

      @Override
      public void onError(Throwable e) {
        if (!subscriber.isUnsubscribed()) {
          System.out.println("aop ex->" + e.getMessage());
          subscriber.onError(new RuntimeException("请重试"));
        }
      }

      @Override
      public void onNext(R t) {
        if (!subscriber.isUnsubscribed()) {
          System.out.println("做一些转换处理");
          subscriber.onNext((T) (t + ""));
        }
      }
    };
  }
}
