import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by stream.
 */
public class Conditional {


  @Test
  public void amb() throws InterruptedException {
    Observable<String> obs1 = Observable.timer(300, TimeUnit.MILLISECONDS).map(v -> "first");
    Observable<String> obs2 = Observable.timer(500, TimeUnit.MILLISECONDS).map(v -> "second");

    //组合起来只弹出第一个.
    Observable.amb(obs1, obs2).subscribe(System.out::println);

    Thread.sleep(2000);
  }

  @Test
  public void defaultValue() {
    Observable.empty().defaultIfEmpty(true).subscribe(System.out::println);
  }

  @Test
  public void skipUntilWhile() throws InterruptedException {
    Observable
        .timer(0, 1000, TimeUnit.MILLISECONDS)
        .skipUntil(Observable.timer(5000, TimeUnit.MILLISECONDS))
        .subscribe(System.out::println);

    //会跳过5秒前的数据
    Thread.sleep(8000);


    System.out.println("-----------------------");
    //跳过小于3的元素
    Observable.range(1, 5).skipWhile(x -> x < 3).subscribe(System.out::println);

  }

  @Test
  public void takeUntilWhile() throws InterruptedException {
    Observable
        .timer(0, 1000, TimeUnit.MILLISECONDS)
        .takeUntil(Observable.timer(3000, TimeUnit.MILLISECONDS))
        .subscribe(System.out::println);

    Thread.sleep(5000);

    System.out.println("-----------------------");

    Observable.range(1, 5).takeWhile(x -> x < 3).subscribe(System.out::println);
  }

  @Test
  public void boolPredict() {
    //所有的必须满足一个条件
    Observable.range(1, 5).all(x -> x < 6).subscribe(System.out::println);

    //
    Observable.range(1, 5).contains(3).subscribe(System.out::println);

    //
    Observable.range(1, 5).exists(x -> x == 3).subscribe(System.out::println);

    //
    Observable.sequenceEqual(Observable.range(1, 5), Observable.range(1, 5)).subscribe(System.out::println);

  }


}
