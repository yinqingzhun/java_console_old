package com.yqz.console.tech.algorithm;

import java.util.*;
import java.util.stream.Collectors;

public class Composite {

    /**
     * 一个数组的元素总共有多少种排列组合
     */

    public static List<String[]> composite(String[] array) {
        if (array == null || array.length == 0)
            return Collections.emptyList();

        if (array.length == 1) {
            return new ArrayList<String[]>() {{
                add(array);
            }};
        } else {
            final String first = array[0];
            List<String[]> mix = composite(Arrays.copyOfRange(array, 1, array.length));
            List<String[]> result = new ArrayList<>();
            mix.forEach(source -> {
                for (int i = 0; i <= source.length; i++) {
                    String[] target = new String[source.length + 1];
                    result.add(target);

                    for (int j = 0; j < source.length + 1; j++) {
                        if (j == i) {
                            target[i] = first;
                        } else if (j < i) {
                            target[j] = source[j];
                        } else {
                            target[j] = source[j-1];
                        }

                    }
                }
            });
            return result;
        }
    }

    public static void main(String[] args) {
        Set<String> stringSet = new HashSet<>();
        composite(new String[]{"a", "b", "c", "d"}).forEach(p -> {
            String s = Arrays.stream(p).collect(Collectors.joining());
            System.out.println(s);
            stringSet.add(s);
        });

        System.out.println(stringSet.size());
    }


}
