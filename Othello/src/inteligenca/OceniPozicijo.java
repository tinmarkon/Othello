package inteligenca;

import logika.Igra;
import logika.Igralec;

public class OceniPozicijo {

	public static int oceniPozicijo(Igra igra, Igralec jaz) {
			return igra.poteze().size() - igra.nasprotnikovePoteze().size();
	}

}