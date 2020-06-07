package com.home.hailstone.math;

import java.awt.*;
import java.io.Serializable;
import java.util.Optional;

public interface Calculator extends Serializable {
    Optional<Integer> calculateChild(int parent, int index);
    default Color mapToColor(int value) {
        return Color.GREEN;
    }
}
