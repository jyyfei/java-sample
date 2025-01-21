package com.java.sample.interview.eleme;

/**
 * 问题：在未排序的数组中找到第 k 个最大的元素。
 * 注意: 需要找的是数组排序后的第 k 个最大的元素，
 * 而不是第 k 个不同的元素。??
 */
public class KMax {
    public static void main(String[] args) {
        int[] aa = new int[]{0, 45, 34, 22, 11, 22};
        System.out.printf(String.valueOf(new KMax().findKthLargest(aa, 6)));
    }

    public int findKthLargest(int[] nums, int k) {
        for (int i = 1; i <= k; i++) {
            setSoltBySort(nums, i - 1);
        }
        return nums[k - 1];
    }

    public void setSoltBySort(int[] nums, int solt) {
        // 找最大的一个
        int length = nums.length;
        int max = nums[solt];
        int maxIndex = solt;
        for (int i = solt; i < length; i++) {
            if (max < nums[i]) {
                max = nums[i];
                maxIndex = i;
            }
        }
        // 交换到前面
        swap(nums, solt, maxIndex);
    }

    public void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
