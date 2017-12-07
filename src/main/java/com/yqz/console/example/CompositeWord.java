package com.yqz.console.example;

import java.util.LinkedList;

/*
 * 字符串的排列组合
 */
public class CompositeWord {
	public static void print(String s) {

		LinkedList<String[]> result = chai(s);
		int length = result.size();
		for (int i = 0; i < length; i++) {
			System.out.println(String.join("", result.get(i)));
		}
		System.out.println("total composite :" + length);
	}

	private static LinkedList<String[]> chai(String s) {
		LinkedList<String[]> result = new LinkedList<String[]>();
		if (s.length() == 0)
			return result;
		else if (s.length() == 1) {
			result.add(new String[] { s });
			return result;
		}

		else if (s.length() == 2) {
			String[] ss = s.split("");
			result.add(new String[] { ss[0], ss[1] });
			result.add(new String[] { ss[1], ss[0] });
			return result;
		}

		String left = s.substring(0, 1);
		LinkedList<String[]> remain = chai(s.substring(1));

		for (int i = 0; i < remain.size(); i++) {
			for (int j = 0; j < remain.get(i).length + 1; j++) {
				result.add(insert(remain.get(i), j, left));
			}
		}

		return result;

	}

	/**
	 * 将字符串{@code insertedString}插入到字符串数组{@code ss}中第{@code insertedPosition}个位置
	 * 
	 * @param ss
	 * @param insertedPosition
	 * @param insertedString
	 * @return
	 */
	private static String[] insert(String[] ss, int insertedPosition, String insertedString) {
		if (insertedPosition < 0 || insertedPosition > ss.length)
			throw new IndexOutOfBoundsException();
		String[] result = new String[ss.length + 1];

		for (int i = 0; i < ss.length + 1; i++) {
			if (i < insertedPosition)
				result[i] = ss[i];
			else if (i > insertedPosition)
				result[i] = ss[i - 1];
			else
				result[insertedPosition] = insertedString;
		}
		return result;
	}

	public static void main(String[] args) {
		String s = "正大光明";
		CompositeWord.print(s);
	}

}
