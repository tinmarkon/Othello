package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import vodja.Vodja;

import logika.Igra;
import logika.Polje;
import logika.Igralec;
import splosno.Poteza;


/**
 * Pravokotno območje, v katerem je narisano igralno polje.
 */
@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel implements MouseListener {
	
	public IgralnoPolje() {
		Color barvaOzadja = new Color(118,181,197);
		setBackground(barvaOzadja);
		this.addMouseListener(this);
		
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(600, 600);
	}

	
	// Relativna širina črte
	private final static double LINE_WIDTH = 0.04;
	
	// Širina enega kvadratka
	private double squareWidth() {
		return Math.min(getWidth(), getHeight()) / 8;
	}
	
	// Relativni prostor okoli žetona
	private final static double PADDING = 0.18;
	
	/**
	 * V grafični kontekst {@g2} nariši žeton v polje {@(i,j)}
	 * @param g2
	 * @param i
	 * @param j
	 */
	private void paintZeton(Graphics2D g2, int j, int i, Color barvaZetona) {
		double w = squareWidth();
		double d = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
		double x = w * (i + 0.5 * LINE_WIDTH + PADDING);
		double y = w * (j + 0.5 * LINE_WIDTH + PADDING);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		g2.setColor(barvaZetona);
		g2.drawOval((int)x, (int)y, (int)d , (int)d);
		g2.fillOval((int)x, (int)y, (int)d , (int)d);
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		/* se pokliče z ukazom .repaint v vodja.osveziGui
		 * nariše žetone na polje glede na stanja na igra.getDeska()
		 */
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		double w = squareWidth();

		// črte
		Color barvaCrte = Color.WHITE;
		g2.setColor(barvaCrte);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		for (int i = 1; i < 8; i++) {
			g2.drawLine((int)(i * w),
					    (int)(0),
					    (int)(i * w),
					    (int)(8 * w));
			g2.drawLine((int)(0),
					    (int)(i * w),
					    (int)(8 * w),
					    (int)(i * w));
		}
		
		Polje[][] deska;;
		if (Vodja.igra != null) {
			deska = Vodja.igra.getDeska();
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					switch(deska[i][j]) {
					case WHITE: paintZeton(g2, i, j, Color.WHITE); break;
					case BLACK: paintZeton(g2, i, j, Color.BLACK); break;
					default: break;
					}
				}
			}
		}	
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (Vodja.clovekNaVrsti) {
			int x = e.getX();
			int y = e.getY();
			int w = (int)(squareWidth());
			int j = x / w ;
			double di = (x % w) / squareWidth() ;
			int i = y / w ;
			double dj = (y % w) / squareWidth() ;
			if (0 <= i && i < 8 &&
					0.5 * LINE_WIDTH < di && di < 1.0 - 0.5 * LINE_WIDTH &&
					0 <= j && j < 8 && 
					0.5 * LINE_WIDTH < dj && dj < 1.0 - 0.5 * LINE_WIDTH) {
				Vodja.igrajClovekovoPotezo (new Poteza(i, j));
				// System.out.print("Kliknil si polje (" + i + ", " + j + ")." );
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
