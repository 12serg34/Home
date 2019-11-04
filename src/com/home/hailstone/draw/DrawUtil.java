package com.home.hailstone.draw;

import java.awt.*;

public class DrawUtil {
    public static void drawCircleWithText(Graphics graphics, Point center, int radius,
                                          String text, Color color) {
        graphics.setColor(color);
        Rectangle oval = new Rectangle(
                (center.x - radius), (center.y - radius),
                (2 * radius), (2 * radius));
        graphics.fillOval(oval.x, oval.y, oval.width, oval.height);

        graphics.setColor(Color.BLACK);
        drawCenteredString(graphics, text, oval, graphics.getFont());
    }

    private static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }
}
