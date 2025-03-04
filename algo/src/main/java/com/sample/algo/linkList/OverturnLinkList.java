package com.sample.algo.linkList;

/**
 * 翻转链表解题思路
 * 设置三个节点pre、cur、next
 * （1）每次查看cur节点是否为NULL，如果是，则结束循环，获得结果
 * （2）如果cur节点不是为NULL，则先设置临时变量next为cur的下一个节点
 * （3）让cur的下一个节点变成指向pre，而后pre移动cur，cur移动到next
 * （4）重复（1）（2）（3）
 *
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

        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;

            cur.next = newHead;

            newHead = cur;

            cur = next;
        }
        return newHead;
    }
}
