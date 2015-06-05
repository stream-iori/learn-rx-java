import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;
import rx.util.async.Async;

import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class AsyncRx {

  @Test
  public void async() throws InterruptedException {
    haveALook();

//    Observable.from(CompletableFuture.runAsync(() -> {
//      haveALook();
//    })).subscribe(o -> haveALook(), Throwable::printStackTrace);

    Async.fromRunnable(() -> {
      throw new RuntimeException("ex");
    }, null).subscribe(o -> haveALook(), Throwable::printStackTrace);

//    Async.start(() -> {
//      future();
//      return null;
//    }).subscribe(o -> haveALook(), Throwable::printStackTrace);


    Thread.sleep(3000);

  }

  private static void haveALook() {
    System.out.println(Thread.currentThread().getName());
  }

  private CompletableFuture<Void> future() {
    return CompletableFuture.runAsync(() -> haveALook()).thenAccept(aVoid -> haveALook());
  }


  @Test
  public void hook() {
    RxJavaPlugins.getInstance().registerObservableExecutionHook(new MyObsHook());

    Observable.just(1, 2, 3)
        .map(integer -> ++integer)
        .subscribe(aInt -> {
          System.out.println(aInt);
        }, ex -> {
          ex.printStackTrace();
        });
  }

  class MyObsHook extends RxJavaObservableExecutionHook {
    public MyObsHook() {
      super();
    }

    @Override
    public <T> Observable.OnSubscribe<T> onCreate(Observable.OnSubscribe<T> f) {
      System.out.println("before create.");
      return super.onCreate(f);
    }

    @Override
    public <T> Observable.OnSubscribe<T> onSubscribeStart(Observable<? extends T> observableInstance, Observable.OnSubscribe<T> onSubscribe) {
      System.out.println("on subscribe start.");
      return super.onSubscribeStart(observableInstance, onSubscribe);
    }

    @Override
    public <T> Subscription onSubscribeReturn(Subscription subscription) {
      System.out.println("on subscribe return.");
      return super.onSubscribeReturn(subscription);
    }

    @Override
    public <T> Throwable onSubscribeError(Throwable e) {
      System.out.println("on subscribe error.");
      return super.onSubscribeError(e);
    }

    @Override
    public <T, R> Observable.Operator<? extends R, ? super T> onLift(Observable.Operator<? extends R, ? super T> lift) {
      System.out.println("on lift.");
      return super.onLift(lift);
    }
  }

}
