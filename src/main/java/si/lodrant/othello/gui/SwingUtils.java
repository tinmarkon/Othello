package si.lodrant.othello.gui;

import javax.swing.*;
import java.awt.*;

public class SwingUtils {
    public static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = SwingUtils.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void nastaviLookAndFeel() {
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println("Ne najdem prave teme!");
        }
    }

}
