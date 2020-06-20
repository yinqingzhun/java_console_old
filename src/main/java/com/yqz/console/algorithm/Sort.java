package com.yqz.console.algorithm;

import com.google.common.base.Preconditions;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Sort {
    private static final int[] src = new int[]
//            { 1, 3, 7, 4};
//             {1, 3, 7, 9, 4};
            // {10000, 0, 6, 2, 4, 3, 5, 1, 1000};
            //    {1, 0, 3, 2, 4};
            {1, 2};
    private static final int[][] arrays = new int[][]{{1, 2, 3, 4},
            {1, 3, 7, 4},
            {1, 3, 7, 9, 4},
            {10000, 0, 6, 2, 4, 3, 5, 1, 1000},
            {1, 0, 3, 2, 4},
            {1, 2},
            {3, 4, 2, 1},
            {1, 3, 2, 4}
    };


    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            Flux.range(0, 10).buffer(10).subscribe(list -> {
                int[] array = shuffle(list.stream().mapToInt(Integer::intValue).toArray());
//                System.out.println("---|" + toString(array));
                insert(array);
                print(array);

            });
        }
       /* for (int i = 0; i < arrays.length; i++) {
            int[] array = copy(arrays[i]);
            fast(array, 0, array.length - 1);
            print(array);
        }*/


       /* 
        quickSort(array, 0, array.length - 1);
        print(array);*/

    }

    /**
     * 选择排序，每次产生一个最小值
     *
     * @param a
     * @param left
     * @param right
     */
    static void select(int[] a, int left, int right) {
        for (int i = 0; i < a.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[minIndex]) {
                    minIndex = j;
                }
            }
            if (i != minIndex)
                swap(a, i, minIndex);
        }
    }

    /**
     * 插入排序
     *
     * @param a
     */
    static void insert(int[] a) {
        int len = 1;
        for (int right = len; right < a.length; right++) {
            int target = a[right];
           if (target < a[right-1]) {
            int left = len - 1;
            for (; left >= 0; left--) {
                if (target > a[left]) {
                    break;
                }
                a[left + 1] = a[left];
            }
            a[left + 1] = target;
            }
            len++;
        }
    }

    static void quick(int[] a, int left, int right)//分治法 子问题求解
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

            quick(a, left, i - 1);

            quick(a, i + 1, right);
        }
    }

    /*
     * 快速排序
     *
     * 参数说明：
     *     a -- 待排序的数组
     *     l -- 数组的左边界(例如，从起始位置开始排序，则l=0)
     *     r -- 数组的右边界(例如，排序截至到数组末尾，则r=a.length-1)
     */
    public static void quickSort(int[] a, int left, int right) {

        if (left < right) {
            int i, j, x;

            i = left;
            j = right;
            x = a[left];
            while (i < j) {
                while (i < j && a[j] > x)
                    j--; // 从右向左找第一个小于x的数
                if (i < j)
                    a[i++] = a[j];
                while (i < j && a[i] < x)
                    i++; // 从左向右找第一个大于x的数
                if (i < j)
                    a[j--] = a[i];
            }
            a[i] = x;
            quickSort(a, left, i - 1); /* 递归调用 */
            quickSort(a, i + 1, right); /* 递归调用 */
        }
    }

    /**
     * @param a    target array
     * @param from start index, inclusive
     * @param to   end index, inclusive
     */
    public static void fast(int[] a, int from, int to) {

        if (from >= to || from < 0 || to >= a.length)
            return;


        int leftIndex = from;
        int rightIndex = to - 1;
        int originRightIndex = rightIndex;

        int keyValue = a[to];

        while (leftIndex < rightIndex) {
            while (leftIndex < rightIndex + 1 && a[leftIndex] < keyValue) {
                leftIndex++;
            }

            while (leftIndex < rightIndex && a[rightIndex] > keyValue) {
                rightIndex--;
            }

            if (leftIndex < rightIndex)
                swap(a, leftIndex, rightIndex);
        }


        if (a[leftIndex] > keyValue)
            swap(a, leftIndex, to);

//        System.out.printf("[%s] [%s] [%s]", toString(a, from, leftIndex - from), a[leftIndex], toString(a, leftIndex + 1, to - leftIndex));
//        System.out.println();

        fast(a, from, leftIndex - 1);
        //System.out.println("[%s]",);
        fast(a, leftIndex + 1, to);

    }

    public static void bubble() {
        int[] array = new int[src.length];
        System.arraycopy(src, 0, array, 0, src.length);

        int len = array.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int t = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = t;
                }
            }
            print(array);
        }
    }

    public static void print(int[] a) {
        System.out.println("--->" + Arrays.stream(a).mapToObj(p -> String.valueOf(p)).collect(Collectors.reducing((m, n) -> String.valueOf(m) + "," + String.valueOf(n))).orElse(""));
    }

    public static String toString(int[] a, int from, int len) {
        return (Arrays.stream(copy(a, from, len)).mapToObj(p -> String.valueOf(p)).collect(Collectors.reducing((m, n) -> String.valueOf(m) + "," + String.valueOf(n))).orElse(""));
    }

    public static String toString(int[] a) {
        return (Arrays.stream(copy(a, 0, a.length)).mapToObj(p -> String.valueOf(p)).collect(Collectors.reducing((m, n) -> String.valueOf(m) + "," + String.valueOf(n))).orElse(""));
    }

    /**
     * 复制数组<code>array</code>从from（包含）到to（不包含）的部分
     *
     * @param array
     * @param from
     * @param len
     * @return
     */
    public static int[] copy(int[] array, int from, int len) {
        if (array == null || array.length == 0 || len <= 0)
            return new int[0];
        Preconditions.checkState(from >= 0 && len <= array.length);
        int[] result = new int[len];
        System.arraycopy(array, from, result, 0, result.length);
        return result;
    }

    public static int[] copy(int[] array) {
        return copy(array, 0, array.length);
    }

    public static boolean swap(int[] array, int i, int j) {
        if (i != j) {
            int t = array[j];
            array[j] = array[i];
            array[i] = t;
        }
        return i != j;
    }

    private static int[] shuffle(int[] array) {
        int[] copy = Arrays.copyOf(array, array.length);
        Random random = new Random();
        int i = copy.length;
        while (--i > 0) {
            int j = random.nextInt(i);
            int temp = copy[i];
            copy[i] = copy[j];
            copy[j] = temp;
        }
        return copy;
    }
}
