package com.sample.algo.linkList;

/**
 * @author yunfei.jyf
 * @date 2024/5/30
 */
public class OverturnLinkList {
    public static void main(String[] args) {
        ListNode ee = new ListNode(5);
        ListNode dd = new ListNode(4, ee);
        ListNode cc = new ListNode(3, dd);
        ListNode bb = new ListNode(2, cc);
        ListNode aa = new ListNode(1, bb);
        System.out.println(aa);
        ListNode result = new OverturnLinkList().run(aa);
        System.out.println(result);
    }

    private ListNode run(ListNode head) {
        ListNode newHead = null;
        while (head != null) {
            ListNode tmp = head;
            head = head.next;
            tmp.next = newHead;
            newHead = tmp;
        }
        return newHead;
    }
}
