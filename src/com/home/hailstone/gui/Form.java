package com.home.hailstone.gui;

import com.home.hailstone.graph.Graph;
import com.home.hailstone.math.Calculator1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Form extends JFrame {
    private static final String GRAPHS_DIR = "graphs/";
    private final float SCALE_INCREMENT = 0.1f;

    private final BufferedImage bufferedImage;
    private final Graphics buffer;
    private Graph graph;
    private boolean isGraphMoving;

    public static void main(String[] args) {
        String path = "";
        if (args.length > 0) {
            path = args[0];
        }
        new Form(path);
    }

    private Form(String path) throws HeadlessException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point center = new Point(screenSize.width / 2, screenSize.height / 2);

        if (!path.isEmpty()) {
            loadGraphFromFile(path);
        } else {
//            graph = new Graph(1, new Calculator0(), center, 30, 75);
            graph = new Graph(0, new Calculator1(), center, 30, 75);
        }

        initGUI();
        bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        buffer = bufferedImage.getGraphics();
        refresh();
    }

    private void initGUI() {
        setTitle("MyForm");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(Color.BLACK);
        pack();
        setVisible(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                onMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                onMouseReleased(e);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                onMouseDragged(e);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                onKeyPressed(e);
            }
        });
    }

    private void onMousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            isGraphMoving = true;
            graph.dragFrom(e.getPoint());
        }
    }

    private void onMouseDragged(MouseEvent e) {
        if (isGraphMoving) {
            graph.dragTo(e.getPoint());
            refresh();
        }
    }

    private void onMouseReleased(MouseEvent e) {
        isGraphMoving = false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
        g.dispose();
    }

    private void onClick(MouseEvent e) {
        if (graph.tryToAddChild(e.getPoint())) {
            refresh();
        }
    }

    private void onKeyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_W:
                zoomIn(getMousePosition());
                break;
            case KeyEvent.VK_Q:
                zoomOut(getMousePosition());
                break;
            case KeyEvent.VK_S:
                saveToFile();
                break;
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream stream = new ObjectOutputStream(
                new FileOutputStream(GRAPHS_DIR + System.currentTimeMillis()))) {
            stream.writeObject(graph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGraphFromFile(String path) {
        try (ObjectInputStream stream = new ObjectInputStream(
                new FileInputStream(path))) {
            graph = (Graph) stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void zoomIn(Point base) {
        zoom(base, 1.0f + SCALE_INCREMENT);
    }

    private void zoomOut(Point base) {
        zoom(base, 1.0f - SCALE_INCREMENT);
    }

    private void zoom(Point base, float scaleIncrement) {
        graph.zoom(base, scaleIncrement);
        refresh();
    }

    private void refresh() {
        Graphics graphics = getGraphics();
        draw(graphics);
        graphics.dispose();
    }

    private void draw(Graphics baseGraphics) {
        buffer.clearRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graph.draw(buffer);
        baseGraphics.drawImage(bufferedImage, 0, 0, null);
    }
}
