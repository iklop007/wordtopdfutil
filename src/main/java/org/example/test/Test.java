package org.example.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Test
 * @Description TODO
 * @Author qlzcj
 * @Date 2026/3/31 21:16
 * @Version 1.0
 **/
public class Test {

    public static List<String> getnerateCombinations(char[] arr){
        List<String> result = new ArrayList<>();
        int n = arr.length;
        int total = 1 << n;

        // 从1 开始跳过空集
        for (int i = 1; i < total; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                // 检查第 j 位是否为1
                if ((i & (1 << j)) != 0) {
                    if (sb.length() > 0) {
                        sb.append("-");
                    }
                    sb.append(arr[j]);
                }
            }
            result.add(sb.toString());
        }
        return result;
    }
    public static void main(String[] args) {
        char[] arr = {'a', 'b', 'c', 'd'}; //a,b,c,d
        List<String> combinations = getnerateCombinations(arr);
        for (String combination : combinations) {
            System.out.println(combination);
        }

        System.out.println(combinations.size());
    }

}
