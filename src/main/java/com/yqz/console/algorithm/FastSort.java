package com.yqz.console.algorithm;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FastSort {
    private static final int[] src = new int[]{10000, 0, 6, 2, 4, 3, 5, 1, 1000};

    public static void main(String[] args) {
        int[] array = src;
        quickSort(array, 0, array.length-1);
        print(array);
        quickSort(array, 0, array.length - 1);
        print(array);


    }

    public static void print(int[] a) {
        System.out.println(Arrays.stream(a).mapToObj(p -> String.valueOf(p)).collect(Collectors.reducing((m, n) -> String.valueOf(m) + "," + String.valueOf(n))).orElse(""));
    }


    static void quickSort(int[] a, int left, int right)//分治法 子问题求解
    {
        if (left < right) {
            int i = left, j = right;
            int keyValue = a[left];
            while (i < j) {
                while (i < j && a[j] > keyValue) {
                    j--;
                }
                if (i < j) {
                    a[i++] = a[j];
                }
                while (i < j && a[i] < keyValue) {
                    i++;
                }
                if (i < j) {
                    a[j--] = a[i];
                }

            }
            a[i] = keyValue;

            quickSort(a,left,i-1);

            quickSort(a,i+1,right);
        }
    }

    public static void swap(int[] array, int i, int j) {
        if (i != j) {
            int t = array[j];
            array[j] = array[i];
            array[i] = t;
        }
    }
}
