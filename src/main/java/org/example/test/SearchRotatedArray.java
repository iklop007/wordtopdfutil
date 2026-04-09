package org.example.test;

public class SearchRotatedArray {
    public static int search(int[] nums, int target) {
        // 处理边界情况：数组为空
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0;
        int right = nums.length - 1;

        // 二分查找核心逻辑
        while (left <= right) {
            // 计算中间下标（避免 left+right 溢出）
            int mid = left + (right - left) / 2;

            // 找到目标值，直接返回下标
            if (nums[mid] == target) {
                return mid;
            }

            // 情况1：左半区间 [left, mid] 是有序的
            if (nums[left] <= nums[mid]) {
                // 目标值在左半有序区间内，缩小右边界
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    // 目标值不在左半区间，缩小左边界
                    left = mid + 1;
                }
            }
            // 情况2：右半区间 [mid, right] 是有序的
            else {
                // 目标值在右半有序区间内，缩小左边界
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    // 目标值不在右半区间，缩小右边界
                    right = mid - 1;
                }
            }
        }

        // 遍历结束未找到目标值，返回-1
        return -1;
    }

    // 测试用例
    public static void main(String[] args) {
        int[] nums = {4, 5, 6, 7, 0, 1, 2};
        int target = 0;
        int index = search(nums, target);
        System.out.println("目标值的下标是：" + index); // 输出：4

        // 额外测试用例
        int target2 = 7;
        System.out.println("目标值7的下标是：" + search(nums, target2)); // 输出：3
        int target3 = 3;
        System.out.println("目标值3的下标是：" + search(nums, target3)); // 输出：-1
    }
}
