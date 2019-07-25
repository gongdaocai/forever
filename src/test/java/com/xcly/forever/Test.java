package com.xcly.forever;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Test {

//    public static void main(String[] args) {
//
//        StackOnLinkedList stack = new StackOnLinkedList();
//        stack.push(1);
//        System.out.println(stack.getSize());
//
//        Object pop = stack.pop();
//
//        stack.push(2);
//        System.out.println(stack.getSize());
//
//        stack.push(3);
//        System.out.println(stack.getSize());
//
//
//        Object pop1 =stack.pop();
//
//        stack.push(5);
//
//        System.out.println(stack.getSize());
//    }

//    public static void main(String[] args) {
//        int[] scores = {90, 70, 50, 80, 60, 85};
//        sortScore(scores);
//        for (int score : scores) {
//            System.out.print(score + ",");
//        }
//    }

//    private static void sortScore(int[] scores) {
//        int length = scores.length;
//        int length1 = length - 1;
//        int temp;
//        int compareCount = 0;
//        boolean isExchanged = false;
//        int childCount = 0;
//        for (int i = 0; i < length; i++) {
//            for (int j = 0; j < length1; j++) {
//                if (scores[j] > scores[j + 1]) {
//                    temp = scores[j];
//                    scores[j] = scores[j + 1];
//                    scores[j + 1] = temp;
//                    isExchanged = true;
//                    childCount = j;
//                }
//                compareCount++;
//            }
//            if (!isExchanged) {
//                return;
//            }
//            isExchanged = false;
//            length1 = childCount;
//            System.out.print("第" + (i + 1) + "次排序  结果:");
//            for (int score : scores) {
//                System.out.print(score + ",");
//            }
//            System.out.println("此次比较次数 " + compareCount);
//            compareCount = 0;
//        }
//    }

    public static void main(String[] args) {
        int[] nums = {2,4,5,7};
        int[] ints = twoSum(nums, 6);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> tuples = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i - 1] == nums[i]) continue; //去重

            int l = i + 1, r = nums.length - 1;
            if (nums[l] < 0 && Integer.MIN_VALUE - nums[l] > nums[i]) continue; //如果溢出最小值则跳过
            if (nums[i] > 0 && Integer.MAX_VALUE - nums[l] < nums[i]) break; //溢出最大值直接结束，不可能会有新的三元组出现了

            while (l < r) {
                if (nums[r] > -nums[i] - nums[l]) {
                    while (l < r && nums[r - 1] == nums[r]) r--; //右指针去重
                    r--;
                } else if (nums[r] < -nums[i] - nums[l]) {
                    while (l < r && nums[l + 1] == nums[l]) l++; //左指针去重
                    l++;
                } else {
                    tuples.add(Arrays.asList(nums[i], nums[l], nums[r]));
                    while (l < r && nums[r - 1] == nums[r]) r--; //左指针去重
                    while (l < r && nums[l + 1] == nums[l]) l++; //右指针去重
                    r--;
                    l++;
                }
            }
        }
        return tuples;
    }


    public static boolean validate(String s) {
//        long begin = System.currentTimeMillis();
//        while (s.contains("()") || s.contains("{}") || s.contains("[]")) {
//            s = s.replace("{}", "").replace("()", "").replace("[]", "");
//        }
//        System.out.println(s.equals(""));
//
//        System.out.println(System.currentTimeMillis() - begin);

        Stack<Character> stack = new Stack<>();
        for (char aChar : s.toCharArray()) {
            if (aChar == '(' || aChar == '[' || aChar == '{') {
                stack.push(aChar);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                Character pop = stack.pop();
                boolean a = (pop == '(' && aChar != ')');
                boolean b = (pop == '[' && aChar != ']');
                boolean c = (pop == '{' && aChar != '}');
                if (a || b || c) {
                    return false;
                }
            }
        }
        if (!stack.isEmpty()) {
            return false;
        }
        return true;
    }

    public static int searchInsert(int[] nums, int target) {

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= target) {
                return i;
            }
        }
        return nums.length;

    }


    public static int[] twoSum(int[] nums, int target) {
//        Map<Integer, Integer> map = new HashMap<>();
//        for (int i = 0; i < nums.length; i++) {
//            int x = nums[i];
//            if (map.containsKey(target - x)) {
//                return new int[] { map.get(target - x), i };
//            }
//            map.put(x, i);
//        }
//        return new int[] { 0, 0 };

        Arrays.sort(nums);

        int b = nums.length - 1;

        for (int a = 0; a <= b; ) {
            if (nums[a] + nums[b] < target) {
                a = a + 1;
            } else if (nums[a] + nums[b] > target) {
                b = b - 1;
            } else {
                return new int[]{a, b};
            }
        }
        return new int[]{0, 0};
    }


}
