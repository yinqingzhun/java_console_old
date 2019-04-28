package com.yqz.console.algorithm;


import com.google.common.base.Joiner;
import com.yqz.console.utils.StringHelper;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReservoirSamplingTest {
    private int poolSize = 1000;
    private int resultSize = 10;
    private Random random = new Random();

    public static void main(String[] args) throws Exception {

        int[] r = new ReservoirSamplingTest().sample();
        System.out.println(Arrays.stream(r).sorted().mapToObj(p -> String.valueOf(p)).collect(Collectors.joining(",")));

    }

    public int[] sample() {
        int[] pool = new int[poolSize];
        for (int i = 0; i < poolSize; i++) {
            pool[i] = i;
        }


        int[] result = new int[resultSize];
        for (int i = 0; i < resultSize; i++) {
            result[i] = pool[i];
        }


        for (int i = resultSize; i < poolSize; i++) {
            int m = random.nextInt(i + 1);
            if (m < resultSize) {
                result[m] = pool[i];
            }
        }

        return result;

    }

}