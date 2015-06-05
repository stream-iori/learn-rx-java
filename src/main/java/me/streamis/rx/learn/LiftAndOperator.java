package me.streamis.rx.learn;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;
import rx.subjects.PublishSubject;

import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class LiftAndOperator {


  private static Observable<Integer> special() {
    return Observable.just(2);
  }

  public static Observable<String> sequence() {
    return Observable.just("myName")
        .flatMap(s -> special())
        .flatMap(new Func1<Integer, Observable<String>>() {
          @Override
          public Observable<String> call(Integer s) {
            //return Observable.error(new RuntimeException("exception...."));
            //String[] ss = new String[]{s + "a", s + "b"};
            //return Observable.from(ss);
            return Observable.just(s + "a");
          }
        })
        .flatMap(new Func1<String, Observable<String>>() {
          @Override
          public Observable<String> call(String s) {
            System.out.println("origin->" + s);
            //return Observable.error(new RuntimeException("exception."));
            return Observable.just("s");
          }
        });
  }

  public static void main(String[] args) {
    //RxJavaPlugins.getInstance().getObservableExecutionHook().onCreate(subscriber -> System.out.println("hook"));
    Observable.from(CompletableFuture.completedFuture("a")).subscribe(s -> {
      System.out.println(s);
    });

    RxJavaPlugins.getInstance().registerObservableExecutionHook(new RxJavaObservableExecutionHook() {
      @Override
      public <T> Observable.OnSubscribe<T> onCreate(Observable.OnSubscribe<T> f) {
        System.out.println("hook");
        return super.onCreate(f);
      }
    });

    PublishSubject.just(Observable.just("1"));


    sequence().subscribe(
        new Action1<String>() {
          @Override
          public void call(String o) {
            System.out.println("-->" + o);
          }
        },
        new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            System.out.println(throwable);
          }
        }
    );

  }

}
