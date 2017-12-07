package com.yqz.console.parallel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

public class ParalleMergeSort {
	public static void main(String[] args) {
		long[] array = {  5, 6, 8 ,-216, -17, -8,0};
		int lo = 0;
		int hi = array.length;
		int mid = (lo+hi)>>>1;

		long[] buf = Arrays.copyOfRange(array, lo, mid);
		for (int i = 0, l = lo, m = mid; i < buf.length; l++) {
			System.out.println(String.format("%s,%s,%s=%s,%s<%s",i,l, m, hi, (i >= buf.length ? -1 : buf[i]),
					(m >= array.length ? -1 : array[m])));
			array[l] = (m == hi || buf[i] < array[m]) ? buf[i++] : array[m++];
		}
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i]+",");
		}System.out.println();
//		System.out.println("sort array :"
//				+ String.join(",", array));

		final int SIZE = 7;
		Long[] list1 = new Long[SIZE];
		Long[] list2 = new Long[SIZE];

		for (int i = 0; i < list1.length; i++) {
			list1[i] = list2[i] = (long) (Math.random() * 10L);
		}

		System.out.println("input array :"
				+ String.join(",", Arrays.asList(list1).stream().map(p -> p.toString()).collect(Collectors.toList())));

		Long startTime = System.currentTimeMillis();
		paralleMergeSort(list1);
		Long endTime = System.currentTimeMillis();
		System.out.println("\nParallel time with " + Runtime.getRuntime().availableProcessors() + " processors is "
				+ (endTime - startTime) + " milliseconds");

		System.out.println("sorted array :"
				+ String.join(",", Arrays.asList(list1).stream().map(p -> p.toString()).collect(Collectors.toList())));

		// startTime = System.currentTimeMillis();
		// // MergeSort.mergeSort(list2);
		// Arrays.sort(list2);
		// endTime = System.currentTimeMillis();
		// System.out.println("\nSequential time is " + (endTime - startTime) +
		// " milliseconds");

	}

	public static void paralleMergeSort(Long[] list) {
		RecursiveAction mainTask = new SortTask(list);
		ForkJoinPool pool = new ForkJoinPool();
		pool.invoke(mainTask);
	}

	static class SortTask extends RecursiveAction {
		final int THRESHOLD = 5;
		final Long[] array;
		final int lo, hi;

		public SortTask(Long[] array, int lo, int hi) {
			this.array = array;
			this.lo = lo;
			this.hi = hi;
		}

		public SortTask(Long[] array) {
			this(array, 0, array.length);
		}

		protected void compute() {
			if (hi - lo < THRESHOLD)
				sortSequentially(lo, hi);
			else {
				int mid = (lo + hi) >>> 1;
				invokeAll(new SortTask(array, lo, mid), new SortTask(array, mid, hi));

				System.out.println(String.join(",",
						Arrays.asList(array).stream().map(p -> p.toString()).collect(Collectors.toList())));
				merge(lo, mid, hi);
			}
		}

		// implementation details follow:

		void sortSequentially(int lo, int hi) {
			Arrays.sort(array, lo, hi);
		}

		void merge(int lo, int mid, int hi) {
			Long[] buf = Arrays.copyOfRange(array, lo, mid);
			System.out.println(String.format("lo=%s,mid=%s,hi=%s", lo, mid, hi));
			System.out.println(
					String.join(",", Arrays.asList(buf).stream().map(p -> p.toString()).collect(Collectors.toList())));

			for (int i = 0, l = lo, m = mid; i < buf.length; l++)
				array[l] = (m == hi || buf[i] < array[m]) ? buf[i++] : array[m++];
		}
	}

	/*
	 * private static class SortTask extends RecursiveAction{ private final int
	 * THRESHOLD=500; private int[] list;
	 * 
	 * public SortTask(int[] list) { this.list=list; }
	 * 
	 * protected void compute() { if(list.length<THRESHOLD)
	 * java.util.Arrays.sort(list); else{ int[] firstHalf=new
	 * int[list.length/2]; System.arraycopy(list, 0, firstHalf, 0,
	 * list.length/2);
	 * 
	 * int secondHalfLength=list.length-list.length/2; int[] secondHalf=new
	 * int[secondHalfLength]; System.arraycopy(list,list.length/2 ,secondHalf,
	 * 0,secondHalfLength);
	 * 
	 * invokeAll(new SortTask(firstHalf),new SortTask(secondHalf));
	 * 
	 * MergeSort.merge(firstHalf, secondHalf, list); } }
	 */

}