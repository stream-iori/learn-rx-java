import org.junit.Test;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by stream
 */
public class CustomOperate {

  class MyOperator<T> implements Observable.Operator<String, T> {

    @Override
    public Subscriber<? super T> call(Subscriber<? super String> subscriber) {
      return new Subscriber<T>(subscriber) {
        @Override
        public void onCompleted() {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onCompleted();
          }
        }

        @Override
        public void onError(Throwable e) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onError(e);
          }
        }

        @Override
        public void onNext(T t) {
          if (!subscriber.isUnsubscribed()) {
            System.out.println("value is " + t);
            subscriber.onNext("convert to string as " + t.toString());
          }
        }
      };
    }
  }

  @Test
  public void myOperator() {
    Observable.range(1, 2).lift(new MyOperator<>()).subscribe(System.out::println);
  }


  class MyTransformer implements Observable.Transformer<Integer, String> {
    @Override
    public Observable<String> call(Observable<Integer> integerObservable) {
      return integerObservable.map(integer -> "num:" + integer);
    }
  }


  @Test
  public void myTransform() {
    Observable.range(1, 2).compose(new MyTransformer()).subscribe(System.out::println);
  }


}
