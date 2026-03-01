package com.csci2020;

import com.csci2020.frontend.Window;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            Window window = new Window();
            window.setVisible(true);
        });
    }
}
