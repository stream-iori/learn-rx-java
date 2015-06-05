package me.streamis.rx.learn;

import rx.util.async.Async;

/**
 *
 */
public class AsyncRx {

  public static void main(String[] args) {
    Async
        .start(() -> {
          haveALook();
          return "a";
        })
        .subscribe(
            o -> haveALook(),
            Throwable::printStackTrace);
  }

  private static void haveALook() {
    System.out.println(Thread.currentThread().getName());
  }

}
