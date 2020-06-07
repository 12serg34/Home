package com.home.hailstone.drawmod;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawerModIndex {

    private BufferedImage image;
    private Graphics graphics;
    private Color[] colors;

    private final int MAX_VALUE;
    private final int COUNT_PICTURES;

    private String format;
    private int[][] koefs;

    {
        COUNT_PICTURES = 7;
        MAX_VALUE = (int) Math.pow(3, COUNT_PICTURES);
        format = "bmp";
        colors = new Color[]{Color.GREEN, Color.RED, Color.BLUE};
    }

    public static void main(String[] args) {
        DrawerModIndex drawer = new DrawerModIndex();
        drawer.init();
        drawer.run();
    }

    private void init() {
        koefs = new int[MAX_VALUE][MAX_VALUE];
    }

    private void run() {
        System.out.println("Application running...");
        calculateKoefs();

        for (int i = 0; i < COUNT_PICTURES; i++) {
            generatePicture(i);
        }
        System.out.println("Application stopped.");
    }

    private void calculateKoefs() {
        for (int i = 0; i < MAX_VALUE; i++) {
            koefs[i][0] = 1;
        }

        for (int j = 1; j < MAX_VALUE; j++) {
            for (int i = j; i < MAX_VALUE; i++) {
                koefs[i][j] = (koefs[i - 1][j] + koefs[i - 1][j - 1]) % MAX_VALUE;
            }
        }
    }

    private void generatePicture(int index) {
        image = new BufferedImage(MAX_VALUE, MAX_VALUE, BufferedImage.TYPE_INT_RGB);
        graphics = image.getGraphics();

        for (int i = 0; i < MAX_VALUE; i++) {
            for (int j = 0; j <= i; j++) {
                int degree = (int) Math.pow(3, index);
                int value = koefs[i][j] / degree % 3;
                drawPixel(i, j, value);
            }
        }
        graphics.dispose();
        saveToFile(index);
    }

    private void drawPixel(int i, int j, int value) {
        graphics.setColor(colors[value]);
        graphics.drawLine(j, i, j, i);
    }

    private void saveToFile(int index) {
        try {
            File outputFile = new File(index + "." + format);
            ImageIO.write(image, format, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}