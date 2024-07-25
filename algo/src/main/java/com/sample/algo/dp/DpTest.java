package com.sample.algo.dp;

/**
 * 动态规划
 *
 * @author yunfei.jyf
 * @date 2024/3/12
 */
public class DpTest {
    public static void main(String[] args) {
        int result = nn(3);
        System.out.println(result);
    }

    private static int nn(int n) {
        int a = 1;
        int b = 2;
        int nn = 0;
        for (int i = 3; i <= n; i++) {
            nn = a + b;
            a = b;
            b = nn;
        }
        return nn;
    }
}
