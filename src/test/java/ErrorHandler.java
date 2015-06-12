import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ErrorHandler {

  @Test
  public void errorResumeNext() {
    //捕捉到后处理.然后抛出一个自定义的Obserable.
    Observable.error(new RuntimeException("MyException.")).onErrorResumeNext(throwable -> {
      return Observable.just("Ok, i have got exception:" + throwable.getMessage());
    }).subscribe(System.out::println);

    //忽略异常
    Observable.error(new RuntimeException("MyException."))
        .onErrorResumeNext(Observable.just("OK"))
        .subscribe(System.out::println);

    Observable.error(new RuntimeException("MyException."))
        .onExceptionResumeNext(Observable.just("OK"))
        .subscribe(System.out::println);


    //直接返回字符串,而不是Obserable
    Observable.error(new RuntimeException("MyException."))
        .onErrorReturn(throwable -> "OK")
        .subscribe(System.out::println);

  }


  @Test
  public void retry() {
    Observable.error(new RuntimeException("MyException."))
        .retry(2)
        .subscribe(System.out::println, ex -> System.out.println(ex.getMessage()));

    System.out.println("-----------------------------");

    Observable.create(subscriber -> {
      System.out.println("subscribing");
      subscriber.onError(new RuntimeException("always fails"));
      //attempts 需要处理一下,然后再返回
    }).retryWhen(attempts -> attempts.zipWith(Observable.range(1, 3), (ex, i) -> {
      System.out.println(ex.getMessage());
      return i;
    }).flatMap(i -> {
      System.out.println("delay retry by " + i + " second(s)");
      return Observable.timer(i, TimeUnit.SECONDS);
    })).toBlocking().forEach(System.out::println);

  }

}
