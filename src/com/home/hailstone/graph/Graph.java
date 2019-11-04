package com.home.hailstone.graph;

import com.home.hailstone.math.Calculator;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Graph implements Serializable {
    private List<Node> nodes;
    private Calculator calculator;
    private Point center;
    private int nodeRadius;
    private int levelRadius;
    private transient Point dragFromPoint;
    private transient Point dragFromCenterPoint;

    public Graph(int rootValue, Calculator calculator, Point center, int nodeRadius, int levelRadius) {
        Node root = new Node(0, rootValue);
        root.setLocation(center);
        root.setInterval(0, 2 * Math.PI);

        this.calculator = calculator;
        this.center = center;
        this.nodeRadius = nodeRadius;
        this.levelRadius = levelRadius;
        this.nodes = new ArrayList<>(20);
        nodes.add(root);
        refresh();
    }

    public boolean tryToAddChild(Point point) {
        Optional<Node> parent = find(point);
        if (parent.isPresent()) {
            addChild(parent.get());
            return true;
        }
        return false;
    }

    private Optional<Node> find(Point point) {
        return nodes.parallelStream()
                .filter(node -> node.checkPointInCircle(point, nodeRadius))
                .findAny();
    }

    private void addChild(Node parent) {
        calculator.calculateChild(parent.getValue(), parent.getNextIndex())
                .ifPresent(childValue -> nodes.add(parent.addChild(childValue)));
        refresh();
    }

    public void dragFrom(Point from) {
        this.dragFromPoint = from;
        dragFromCenterPoint = (Point) center.clone();
    }

    public void dragTo(Point to) {
        center.setLocation(
                to.x - dragFromPoint.x + dragFromCenterPoint.x,
                to.y - dragFromPoint.y + dragFromCenterPoint.y);
        refresh();
    }

    public void zoom(Point base, float scaleIncrement) {
        center.x = (int) ((center.x - base.x) * scaleIncrement + base.x);
        center.y = (int) ((center.y - base.y) * scaleIncrement + base.y);
        levelRadius = (int) (levelRadius * scaleIncrement);
        refresh();
    }

    public void draw(Graphics graphics) {
        nodes.forEach(node -> node.paintConnections(graphics));
        nodes.forEach(node ->
                com.home.hailstone.draw.DrawUtil.drawCircleWithText(graphics, node.getLocation(), nodeRadius,
                        Integer.toString(node.getValue()), calculator.mapToColor(node.getValue()))
        );
    }

    private void refresh() {
        nodes.forEach(node -> {
            double angle = node.getAngle();
            int radius = node.getLevel() * levelRadius;
            node.setLocation(
                    new Point(
                            center.x + (int) (radius * Math.cos(angle)),
                            center.y - (int) (radius * Math.sin(angle))));
        });
    }
}
