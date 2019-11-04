package com.home.hailstone.math;

import java.awt.*;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class Calculator0 implements Calculator {
    @Override
    public Optional<Integer> calculateChild(int parent, int index) {
        if (parent % 2 == 0 && parent % 3 == 1) {
            if (index == 0) {
                return of((parent - 1) / 3);
            }
            if (index == 1) {
                return of(parent << 1);
            }
        }
        if (index == 0) {
            return of(parent << 1);
        }
        return empty();
    }

    @Override
    public Color mapToColor(int value) {
        switch (value % 2) {
            case 0:
                return Color.RED;
            case 1:
                return Color.GREEN;
        }
        return Color.LIGHT_GRAY;
    }
}
