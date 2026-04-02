package com.csci2020.frontend;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Tournament extends JPanel {
    private final Image image;

    public Tournament() {
        URL imageURL = getClass().getResource("/Tournament.jpg");

        if (imageURL == null) {
            throw new RuntimeException("Tournament.jpg not found in resources");
        }

        image = new ImageIcon(imageURL).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}