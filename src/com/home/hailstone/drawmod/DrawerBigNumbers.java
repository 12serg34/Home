package com.home.hailstone.drawmod;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class DrawerBigNumbers {

    private BufferedImage image;
    private Graphics graphics;
    private Color[] colors;

    private final int WIDTH;
    private final int HEIGHT;
    private final int COUNT_PICTURES;

    private String format;
    private BigInteger[][] koefs;

    {
        WIDTH = 729;
        HEIGHT = 729;
        COUNT_PICTURES = 10;
        format = "bmp";
        colors = new Color[]{Color.GREEN, Color.RED, Color.BLUE};
    }

    public static void main(String[] args) {
        DrawerBigNumbers drawer = new DrawerBigNumbers();
        drawer.init();
        drawer.run();
    }

    private void init() {
        koefs = new BigInteger[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                koefs[i][j] = BigInteger.ZERO;
            }
        }
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
        for (int i = 0; i < HEIGHT; i++) {
            koefs[i][0] = BigInteger.ONE;
        }

        for (int j = 1; j < WIDTH; j++) {
            for (int i = j; i < HEIGHT; i++) {
                koefs[i][j] = koefs[i - 1][j].add(koefs[i - 1][j - 1]);
            }
        }
    }

    private void generatePicture(int index) {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        graphics = image.getGraphics();

        BigInteger degree = BigInteger.valueOf((long)Math.pow(3, index));
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j <= i; j++) {
                int value = koefs[i][j].divide(degree).mod(BigInteger.valueOf(3L)).intValue();
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