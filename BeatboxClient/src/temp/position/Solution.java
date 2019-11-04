package temp.position;

/*
Find First and Last Position of Element in Sorted Array
 */

class Solution {
    private final int[] emptyRange = {-1, -1};

    private int[] nums;
    private int t;

    public int[] searchRange(int[] nums, int target) {
        this.nums = nums;
        this.t = target;
        return searchRange(0, nums.length - 1);
    }

    private int[] searchRange(int l, int r) {
        if (l > r) {
            return emptyRange;
        }

        if (l == r) {
            if (nums[l] == t) {
                return new int[]{l, r};
            }
            return emptyRange;
        }

        int m = (l + r) >> 1;
        if (nums[m] > t) {
            return searchRange(l, m);
        }

        if (nums[m] < t) {
            return searchRange(m + 1, r);
        }

        int[] leftRange = searchRange(l, m - 1);
        int[] rightRange = searchRange(m + 1, r);
        int[] result = {m, m};
        if (leftRange != emptyRange) {
            result[0] = leftRange[0];
        }
        if (rightRange != emptyRange) {
            result[1] = rightRange[1];
        }
        return result;
    }
}

class Test {
    public static void main(String[] args) {
        int[] nums = {5,7,7,8,8,10};
        new Solution().searchRange(nums, 8);
    }
}