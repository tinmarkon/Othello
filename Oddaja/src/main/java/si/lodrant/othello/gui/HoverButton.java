package si.lodrant.othello.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverButton extends JButton
    /* Gumb, ki se spremeni iz delno prosojnega v neprosojnega, ko se čezenj zapeljemo s kurzorjem.
    Razred povzet po tem: https://stackoverflow.com/a/22639355 odgovoru na StackOverflowu.*/ {
    float alpha = 0.8f;

    public HoverButton(String text, String size) {
        super(text);
        setFocusPainted(false);
        addMouseListener(new ML());
        Font buttonFont = new Font("Dialog", Font.PLAIN, 40);
        if (size.equals("small")) buttonFont = new Font("Dialog", Font.PLAIN, 15);
        else if (size.equals("medium")) buttonFont = new Font("Dialog", Font.PLAIN, 30);
        setFont(buttonFont);
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    public void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(52, 113, 235));
        super.paintComponent(g2);
    }

    public class ML extends MouseAdapter {
        public void mouseExited(MouseEvent me) {
            new Thread(new Runnable() {
                public void run() {
                    for (float i = 1f; i >= 0.8f; i -= .03f) {
                        setAlpha(i);
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                        }
                    }
                }
            }).start();
        }

        public void mouseEntered(MouseEvent me) {
            new Thread(new Runnable() {
                public void run() {
                    for (float i = 0.8f; i <= 1f; i += .03f) {
                        setAlpha(i);
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                        }
                    }
                }
            }).start();
        }

        public void mousePressed(MouseEvent me) {
            new Thread(new Runnable() {
                public void run() {
                    for (float i = 1f; i >= 0.6f; i -= .1f) {
                        setAlpha(i);
                        try {
                            Thread.sleep(1);
                        } catch (Exception e) {
                        }
                    }
                }
            }).start();
        }
    }
}