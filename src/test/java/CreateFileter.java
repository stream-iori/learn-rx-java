import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by stream.
 */
public class CreateFileter {


  @Test
  public void create() {
    //创建一个订阅者
    Observable.create(subscriber -> {
      subscriber.onNext("onNext");
      subscriber.onCompleted();
    }).subscribe(System.out::println);//通过subscribe这个方法触发

    System.out.println("--------------------------------------");

    //延迟,直到subscribe触发
    Observable.defer(() -> Observable.just("Hello")).subscribe(System.out::println);
  }


  @Test
  public void filter() throws InterruptedException {
    //每隔100毫秒就抽样一些最近emit的数据
    Observable.interval(10, TimeUnit.MILLISECONDS).sample(100, TimeUnit.MILLISECONDS).subscribe(System.out::println);
    //sample 等效于 throttleLast
    Thread.sleep(5000);

    System.out.println("--------------------------------------");

    Observable.interval(10, TimeUnit.MILLISECONDS).throttleFirst(100, TimeUnit.MILLISECONDS).subscribe(System.out::println);
    Thread.sleep(5000);

    System.out.println("--------------------------------------");
  }

  @Test
  public void debounceAndThrottle() throws InterruptedException {
    //debounce 策略的电梯。如果电梯里有人进来，等待15秒。如果又人进来，15秒等待重新计时，直到15秒超时，开始运送
    Observable.range(100, 10)
        //delay each item by timer
        .flatMap(time -> {
          System.out.println(time);
          return Observable.timer(time, TimeUnit.MILLISECONDS);
        })
        //延迟105毫秒执行,但是不停的有timer触发,所以这里会在110执行后,触发一次
        .debounce(105, TimeUnit.MILLISECONDS)
        .subscribe(System.out::println);

    Thread.sleep(1000);
    System.out.println("--------------------------------------");

    Observable.range(100, 10)
        //delay each item by timer
        .flatMap(time -> {
          System.out.println(time);
          return Observable.timer(time, TimeUnit.MILLISECONDS);
        })
        //延迟105毫秒执行,但是不停的有timer触发,所以这里会在110执行后,触发一次
        .throttleWithTimeout(105, TimeUnit.MILLISECONDS)
        .subscribe(System.out::println);

    Thread.sleep(1000);

  }

}
