package si.lodrant.othello;

import si.lodrant.othello.gui.ZacetnoOkno;

public class Othello {
	public static void main(String[] args) {
		
		ZacetnoOkno zacetno_okno = new ZacetnoOkno();
		zacetno_okno.pack();
		zacetno_okno.setVisible(true);
		/* 
		GlavnoOkno glavno_okno = new GlavnoOkno();
    	glavno_okno.pack();
		glavno_okno.setVisible(true);
		Vodja.okno = glavno_okno;*/
	}   
}
