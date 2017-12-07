package com.yqz.console.example;
/*
 * 字符串是否对称
 */
public class SymmetricalWordDemo {
	public static void print(String s) {
		if (!isSymmetrical(s))
			return;

		String[] ss = s.split("");
		for (int i = 0; i < ss.length / 2; i++) {
			if (ss[i].equals(ss[ss.length - 1 - i]) && i != ss.length - 1 - i)
				System.out.printf("index:%d,%d,value:%s\n", i, ss.length - 1 - i, ss[i]);
		}

	}

	public static boolean isSymmetrical(String s) {
		String[] ss = s.split("");
		for (int i = 0; i < ss.length; i++) {
			if (!ss[i].equals(ss[ss.length - 1 - i]))
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String s = "abcdefgfedcba";
		SymmetricalWordDemo.print(s);
	}
}
