package org.example.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {
    public static int[][] merge(int[][] intervals) {
        // 处理边界情况：空数组或只有一个区间
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }

        // 步骤1：按区间起始值升序排序（时间复杂度 O(n log n)）
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        // 步骤2：合并重叠区间
        List<int[]> merged = new ArrayList<>();
        // 先将第一个区间加入结果列表
        merged.add(intervals[0]);

        for (int i = 1; i < intervals.length; i++) {
            // 获取结果列表最后一个区间
            int[] last = merged.get(merged.size() - 1);
            int[] current = intervals[i];

            // 判断是否重叠：当前区间的起始值 <= 最后一个区间的结束值
            if (current[0] <= last[1]) {
                // 重叠则合并，更新最后一个区间的结束值（取最大值）
                last[1] = Math.max(last[1], current[1]);
            } else {
                // 不重叠则直接加入结果列表
                merged.add(current);
            }
        }

        // 将List转换为二维数组返回
        return merged.toArray(new int[merged.size()][]);
    }

    // 测试用例
    public static void main(String[] args) {
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] result = merge(intervals);

        // 打印结果
        System.out.print("合并后的区间：");
        for (int[] interval : result) {
            System.out.print("[" + interval[0] + "," + interval[1] + "] ");
        }
        // 输出：[1,6] [8,10] [15,18]
    }
}
