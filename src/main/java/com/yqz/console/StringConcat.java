package com.yqz.console;

public class StringConcat {
    public static void main(String[] args) {
        int count=300000;
        plus(count);
        concat(count);
       join(count);
        stringBuilder(count);

//        count=300000;
//        plus(count);
//        concat(count);
//        join(count);
//        stringBuilder(count);
//
//        count=1000000;
//        plus(count);
//        concat(count);
//        join(count);
//        stringBuilder(count);
    }
    public static void join(int count) {
       String[] array=new String[count];
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            array[i]= "a";
        }
        String.join("",array);
        long end = System.currentTimeMillis();
        System.out.println("join:" + (end - start));
        System.out.println();
    }
    public static void plus(int count) {
        String initial = "";
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            initial = initial + "a";
        }
        long end = System.currentTimeMillis();
        System.out.println("plus:" + (end - start));

//        initial = "";
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++) {
//            initial += String.valueOf(i);
////            a = a + String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + String.valueOf(i);
//        }
//        end = System.currentTimeMillis();
//        System.out.println("double plus:" + (end - start));
        System.out.println();
    }

    public static void stringBuilder(int count) {
        StringBuilder initial = new StringBuilder("");
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            initial = initial.append("b");
        }
        long end = System.currentTimeMillis();
        System.out.println("StringBuilder:" + (end - start));

//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++) {
//            StringBuilder b = new StringBuilder("b");
//            b.append(String.valueOf(i));
//            //b.append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i));
//        }
//        end = System.currentTimeMillis();
//        System.out.println("double StringBuilder:" + (end - start));
        System.out.println();
    }

    public static void stringBuffer() {
        StringBuffer initial = new StringBuffer("");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            initial = initial.append("c");
        }
        long end = System.currentTimeMillis();
        System.out.println("StringBuffer:" + (end - start));

//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++) {
//            StringBuffer c = new StringBuffer("c");
//            c.append(String.valueOf(i));
//            //c.append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i)).append(String.valueOf(i));
//        }
//        end = System.currentTimeMillis();
//        System.out.println("double StringBuffer:" + (end - start));
        System.out.println();
    }

    public static void concat(int count) {
        String initial = "";
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            initial = initial.concat("d");
        }
        long end = System.currentTimeMillis();
        System.out.println("concat:" + (end - start));

//        start = System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++) {
//            String d = "d";
//            d = d.concat(String.valueOf(i));
//            // d = //d.concat(String.valueOf(i)).concat(String.valueOf(i)).concat(String.valueOf(i)).concat(String.valueOf(i)).concat(String.valueOf(i)).concat(String.valueOf(i)).concat(String.valueOf(i)).concat(String.valueOf(i));
//        }
//        end = System.currentTimeMillis();
//        System.out.println("double concat:" + (end - start));

        System.out.println();
    }
}
