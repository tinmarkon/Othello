package si.lodrant.othello.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import si.lodrant.othello.vodja.Vodja;

import si.lodrant.othello.logika.Polje;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.splosno.Poteza;

/**
 * Pravokotno območje, v katerem je narisano igralno polje.
 */
public class IgralnoPolje extends JPanel implements MouseListener {

    public IgralnoPolje() {
        Color barvaOzadja = new Color(44, 144, 169);
        setBackground(barvaOzadja);
        this.addMouseListener(this);
    }

    public IgralnoPolje(Dimension preferredSize) {
        Color barvaOzadja = new Color(44, 144, 169);
        setBackground(barvaOzadja);
        this.addMouseListener(this);
        this.setPreferredSize(preferredSize);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }

    // Relativna širina črte
    private final static double LINE_WIDTH = 0.04;

    // Širina enega kvadratka
    private double squareWidth() {
        return Math.min(getWidth(), getHeight()) / 8;
    }

    private double getCenterX() {
        return (getWidth() - squareWidth() * 8) / 2;
    }

    private double getCenterY() {
        return (getHeight() - squareWidth() * 8) / 2;
    }

    // Relativni prostor okoli žetona
    private final static double PADDING = 0.13;

    private void paintZeton(Graphics2D g2, int j, int i, Color barvaZetona, Boolean senca) {
        /* Nariše žeton barve barvaZetona na polje i, j na deski. Premaknjeno v center zaslona X, Y. */
        int X = (int) getCenterX();
        int Y = (int) getCenterY();

        double w = squareWidth();
        // ----- senca -------
        if (senca) {
            Color barvaSence = new Color(38, 124, 145);
            double ds = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
            double xs = w * (i + 0.5 * LINE_WIDTH + PADDING) + 0.05 * w;
            double ys = w * (j + 0.5 * LINE_WIDTH + PADDING) + 0.03 * w;
            g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
            g2.setColor(barvaSence);
            //g2.drawOval((int)xs, (int)ys, (int)ds, (int)ds);
            g2.fillOval((int) (X + xs), (int) (Y + ys), (int) ds, (int) ds);
        }
        // ----- zeton -------
        double d = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
        double x = w * (i + 0.5 * LINE_WIDTH + PADDING);
        double y = w * (j + 0.5 * LINE_WIDTH + PADDING);
        g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
        g2.setColor(barvaZetona);
        //g2.drawOval((int)x, (int)y, (int)d, (int)d);
        g2.fillOval((int) (X + x), (int) (Y + y), (int) d, (int) d);
    }

    @Override
    protected void paintComponent(Graphics g) {
        /* Pokliče se z ukazom .repaint v vodja.osveziGUI.
         * Nariše žetone na polje glede na stanja na igra.getDeska(),
         * pobarva igralno polje na bolj svetlo modro od ozadja,
         * nariše bele črte.
         */
        int X = (int) getCenterX();
        int Y = (int) getCenterY();

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        double w = squareWidth();

        // ------ svetlejše območje, kjer je igralno polje --------
        Color barvaPolja = new Color(90, 170, 197); //new Color(118,181,197);
        g2.setColor(barvaPolja);
        g2.drawRect(X, Y, (int) (8 * w), (int) (8 * w));
        g2.fillRect(X, Y, (int) (8 * w), (int) (8 * w));

        // ----------------------- črte ---------------------------
        Color barvaCrte = Color.WHITE;
        Color barvaObrobe = new Color(44, 144, 169);
        g2.setColor(barvaCrte);
        g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
        for (int i = 1; i < 8; i++) {
            g2.drawLine((int) (X + i * w),
                    (int) (Y),
                    (int) (X + i * w),
                    (int) (Y + 8 * w));
            g2.drawLine((int) (X),
                    (int) (Y + i * w),
                    (int) (X + 8 * w),
                    (int) (Y + i * w));
        }

        // ----------------------- obrobe ---------------------------
        g2.setColor(Color.WHITE);
        g2.drawLine((int) (X), (int) (Y), (int) (X), (int) (Y + 8 * w));
        g2.drawLine((int) (X), (int) (Y), (int) (X + 8 * w), (int) (Y));
        g2.drawLine((int) (X + 8 * w), (int) (Y), (int) (X + 8 * w), (int) (Y + 8 * w));
        g2.drawLine((int) (X), (int) (Y + 8 * w), (int) (X + 8 * w), (int) (Y + 8 * w));

        if (Vodja.igra != null) {
            // ----------- narisi zetone glede na stanje igre ------------
            Polje[][] deska;
            deska = Vodja.igra.getDeska();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    switch (deska[i][j]) {
                        case WHITE:
                            paintZeton(g2, i, j, Color.WHITE, true);
                            break;
                        case BLACK:
                            paintZeton(g2, i, j, Color.BLACK, true);
                            break;
                        default:
                            break;
                    }
                }
            }

            if (Vodja.pokaziPoteze) {
                // -------- Nariše možne poteze za igralca naPotezi.----------
                ArrayList<Poteza> moznePoteze = Vodja.igra.poteze();
                Color barvaPoteze = new Color(255, 255, 255, 100);
                if (Vodja.igra.naPotezi() == Igralec.BLACK) barvaPoteze = new Color(0, 0, 0, 80);
                for (Poteza p : moznePoteze) {
                    paintZeton(g2, p.getX(), p.getY(), barvaPoteze, false);
                }
            }

            if (Vodja.namig != null) {
                /* Nariše potezo, ki jo je namigInteligenca iz razreda Vodja izbrala kot najboljšo. */
                Poteza namig = Vodja.namig;
                Color barvaNamiga = new Color(79, 223, 142);
                if (Vodja.igra.naPotezi() == Igralec.BLACK) barvaNamiga = new Color(255, 237, 117);
                paintZeton(g2, namig.getX(), namig.getY(), barvaNamiga, false);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (Vodja.clovekNaVrsti) {
            int x = e.getX() - (int) getCenterX();
            int y = e.getY() - (int) getCenterY();
            int w = (int) (squareWidth());
            int j = x / w;
            double di = (x % w) / squareWidth();
            int i = y / w;
            double dj = (y % w) / squareWidth();
            if (0 <= i && i < 8 &&
                    0.5 * LINE_WIDTH < di && di < 1.0 - 0.5 * LINE_WIDTH &&
                    0 <= j && j < 8 &&
                    0.5 * LINE_WIDTH < dj && dj < 1.0 - 0.5 * LINE_WIDTH) {
                Vodja.igrajClovekovoPotezo(new Poteza(i, j));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
