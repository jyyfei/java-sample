package com.sample.algo.linkList;

import java.util.Objects;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        ListNode aa = this;
        while (Objects.nonNull(aa)) {
            stringBuffer.append(aa.val);
            aa = aa.next;
            if (Objects.nonNull(aa)) {
                stringBuffer.append("->");
            }
        }
        return stringBuffer.toString();
    }
}