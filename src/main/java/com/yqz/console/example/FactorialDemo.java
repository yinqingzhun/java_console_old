package com.yqz.console.example;

/*
 * 阶乘
 */
public class FactorialDemo {

	public static long factorial(long n) {
		if (n <= 1)
			return 1;
		return factorial(n - 1) * n;
	}

	public static long factorial2(long n) {
		long result = 1;
		for (int i = 1; i <= n; i++) {
			result *= i;
		}
		return result;
	}

	public static void main(String args[]) {
		System.out.println(FactorialDemo.factorial(5));
		System.out.println(FactorialDemo.factorial2(5));
	}
}
