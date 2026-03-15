package com.csci2020;

import com.csci2020.backend.Database;
import com.csci2020.backend.Player;
import com.csci2020.frontend.Window;

import javax.swing.*;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args){
        Database db = new Database(Path.of(".","database"));
        Player me = db.getPlayerByUsername("Daniel");
        System.out.println(me);
        SwingUtilities.invokeLater(()->{
            Window window = new Window(db);
            window.setVisible(true);
        });
    }
}
