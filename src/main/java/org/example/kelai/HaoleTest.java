package org.example.kelai;

public class HaoleTest {

    /**
     * 查找数组中唯一出现一次的数字
     * @param nums 输入数组
     * @return 唯一出现一次的数字，如果没有则抛出异常
     * @throws IllegalArgumentException 当没有唯一数字时抛出
     */
    public static int findSingleNumber(int[] nums) throws IllegalArgumentException {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }

        int result = 0;
        for (int num : nums) {
            result ^= num;
        }

        // 检查是否真的存在唯一数字
        int count = 0;
        for (int num : nums) {
            if (num == result) {
                count++;
            }
        }

        if (count != 1) {
            throw new IllegalArgumentException("找不到");
        }

        return result;
    }

    public static void main(String[] args) {
        int[] test1 = {1, 2, 5, 5, 6, 2, 1};
        int[] test2 = {-1, 2, -1, 2};

        try {
            System.out.println(findSingleNumber(test1)); // 输出5
            System.out.println(findSingleNumber(test2)); // 抛出异常
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
