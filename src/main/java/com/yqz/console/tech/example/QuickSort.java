package com.yqz.console.tech.example;

/*
 * 快速排序
 */
public class QuickSort {
	public static void sort(int arr[], int low, int high) {
		int l = low;
		int h = high;
		int povit = arr[low];

		while (l < h) {
			while (l < h && arr[h] >= povit)
				h--;
			if (l < h) {
				int temp = arr[h];
				arr[h] = arr[l];
				arr[l] = temp;
				l++;
			}
			System.out.printf("L:%s,R:%s\r", l,h);
			print(arr);

			while (l < h && arr[l] <= povit)
				l++;

			if (l < h) {
				int temp = arr[h];
				arr[h] = arr[l];
				arr[l] = temp;
				h--;
			}
			System.out.printf("L:%s,R:%s\r", l,h);
			print(arr);
		}

		System.out.print("l=" + (l + 1) + ",h=" + (h + 1) + ",povit=" + povit + "\n");
		if (l - 1 > low)
			sort(arr, low, l - 1);
		if (h + 1 < high)
			sort(arr, h + 1, high);
	}

	private static void print(int[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + (i < array.length - 1 ? "," : ""));
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] array = { 6, 8, 4, 2, 1, 3, 5, 9, 7 };
		print(array);
		QuickSort.sort(array, 0, array.length - 1);
		print(array);
	}

}