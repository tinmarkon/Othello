package inteligenca;

import logika.Igra;
import logika.Igralec;

public class OceniPozicijo {

	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		int[] zetoni = igra.prestejZetone();
		int ocena = (jaz == Igralec.BLACK ? zetoni[0] - zetoni[1] : -(zetoni[0] - zetoni[1]));
		return ocena;
	}

}