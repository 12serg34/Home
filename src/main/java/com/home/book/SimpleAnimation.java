package com.home.book;

import javax.swing.*;
import java.awt.*;

public class SimpleAnimation {
    int x, y;
    MyPanel panel;

    public static void main(String[] args){
        new SimpleAnimation().run();
    }

    private void run(){
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new MyPanel();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        runAnimation();
    }

    private void runAnimation(){
        int x0 = 10, y0 = 10;
        int h = 4;
        int xEnd = 400;
        int timePause = 100;
        while (x < xEnd){
            x+= h;
            y+=h;
            panel.repaint();
            try {
                Thread.sleep(timePause);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private class MyPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0,0,panel.getWidth(), panel.getHeight());
            g.setColor(Color.green);
            g.fillOval(x,y, 40, 40);
        }
    }
}
