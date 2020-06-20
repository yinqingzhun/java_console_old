package com.yqz.console.tech.executor;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

public class SerialExecutor implements Executor {
	final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
	final Executor executor;
	Runnable active;

	SerialExecutor(Executor executor) {
		this.executor = executor;
	}

	public synchronized void execute(final Runnable r) {
		tasks.offer(new Runnable() {
			public void run() {
				try {
					r.run();
				} finally {
					scheduleNext();
				}
			}
		});
		if (active == null) {
			System.out.println("hi,this is no active");
			scheduleNext();
		}
	}

	protected synchronized void scheduleNext() {
		if ((active = tasks.poll()) != null) {
			executor.execute(active);
		}
	}

	public static void main(String[] args) {
		ThreadPerTaskExecutor de = new ThreadPerTaskExecutor();
		SerialExecutor executor = new SerialExecutor(de);
		executor.execute(() -> {
			System.out.println("hi,this is no.1");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		executor.execute(() -> System.out.println("hi,this is no.2"));
	}
}
