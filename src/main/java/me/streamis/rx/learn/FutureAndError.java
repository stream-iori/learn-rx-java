package me.streamis.rx.learn;

import rx.Observable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 */
public class FutureAndError {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
//    Future<Void> future = CompletableFuture.runAsync(() -> { throw  new RuntimeException("error...");});
//
//    Observable.from(future).subscribe(
//        o -> System.out.println("ok"),
//        Throwable::printStackTrace);

    System.out.println(Optional.ofNullable(null).orElse("b"));

    Observable.error(new RuntimeException("first error."))
        .flatMap(s -> Observable.just("b"))
        .onErrorResumeNext(throwable -> {
          System.out.println(throwable.getMessage());
          return Observable.error(new RuntimeException("second.error"));
        }).subscribe(System.out::println, Throwable::printStackTrace);

//    Observable.from(run()).subscribe(
//        System.out::println,
//        Throwable::printStackTrace);
    Thread.sleep(10000);
  }


  private static Future<Boolean> run() throws InterruptedException {
    return run1()
        .thenCompose(aVoid -> run2())
        .handle((s, throwable) -> {
          if (throwable != null) {
            throwable.printStackTrace();
          } else {
            System.out.println("result is " + s);
          }
          return true;
        });
  }

  private static CompletableFuture<Void> run1() {
    System.out.println("1->" + Thread.currentThread().getName());
    //future.completeExceptionally(new RuntimeException("error...."));
    return CompletableFuture.runAsync(() -> {
      try {
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getName());
        System.out.println("a");
        //throw new RuntimeException("exxx");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
  }

  private static CompletableFuture<String> run2() {
    return CompletableFuture.supplyAsync(() -> {
      System.out.println(Thread.currentThread().getName());
      //throw new RuntimeException("exxx");
      return "b";
    });
  }

}
