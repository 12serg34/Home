package com.home.book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleFrame {

    JFrame frame;
    JButton colorButton, labelButton;
    JLabel label;
    MyDrawPanel drawPanel;

    public static void main(String[] args) {
        new SimpleFrame().run();
    }

    public void run() {
        colorButton = new JButton("Change color");
        colorButton.addActionListener(new ColorListener());

        labelButton = new JButton("Change label");
        labelButton.addActionListener(new LabelListener());

        drawPanel = new MyDrawPanel();
        label = new JLabel("I'm label");

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        Container container = frame.getContentPane();
        container.add(BorderLayout.SOUTH, colorButton);
        container.add(BorderLayout.CENTER, drawPanel);
        container.add(BorderLayout.WEST, label);
        container.add(BorderLayout.EAST, labelButton);
        frame.setVisible(true);
    }

    class LabelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            label.setText("Ouch!");
        }
    }

    class ColorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.repaint();
        }
    }
}
