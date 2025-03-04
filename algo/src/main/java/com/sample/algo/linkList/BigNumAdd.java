package com.sample.algo.linkList;

/**
 * 使用链表实现大数加法
 * 两个用链表代表的整数，其中每个节点包含一个数字。
 * 数字存储按照在原来整数中相反的顺序，使得第一个数字位于链表的开头。
 * 写出一个函数将两个整数相加，用链表形式返回和。
 *
 * 例如：
 * 输入：
 * 3->1->5->null
 * 5->9->2->null，
 * 输出：
 * 8->0->8->null
 */
public class BigNumAdd {
    public static void main(String[] args) {
        ListNode ee = new ListNode(5);
        ListNode dd = new ListNode(9, ee);
        ListNode cc = new ListNode(3, dd);
        ListNode bb = new ListNode(2, cc);
        ListNode aa = new ListNode(1, bb);
        System.out.println(aa);
        ListNode result = new BigNumAdd().run(aa, bb);
        // 12395 + 2395 = 35256
        System.out.println(result);
    }

    private ListNode run(ListNode aa, ListNode bb) {
        ListNode result = null;
        ListNode tmp = null;
        int addSlot = 0;
        while (aa != null || bb != null) {
            int item = addSlot;
            if (aa != null) {
                item = item + aa.val;
                aa = aa.next;
            }

            if (bb != null) {
                item = item + bb.val;
                bb = bb.next;
            }

            addSlot = item /10;

            if (result == null) {
                result = new ListNode(item % 10, tmp);
                tmp = result;
            } else {
                ListNode tmp11 = new ListNode(item % 10, null);
                tmp.next = tmp11;
                tmp = tmp11;
            }
        }
        return result;
    }
}
