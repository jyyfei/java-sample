package com.sample.algo.linkList;

/**
 * 给定一个链表的头节点head和一个整数k，请将链表向右旋转k个位置，并返回旋转后的链表的头节点。
 * 核心是找到链表第length-k个位置变成head
 */
public class RevolveLinkList {
    public static void main(String[] args) {
        int k = 9;

        ListNode ee = new ListNode(5);
        ListNode dd = new ListNode(4, ee);
        ListNode cc = new ListNode(3, dd);
        ListNode bb = new ListNode(2, cc);
        ListNode aa = new ListNode(1, bb);
        System.out.println(aa);
        ListNode result = new RevolveLinkList().run(aa, k);
        System.out.println(result);
    }

    private ListNode run(ListNode aa, int k) {
        if (k == 0 || aa == null || aa.next == null) {
            return aa;
        }

        ListNode head = aa;
        ListNode tail = aa;
        int length = 1;
        while (tail.next != null) {
            length++;
            tail = tail.next;
        }
        tail.next = head;

        int kk = k % length;
        int i = length - kk;

        // 找到第i个就是newTail
        ListNode newTail = get(aa, i, tail);
        ListNode newHead = newTail.next;
        newTail.next = null;
        return newHead;
    }

    /**
     * 找到链表的第i个元素
     */
    private static ListNode get(ListNode aa, int i, ListNode tail) {
        ListNode tmp = aa;

        if (i == 0) {
            return tail;
        }
        for (int j = 1; j < i; j++) {
            tmp = tmp.next;
        }
        return tmp;
    }
}
