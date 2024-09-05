package com.sample.juc.exchanger;

import java.util.concurrent.Exchanger;

/**
 * @author yunfei.jyf
 * @date 2024/9/5
 */
public class ExchangerTest {
    static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message = "aaa";
                try {
                    String exchange = exchanger.exchange(message);
                    System.out.println(exchange);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String message = "bbb";
                try {
                    String exchange = exchanger.exchange(message);
                    System.out.println(exchange);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
