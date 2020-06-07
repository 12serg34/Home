package com.home.hailstone.graph;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Node implements Serializable {
    private final int level;
    private Point location;
    private List<Node> children;
    private double intervalStart;
    private double intervalEnd;
    private int value;

    Node(int level, int value) {
        this.value = value;
        this.level = level;
        this.children = new ArrayList<>(5);
        this.location = new Point();
    }

    void setLocation(Point location) {
        this.location = location;
    }

    Point getLocation() {
        return location;
    }

    void setInterval(double intervalStart, double intervalEnd) {
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;

        int size = children.size();
        double duration = intervalEnd - intervalStart;
        double childDuration = duration / size;
        for (int i = 0; i < size; i++) {
            children.get(i).setInterval(
                    intervalStart + childDuration * i,
                    intervalStart + childDuration * (i + 1));
        }
    }

    int getLevel() {
        return level;
    }

    double getAngle() {
        return (intervalStart + intervalEnd) / 2;
    }

    int getValue() {
        return value;
    }

    int getNextIndex() {
        return children.size();
    }

    void paintConnections(Graphics graphics) {
        graphics.setColor(Color.GREEN);
        children.forEach(node -> graphics.drawLine(location.x, location.y, node.location.x, node.location.y));
    }

    boolean checkPointInCircle(Point point, int radius) {
        return location.distance(point) < radius;
    }

    Node addChild(int value) {
        Node child = new Node(level + 1, value);
        children.add(child);
        setInterval(intervalStart, intervalEnd);
        return child;
    }
}
