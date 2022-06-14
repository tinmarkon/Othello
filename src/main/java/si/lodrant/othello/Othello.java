package si.lodrant.othello;

import si.lodrant.othello.gui.GlavnoOkno;
import si.lodrant.othello.gui.SwingUtils;
import si.lodrant.othello.vodja.Vodja;

public class Othello {
	public static void main(String[] args) {
		SwingUtils.nastaviLookAndFeel();
		GlavnoOkno glavno_okno = new GlavnoOkno();
		glavno_okno.prikazi();
		Vodja.okno = glavno_okno;
	}
}
