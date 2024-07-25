package com.sample.algo.test;

/**
 * 输入：s = "abpcplea", dictionary = ["ale","apple","monkey","plea"]
 * 输出："apple"
 *
 * @author yunfei.jyf
 * @date 2024/4/23
 */
public class SelectMax {
    public static void main(String[] args) {
        String[] dictionary = new String[]{"ale", "apple", "monkey", "plea"};
        String s = "abpcplea";
        String select = null;
        for (String string : dictionary) {
            boolean selectResult = isSelect(s, string);
            if (selectResult) {
                if (select == null) {
                    select = string;
                } else if (select.length() < string.length()) {
                    select = string;
                }
            }
        }
        System.out.println(select);
    }

    private static boolean isSelect(String s, String dict) {
        char[] sArray = s.toCharArray();
        char[] dictArray = dict.toCharArray();
        int item = 0;
        for (int ii = 0; ii < dictArray.length; ii++) {
            char dictChar = dictArray[ii];
            for (int i = item; i < sArray.length; i++) {
                if (sArray[i] == dictChar) {
                    item = i + 1;
                    break;
                }
            }
            if (item == 0) {
                return false;
            }
            if (item == sArray.length && ii != dictArray.length -1) {
                return false;
            }
        }
        return true;
    }
}
