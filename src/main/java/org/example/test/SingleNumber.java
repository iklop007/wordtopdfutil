package org.example.test;

/**
 * @ClassName SingleNumber
 * @Description TODO
 * @Author qlzcj
 * @Date 2026/3/23 15:32
 * @Version 1.0
 **/
public class SingleNumber {
    public static int singleNumber(int[] nums) {
        // 初始化结果为0，因为0和任何数异或不改变其值
        int result = 0;
        // 遍历数组，依次异或所有元素
        for (int num : nums) {
            result ^= num;
        }
        // 最终结果即为只出现一次的数字
        return result;
    }

    // 测试用例
    public static void main(String[] args) {
        int[] nums = {2, 2, 1};
        int single = singleNumber(nums);
        System.out.println("只出现一次的数字是：" + single); // 输出：1

        // 额外测试用例
        int[] nums2 = {4, 1, 2, 1, 2};
        System.out.println("只出现一次的数字是：" + singleNumber(nums2)); // 输出：4
    }
}
