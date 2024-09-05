package com.sample.juc.cyclicBarrier;

import java.util.HashMap;

/**
 * @author yunfei.jyf
 * @date 2024/9/4
 */
public class MyCyclicBarrierTest {

    private static StringBuffer result = new StringBuffer();
    private static final HashMap<String, Integer> resultMap = new HashMap<>();

    static MyCyclicBarrier cyclicBarrierStart = new MyCyclicBarrier(2, new Runnable() {
        @Override
        public void run() {
            result.append("3");
        }
    });
    static MyCyclicBarrier cyclicBarrierEnd = new MyCyclicBarrier(2);

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            MyCyclicBarrierTest.test();
            Integer count = resultMap.get(result.toString());
            if (count == null) {
                count = 1;
            } else {
                count = count + 1;
            }
            resultMap.put(result.toString(), count);

            result = new StringBuffer();
        }
        System.out.println(resultMap);
    }

    public static void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrierStart.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                result.append("2");

                try {
                    cyclicBarrierEnd.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        try {
            cyclicBarrierStart.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        result.append("1");

        try {
            cyclicBarrierEnd.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
