/*
package com.yqz.console.algorithm;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FastSort {
    private static final int[] src = new int[]{10000, 0, 6, 2, 4, 3, 5, 1, 1000};

    public static void main(String[] args) {
        int[] array = src;
        QuickSort(array, 0, array.length);
        print(array);
       */
/* 
        quickSort(array, 0, array.length - 1);
        print(array);*//*


    }

    public static void print(int[] a) {
        System.out.println(Arrays.stream(a).mapToObj(p -> String.valueOf(p)).collect(Collectors.reducing((m, n) -> String.valueOf(m) + "," + String.valueOf(n))).orElse(""));
    }

    static int PartSort1(int[] a, int left, int right) {
        int key = right;
        while (left < right) {
            while (left < right && a[left] <= a[key])
                ++left;
            while (left < right && a[right] >= a[key])
                --right;
            swap(a, left, right);
        }
        swap(a, left, key);
        return left;
    }

    static void QuickSort(int[] a, int left, int right)//分治法 子问题求解
    {
        if (left >= right)
            return;
        int key = PartSort1(a, left, right);
        QuickSort(a, left, key - 1);
        QuickSort(a, key + 1, right);
    }

    public static void swap(int[] array, int i, int j) {
        if (i != j) {
            int t = array[j];
            array[j] = array[i];
            array[i] = t;
        }
    }
}
*/
