package com.yqz.console.tech.example;

public class FizzBuzzDemo {

	public static void print(int from, int to) {
		for (; from < to; from++) {
			if (from % 3 == 0 && from % 5 == 0)
				System.out.println(from+" FizzBuzz");
			else if (from % 3 == 0)
				System.out.println(from+" Fizz");
			else if (from % 5 == 0)
				System.out.println(from+" Buzz");
			else
				System.out.println(from);
		}
	}

	public static void main(String[] args) {
		FizzBuzzDemo.print(1, 100);

	}

}
