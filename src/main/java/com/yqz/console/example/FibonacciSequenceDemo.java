package com.yqz.console.example;

import java.util.HashMap;
import java.util.Map;

public class FibonacciSequenceDemo {

	public static long run(int n) {
		if (n < 0)
			throw new IllegalArgumentException();
		if (n == 0 || n == 1)
			return 1;
		else
			return run(n - 1) + run(n - 2);
	}

	public static long run2(int n) {
		if (n < 0)
			throw new IllegalArgumentException();
		Map<Integer, Long> map = new HashMap<Integer, Long>();
		for (int i = 0; i <= n; i++) {
			if (i == 0 || i == 1)
				map.put(i, 1l);
			else {
				map.put(i, map.get(i - 2) + map.get(i - 1));
			}
		}
		return map.get(n);

	}

	public static int countWays(int n) {

		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i <= n; i++) {
			if (i == 1 || i == 0)
				map.put(i, 0);
			else if (i == 2)
				map.put(2, 2);
			else {
				map.put(i, map.get(i - 1) + map.get(i - 2));
			}
		}

		return map.get(n);
	}

	public static void main(String[] args) {
		System.out.println(run2(3));
		// System.out.println(countWays(3));
	}
}
