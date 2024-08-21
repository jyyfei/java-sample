package com.java.sample.interview.ant;

import java.util.concurrent.CompletableFuture;

/**
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrint1 {
    private void aa() {
        System.out.println("aa");
    }

    private void bb() {
        System.out.println("bb");
    }

    private void cc() {
        System.out.println("cc");
    }

    public static void main(String[] args) {
        OrderPrint1 orderPrint1 = new OrderPrint1();
        for (int i = 0; i < 10; i++) {
            System.out.println("************************************");
            try {
                CompletableFuture<Void> aa = CompletableFuture.runAsync(orderPrint1::aa);
                aa.get();
                CompletableFuture<Void> bb = CompletableFuture.runAsync(orderPrint1::bb);
                bb.get();
                CompletableFuture<Void> cc = CompletableFuture.runAsync(orderPrint1::cc);
                cc.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
