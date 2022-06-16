package si.lodrant.othello.gui;

import javax.swing.*;
import java.awt.*;

public class MojToggleButton extends JToggleButton {
    /* Gumb, ki se spremeni iz delno prosojnega v neprosojnega, ko se čezenj zapeljemo s kurzorjem.
    Razred povzet po tem: https://stackoverflow.com/a/22639355 odgovoru na StackOverflowu.*/

    float alpha = 0.8f;
    boolean pressed;

    public MojToggleButton(String text, boolean selected, String size) {
        super(text);
        //setFocusPainted(true);
        //addMouseListener(new ML());
        setSelected(selected);
        setAlpha((this.isSelected()) ? 1f : 0.8f);
        addActionListener((e) -> {
            setAlpha((this.isSelected()) ? 1f : 0.8f);
        });
        pressed = selected;
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

    public void paintComponent(java.awt.Graphics g)
    {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paintComponent(g2);
    }

    public void postopomaSpremeniAlpha(float start, float stop, float step) {
        new Thread(new Runnable() {
            public void run() {
                for (float i = start; i <= stop; i += step) {
                    setAlpha(i);
                    try {
                        Thread.sleep(10);
                    }
                    catch (Exception e) {
                    }
                }
            }
        }).start();
    }
}