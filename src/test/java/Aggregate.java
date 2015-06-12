import org.junit.Test;
import rx.Observable;

import java.util.ArrayList;

/**
 * Created by stream.
 */
public class Aggregate {

  @Test
  public void aggregate() {
    Observable.range(1, 5)
        .reduce((integer, integer2) -> integer + integer2)
        .subscribe(System.out::println);

    //

    Observable.range(1, 5).collect(ArrayList::new, ArrayList::add).subscribe(result -> {
      System.out.println(result.size());
    });

    //
    Observable.range(1, 5).toList().subscribe(result -> {
      System.out.println(result.size());
    });

    //
    Observable.range(1, 5)
        .toMap(integer -> "a" + integer, integer -> integer + 1)
        .subscribe(System.out::println);

    //
    Observable.range(1, 5)
        .toMultimap(integer -> "a" + integer, integer -> integer + 1)
        .subscribe(System.out::println);

    //
    Observable.range(1, 5).collect(ArrayList::new, ArrayList::add).subscribe(System.out::println);

  }

}
