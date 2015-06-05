import org.junit.Test;
import rx.Observable;
import rx.observables.JoinObservable;

import java.util.concurrent.TimeUnit;

/**
 * Created by stream.
 */
public class Combining {

  @Test
  public void startWithTest() {
    //定义一个新的起始
    Observable.just(1, 2, 3).startWith(0).subscribe(System.out::println);
  }


  @Test
  public void mergeTest() {
    Observable.merge(Observable.just(0, 2, 4), Observable.just(1, 3, 5)).subscribe(System.out::println);
    System.out.println("---------------");

    Observable.merge(Observable.from(new Integer[]{1, 2, 3}), Observable.from(new Integer[]{4, 5, 6})).subscribe(System.out::println);
    System.out.println("---------------");

    Observable.just(1, 2, 3).mergeWith(Observable.just(4, 5, 6)).subscribe(System.out::println);
    System.out.println("---------------");

    //错误最后在输出.
    Observable.mergeDelayError(Observable.just(1, 2, 3), Observable.error(new RuntimeException("error")), Observable.just(4, 5, 6))
        .subscribe(System.out::println, ex -> System.out.println(ex.getMessage()));
  }


  @Test
  public void zipAndZipWithTest() {
    //交替
    Observable<Integer> range = Observable.range(0, 5);
    range
        .zipWith(range.skip(1), (integer, integer2) -> integer + ":" + integer2)
        .subscribe(System.out::println);

    System.out.println("---------------------");

    Observable
        .zip(range.skip(1), range.skip(2), (integer, integer2) -> integer + ":" + integer2)
        .subscribe(System.out::println);
  }


  @Test
  public void andThen() throws InterruptedException {
    JoinObservable
        .when(
            JoinObservable
                .from(Observable.timer(200, TimeUnit.MILLISECONDS))
                .and(Observable.timer(200, TimeUnit.MILLISECONDS))
                .then((o1, o2) -> "first"),

            JoinObservable
                .from(Observable.timer(400, TimeUnit.MILLISECONDS))
                .and(Observable.timer(500, TimeUnit.MILLISECONDS))
                .then((integer, o) -> "second")
        ).toObservable().subscribe(System.out::println);

    Thread.sleep(1000);
  }

  @Test
  public void combineLatestFrom() throws InterruptedException {
    Observable<String> obs1 = Observable.interval(140, TimeUnit.MILLISECONDS).map(i -> "First:" + i);
    Observable<String> obs2 = Observable.interval(50, TimeUnit.MILLISECONDS).map(i -> "Second:" + i);

    //When obs1 emits a value, combine it with the latest emission from obs2.

    Observable
        .combineLatest(obs1, obs2, (s, s2) -> s + " , " + s2)
        .take(10).subscribe(System.out::println);

    Thread.sleep(1000);
  }

  @Test
  public void join() throws InterruptedException {
    //定义obs每隔100毫秒触发一次
    Observable<String> xs = Observable.interval(100, TimeUnit.MILLISECONDS).map(aLong -> "First:" + aLong);
    Observable<String> ys = Observable.interval(200, TimeUnit.MILLISECONDS).map(aLong -> "Second:" + aLong);

    xs.join(ys,
        s -> Observable.timer(10, TimeUnit.MILLISECONDS), // 每隔10毫秒就把xs的item emit出来
        s2 -> Observable.timer(10, TimeUnit.MILLISECONDS),// 每隔100毫秒就把ys的item emit出来
        (s, s2) -> s + "-" + s2).subscribe(System.out::println);

    //这里出5条信息. 因为 right 只能出现5条

    Thread.sleep(1000);

//    xs.groupJoin(ys,
//        s -> Observable.timer(10, TimeUnit.MILLISECONDS),
//        s2 -> Observable.timer(10, TimeUnit.MILLISECONDS),
//        (s, s2) -> {
//          return s2.map(s1 -> s + s1);
//        }).subscribe(System.out::println);
//
//    Thread.sleep(1000);
  }

}
