package com.home.stepic.algorithm;

public class Thing {
    private int cost;
    private int volume = 1;
    private double relativeCost = Double.MIN_VALUE;

    public void setCost(int cost) {
        if (cost > -1)
            this.cost = cost;
    }
    public void setVolume(int volume) {
        if (volume > 0)
            this.volume = volume;
    }

    public int getCost() {
        return cost;
    }

    public int getVolume() {
        return volume;
    }

    public double getRelativeCost(){
        if (relativeCost == Double.MIN_VALUE)
            relativeCost = 1.0 * cost / volume;
        return relativeCost;
    }
}
