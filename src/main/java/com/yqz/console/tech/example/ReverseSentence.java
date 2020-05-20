package com.yqz.console.tech.example;

import java.util.regex.Pattern;
/*
 * 翻转句子
 */
public class ReverseSentence {
	public static void print(String s) {
		Pattern p = Pattern.compile("\\b");
		String[] array = p.split(s);
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[array.length - 1 - i]);
		}
	}

	public static void main(String[] args) {
		ReverseSentence.print("example:a+b=3");
	}

}
