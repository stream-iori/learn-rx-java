import org.junit.Test;
import rx.Observable;

/**
 * Created by stream.
 */
public class Utility {


  @Test
  public void utility() {
    Observable.just(1, 1, 2, 2).materialize().subscribe(notification -> {
      System.out.println(notification.getKind());
      System.out.println(notification.getValue());
    });

    System.out.println("=---------------------=");

    Observable.range(1, 4).doOnEach(notification -> {
      System.out.println(notification.isOnNext());
      System.out.println(notification.getValue());
    }).subscribe(System.out::println);//通过subscribe触发

    System.out.println("=---------------------=");

    Observable.range(1, 4)
        .doOnCompleted(() -> System.out.println("over"))
        .doOnTerminate(() -> System.out.println("terminate"))
        .doOnSubscribe(() -> System.out.println("start subscribe"))
        .doOnUnsubscribe(() -> System.out.println("start unSubscribe")) //subscribe结束后应该调用
        .finallyDo(() -> System.out.println("finally"))
        .timestamp() //用timestamp包装一下
        .subscribe(System.out::println, ex -> System.out.println("异常"));


  }

}
