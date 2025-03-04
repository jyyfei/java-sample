package com.sample.algo.linkList;

/**
 * 寻找链表的中间节点，如果有两个中间节点则返回第二个
 * 快慢指针，只需要遍历一次链表
 */
public class MiddleNode {
    public static void main(String[] args) {
        ListNode ee = new ListNode(5);
        ListNode dd = new ListNode(4, ee);
        ListNode cc = new ListNode(3, dd);
        ListNode bb = new ListNode(2, cc);
        ListNode aa = new ListNode(1, bb);
        System.out.println(aa);
        ListNode result = new MiddleNode().run(aa);
        System.out.println(result);
    }

    private ListNode run(ListNode aa) {
        ListNode fast = aa;
        ListNode slow = aa;
        while (fast.next != null) {
            if (fast.next.next != null) {
                fast = fast.next.next;
            } else {
                fast = fast.next;
            }

            slow = slow.next;
        }
        return slow;
    }
}
