package com.yqz.console.algorithm;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FastSort {
    private static final int[] src = new int[]{10, -2, 5, 8, -4, 2, -3, 7, 12, -88, -23, 35};

    public static void main(String[] args) {
        int[] array = src;
        sort(array, 0, array.length - 1);
        print(array);
    }

    public static void print(int[] a) {
        System.out.println(Arrays.stream(a).mapToObj(p -> String.valueOf(p)).collect(Collectors.reducing((m, n) -> String.valueOf(m) + "," + String.valueOf(n))).orElse(""));
    }

    static void sort(int[] a, int left, int right) {
        int i = left;
        int j = right;
        int keyValue = 0;

        while (i < j) {
            while (i < j && a[i] < keyValue)
                i++;
            while (i < j && a[j] > keyValue)
                j--;

            if (i < j)
                swap(a, i, j);
        }
    }

    static void sort2(int[] a, int left, int right) {
        int z = a[0];
        int i = left;
        int j = right;
        int keyValue = 0;
        a[0] = 0;

        while (i < j) {
            while (i < j && a[j] > keyValue)
                j--;
            if (i < j) {
                a[i++] = a[j];
            }

            while (i < j && a[i] < keyValue)
                i++;

            if (i < j) {
                a[j--] = a[i];
            }
        }
        
        a[i]=z;
    }

    


    public static void swap(int[] array, int i, int j) {
        if (i != j) {
            int t = array[j];
            array[j] = array[i];
            array[i] = t;
        }
    }
}
