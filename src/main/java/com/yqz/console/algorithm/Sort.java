package com.yqz.console.algorithm;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Sort {
    private static final int[] src = new int[]{10000, 0, 6, 2, 4, 3, 5, 1, 1000};

    public static void main(String[] args) {
        int[] array = copy(src);
        quickSort3(array, 0, array.length-1);
        print(array);

       /* 
        quickSort(array, 0, array.length - 1);
        print(array);*/

    }
    static void  quickSort3(int[] array,int left,int right)
    {
        if(left < right){
            int key = array[right];
            int cur = left;
            int pre = left - 1;
            while(cur < right)
            {
                while(array[cur] < key && ++pre != cur)//如果找到小于key的值，并且cur和pre之间有距离时则进行交换。注意两个条件的先后位置不能更换，可以参照评论中的解释
                {
                    swap(array,cur,pre);
                }
                ++cur;
            }

            swap(array,++pre,right);


            quickSort(array, left, pre - 1); /* 递归调用 */
            quickSort(array, pre + 1, right); /* 递归调用 */
             
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
    public static void quickSort(int[] a, int l, int r) {

        if (l < r) {
            int i, j, x;

            i = l;
            j = r;
            x = a[i];
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
            quickSort(a, l, i - 1); /* 递归调用 */
            quickSort(a, i + 1, r); /* 递归调用 */
        }
    }

    /**
     * @param a    target array
     * @param from start index, inclusive
     * @param to   end index, exclusive
     */
    public static void fast(int[] a, int from, int to) {

        if (from > to - 1 || from < 0 || to > a.length)
            return;


        int leftIndex = from;
        int rightIndex = to - 2;
        int originLeftIndex = leftIndex;
        int originRightIndex = rightIndex;

        int keyValue = a[to - 1];
        boolean swap = false;
        while (leftIndex < rightIndex) {
            while (leftIndex < rightIndex && a[leftIndex] < keyValue) {
                leftIndex++;
            }

            while (leftIndex < rightIndex && a[rightIndex] > keyValue) {
                rightIndex--;
            }

            if (swap(a, leftIndex, rightIndex)) {
                swap = true;
            }
        }

        if (!swap) {
            if (rightIndex == originRightIndex) {
                if (a[originRightIndex] < keyValue) {
                    //no swap
                    System.out.printf("[%s] [%s]", toString(a, from, to-1), keyValue); System.out.println();
                    fast(a, from, to - 1);
                    return;
                }
            }
        }


        swap(a, leftIndex, to - 1);

        System.out.printf("[%s] [%s] [%s]", toString(a, from, leftIndex), a[leftIndex], toString(a, leftIndex + 1, to));
        System.out.println();

        fast(a, from, leftIndex);
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
        System.out.println(Arrays.stream(a).mapToObj(p -> String.valueOf(p)).collect(Collectors.reducing((m, n) -> String.valueOf(m) + "," + String.valueOf(n))).orElse(""));
    }

    public static String toString(int[] a, int from, int to) {
        if (from >= to)
            return "";
        return (Arrays.stream(copy(a, from, to)).mapToObj(p -> String.valueOf(p)).collect(Collectors.reducing((m, n) -> String.valueOf(m) + "," + String.valueOf(n))).orElse(""));
    }

    /**
     * 复制数组<code>array</code>从from（包含）到to（不包含）的部分
     *
     * @param array
     * @param from
     * @param to
     * @return
     */
    public static int[] copy(int[] array, int from, int to) {
        if (array == null || array.length == 0 || from >= to || to > array.length)
            return new int[0];
        Preconditions.checkState(from >= 0 && to <= array.length && to > from);
        int[] result = new int[to - from];
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

}
