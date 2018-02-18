package com.home.book;

import java.awt.*;
import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.ShortMessage;
import javax.swing.*;

public class MyDrawPanel extends JPanel implements ControllerEventListener{
    boolean message = false;

    protected void paintComponent(Graphics g){
//        g.setColor(Color.green);
//        g.drawLine(100, 100, 300, 400);
//
//        Image image = new ImageIcon("C:\\MyData\\Памятные фото и видео\\Котик.jpg").getImage();
//        g.drawImage(image, 0,0, this);
//
//        Color randomColor = getRandomColor();
//        g.setColor(randomColor);
//
// Graphics2D g2d = (Graphics2D)g;
//        GradientPaint gradient = new GradientPaint(
//                70, 70, getRandomColor(),
//                150, 150, getRandomColor());
//        g2d.setPaint(gradient);
//        g.fillRoundRect(10, 10, 300, 200, 30, 30);

        if (message){
            Graphics2D graphics2D = (Graphics2D)g;
            g.setColor(getRandomColor());
            int width = (int)(190*Math.random()) + 10;
            int height = (int)(190*Math.random()) + 10;
            int x = (int)(300*Math.random());
            int y = (int)(300*Math.random());
            graphics2D.fillRect(x,y,width, height);
            message = false;
        }
    }

    private Color getRandomColor(){
        return new Color(
                (int)(Math.random() * 256),
                (int)(Math.random() * 256),
                (int)(Math.random() * 256));
    }

    @Override
    public void controlChange(ShortMessage event) {
        message = true;
        this.repaint();
    }
}