package com.sample.juc.cyclicBarrier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author yunfei.jyf
 * @date 2024/9/4
 */
public class CyclicBarrierTest {

    private static StringBuffer result = new StringBuffer();
    private static final HashMap<String, Integer> resultMap = new HashMap<>();

    static CyclicBarrier cyclicBarrierStart = new CyclicBarrier(2, new Runnable() {
        @Override
        public void run() {
            result.append("3");
        }
    });
    static CyclicBarrier cyclicBarrierEnd = new CyclicBarrier(2);

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            CyclicBarrierTest.test();
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
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

                result.append("2");

                try {
                    cyclicBarrierEnd.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        try {
            cyclicBarrierStart.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        result.append("1");

        try {
            cyclicBarrierEnd.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
