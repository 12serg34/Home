package com.home.hailstone.math;

import java.util.Arrays;

public class SplitHierarchy {
    private final int size;
    private int[] diffDepths;
    private SplitHierarchy[] hierarchy;

    private SplitHierarchy(int size, int[] diffDepths, SplitHierarchy[] hierarchy) {
        this.size = size;
        this.diffDepths = diffDepths;
        this.hierarchy = hierarchy;
    }

    public static SplitHierarchy splitBy(int size) {
        return new SplitHierarchy(size, null, null);
    }

    public static SplitHierarchy splitBy(int size, int diffDepth) {
        int[] diffDepths = new int[size];
        Arrays.fill(diffDepths, diffDepth);
        return new SplitHierarchy(size, diffDepths, null);
    }

    public static SplitHierarchy splitBy(int... diffDepths) {
        return new SplitHierarchy(diffDepths.length, diffDepths, null);
    }

    public static SplitHierarchy splitBy(SplitHierarchy... hierarchy) {
        return new SplitHierarchy(hierarchy.length, null, hierarchy);
    }

    public static SplitHierarchy onlyDiff(int diffDepth) {
        return new SplitHierarchy(1, new int[]{diffDepth}, null);
    }

    public boolean isSimple() {
        return hierarchy == null;
    }

    public int getSize() {
        return size;
    }

    public SplitHierarchy getChild(int i) {
        return hierarchy[i];
    }

    public boolean hasDiffs() {
        return diffDepths != null;
    }

    public int getDiffDepth(int i) {
        return diffDepths[i];
    }
}
