package com.home.hailstone.math;

import java.awt.*;
import java.util.Optional;

import static com.home.hailstone.math.Util.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class Calculator1 implements Calculator {
    @Override
    public Optional<Integer> calculateChild(int parent, int index) {
        if (parent % 3 == 1) {
            return empty();
        }
        int i = 2 * index + invertMod2(gamma(parent)) + 1;
        return of(reverse1(
                (
                        (forward1(parent) << i) - 1
                ) / 3
        ));
    }

    @Override
    public Color mapToColor(int value) {
        switch (value % 3) {
            case 0:
                return Color.RED;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.BLUE;
        }
        return Color.LIGHT_GRAY;
    }
}
