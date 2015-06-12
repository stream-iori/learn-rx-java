import org.junit.Test;
import rx.Observable;

import java.util.List;

/**
 * Created by stream.
 */
public class Transforming {


  @Test
  public void transform() {
    Observable.range(1, 10).scan((integer, integer2) -> integer + integer2).subscribe(System.out::println);
    System.out.println("-------------------------------");

    Observable.range(1, 10).reduce((integer, integer2) -> integer + integer2).subscribe(System.out::println);
    System.out.println("-------------------------------");


    Observable.just(1, 1, 2, 2, 3, 4, 5, 6)
        //第一参数提取key, 第二个参数确定返回的值
        .groupBy(integer -> integer, integer -> integer)
        .subscribe(
            obs -> {
              obs.doOnNext(integer -> {
                System.out.println("--------");
                System.out.println(integer);
                System.out.println("--------");
              }).count().subscribe(System.out::println);
            });
  }

  @Test
  public void transform2() {
    Observable.range(1, 10)
        .buffer(5) //这里把1-10分成了两个数组,每个数组buffer 5个元素
        .subscribe(System.out::println);

    Observable.range(1, 10)
        .buffer(5)
        .map(List::size) //统计每个数组元素个数
        .subscribe(System.out::println);

    System.out.println("--------------------------");

    Observable.range(1, 10)
        .window(5) //同上 只不过转成了Obserable
        .flatMap(o -> o)
        .subscribe(System.out::println);

    System.out.println("--------------------------");

    Observable.range(1, 10)
        .window(5) //同上 只不过转成了Obserable
        .flatMap(Observable::count)
        .subscribe(System.out::println);

  }


}
