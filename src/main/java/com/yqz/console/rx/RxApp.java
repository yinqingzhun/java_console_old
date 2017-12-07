package com.yqz.console.rx;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxApp {
	public static void main2(String[] args) {
		// Flowable.just("Hello world").subscribe(System.out::println);
		Flowable.fromCallable(() -> {
			Thread.sleep(1000); // imitate expensive computation
			return "Done";
		}).subscribeOn(Schedulers.io()).observeOn(Schedulers.single()).subscribe(System.out::println,
				Throwable::printStackTrace);

		Flowable.range(1, 10).observeOn(Schedulers.computation()).map(v -> v * v).subscribe(System.out::println);

		Flowable.range(1, 10).flatMap(v -> Flowable.just(v).subscribeOn(Schedulers.computation()).map(w -> w * w))
				.subscribe(System.out::println);

		try {
			Thread.sleep(1100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void observableObserver() {
		Observable<Integer> mObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> e) throws Exception {
				e.onNext(1);
				e.onNext(2);
				e.onComplete();
			}
		});

		Observer<Integer> mObserver = new Observer<Integer>() {
			// 这是新加入的方法，在订阅后发送数据之前，
			// 回首先调用这个方法，而Disposable可用于取消订阅
			@Override
			public void onSubscribe(Disposable d) {
				System.out.println(d);
			}

			@Override
			public void onNext(Integer value) {
				System.out.println(value);
			}

			@Override
			public void onError(Throwable e) {
				System.out.println(e);
			}

			@Override
			public void onComplete() {
				System.out.println("received completely.");
			}
		};

		mObservable.subscribe(mObserver);
	}

	// private static void flowableSubscriber() {
	// Flowable.range(0, 10).subscribe(new Subscriber<Integer>() {
	// Subscription sub;
	//
	// // 当订阅后，会首先调用这个方法，其实就相当于onStart()，
	// // 传入的Subscription s参数可以用于请求数据或者取消订阅
	// @Override
	// public void onSubscribe(Subscription s) {
	// Log.w("TAG", "onsubscribe start");
	// sub = s;
	// sub.request(1);
	// Log.w("TAG", "onsubscribe end");
	// }
	//
	// @Override
	// public void onNext(Integer o) {
	// Log.w("TAG", "onNext--->" + o);
	// sub.request(1);
	// }
	//
	// @Override
	// public void onError(Throwable t) {
	// t.printStackTrace();
	// }
	//
	// @Override
	// public void onComplete() {
	// Log.w("TAG", "onComplete");
	// }
	// });
	// }
}
