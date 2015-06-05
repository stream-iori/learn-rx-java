import org.junit.Test;
import rx.Observable;
import rx.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;


/**
 * Created by stream.
 */
public class Connectable {

  @Test
  public void connect() throws InterruptedException {
    ConnectableObservable<Integer> obs = ConnectableObservable.range(1, 1000000000).sample(1000, TimeUnit.MILLISECONDS).publish();

    obs.subscribe(System.out::println);
    obs.subscribe(System.out::println);

    //把上面的obs连接起来,使之在一起
    obs.connect();

    Thread.sleep(3000);
  }

  @Test
  public void connect2() {
    //看看与上面的区别
    Observable.range(1, 5).concatWith(Observable.range(6, 10)).subscribe(System.out::println);
  }


}
