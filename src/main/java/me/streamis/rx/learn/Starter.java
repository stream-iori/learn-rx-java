package me.streamis.rx.learn;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by stream.
 */
public class Starter {

  public static void main(String[] args) {
    Observable<String> myObservable = Observable.create(
        new Observable.OnSubscribe<String>() {
          @Override
          public void call(Subscriber<? super String> sub) {
            System.out.println("observer on thread " + Thread.currentThread().getName());
            sub.onNext("Hello, world!");
            sub.onCompleted();
          }
        }
    );

    //create a subscriber to consume the data.
    Subscriber<String> mySubscriber = new Subscriber<String>() {
      @Override
      public void onNext(String s) {
        System.out.println("subscriber on thread " + Thread.currentThread().getName());
        System.out.println(s);
      }

      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
      }
    };

    //当subscription调用时,myObserable会调用其onNext方法将参数传入到 Subscriber的 onNext方法里.
    myObservable
        .subscribeOn(Schedulers.io())        //指定Observer运行在IO线程上.
        .observeOn(Schedulers.computation()) //Subscriber运行在Computation线程上.一般是主线程或者EventLoop线程上
        .subscribe(mySubscriber);


    Subscription subscription =  myObservable.subscribe(mySubscriber);
    //subscription是 Observable 与 Subscriber之间的链接. 你可以通过subscription中断链


    //Action其实是 Subscriber的参数
    Action1<String> onNextAction = System.out::println;
    myObservable.subscribeOn(Schedulers.io()).subscribe(onNextAction);

  }
}
