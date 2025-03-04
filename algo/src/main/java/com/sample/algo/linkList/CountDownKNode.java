package com.sample.algo.linkList;

/**
 * 寻找链表倒数第K个节点
 * 快慢指针
 * 快的指针先提前跑几步，然后开始一起跑
 */
public class CountDownKNode {
    public static void main(String[] args) {
        int k = 6;

        ListNode ee = new ListNode(5);
        ListNode dd = new ListNode(4, ee);
        ListNode cc = new ListNode(3, dd);
        ListNode bb = new ListNode(2, cc);
        ListNode aa = new ListNode(1, bb);
        System.out.println(aa);
        ListNode result = new CountDownKNode().run(aa, k);
        System.out.println(result);
    }

    private ListNode run(ListNode aa, int k) {
        ListNode fast = aa;
        ListNode slow = aa;

        for (int i = 1; i < k; i++) {
            if (fast.next == null) {
                return null;
            } else {
                fast = fast.next;
            }
        }
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }
}
