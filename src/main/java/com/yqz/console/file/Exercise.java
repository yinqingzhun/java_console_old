package com.yqz.console.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Exercise {
    public static void main(String[] args) {
        Exercise exercise = new Exercise();
        for (int i = 0; i < 1000; i++) {
            String s = exercise.generate(3, Operator.multiply, Operator.divide);
            System.out.println(s + "=");
        }

    }

    private String generate(int size, Operator... operators) {
        if (operators == null || operators.length == 0 || size < 2)
            throw new IllegalArgumentException();

        int result = -1;
        Random random = new Random();
        List<Integer> vs = new ArrayList<>();
        List<Operator> os = new ArrayList<>();
        while (vs.size() < size) {
            Operator op = operators[random.nextInt(operators.length)];
            os.add(op);
            switch (op) {
                case add:
                    if (result >= 0) {
                        vs.add(random.nextInt(100));
                    }
                    break;
                case subtract:
                    break;
                case multiply:
                    if (result >= 0) {
                        if (result > 10) {
                            vs.add(random.nextInt(2));
                        } else {
                            vs.add(random.nextInt(10));
                        }
                        result = result * vs.get(vs.size() - 1);
                    } else {
                        vs.add(random.nextInt(10));
                        vs.add(random.nextInt(10));
                        result = vs.get(vs.size() - 2) * vs.get(vs.size() - 1);
                    }
                    break;
                case divide:
                    if (result >= 0) {
                        if (result == 0) {
                            vs.add(random.nextInt(100));
                        } else if (result == 1) {
                            vs.add(1);
                        } else {
                            int maxIndex = 10;
                            int index = 0;
                            int lastIndex = 0;
                            for (int i = 2; i < result; i++) {
                                if (result % i == 0) {
                                    int srcIndex = index;
                                    index = i;
                                    if (srcIndex > 0)
                                        lastIndex = srcIndex;

                                    if (index >= maxIndex) {
                                        break;
                                    }
                                }
                            }

                            if (index > 0) {
                                if (index > maxIndex)
                                    vs.add(lastIndex);
                                else
                                    vs.add(index);
                            } else {


                                if (System.currentTimeMillis() % 2 == 0) {
                                    vs.add(1);
                                } else {
                                    vs.add(result);
                                }
                            }
                        }

                        try {
                            result = result / vs.get(vs.size() - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        int m = random.nextInt(10);
                        int n = random.nextInt(10);

                        if (m == 0 || n == 0) {
                            n = 1;
                        }


                        int z = m * n;
                        vs.add(z);
                        if (z == 0) {
                            vs.add(Math.max(m, n));
                        } else {
                            vs.add(m);
                        }

                        try {
                            result = vs.get(vs.size() - 2) / vs.get(vs.size() - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }

        }


        return makeExpression(vs, os);
    }

    private String makeExpression(List<Integer> vs, List<Operator> os) {
        if (vs == null || os == null)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < os.size(); i++) {
            stringBuilder.append(vs.get(i)).append(os.get(i).getSymbol());
        }
        if (vs.size() > 0)
            stringBuilder.append(vs.get(vs.size() - 1));
        return stringBuilder.toString();
    }


    enum Operator {
        add("+", 0x1), subtract("-", 0x2), multiply("×", 0x4), divide("÷", 0x8);

        private String symbol;
        private int id;

        Operator(String symbol, int id) {
            this.symbol = symbol;
            this.id = id;
        }

        public String getSymbol() {
            return symbol;
        }
    }


}
