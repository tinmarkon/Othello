package si.lodrant.othello.inteligenca;

import si.lodrant.othello.logika.Igra;
import si.lodrant.othello.logika.Igralec;

public class OceniPozicijo {

    public static int oceniPozicijo(Igra igra, Igralec jaz) {
        return igra.poteze().size() - igra.nasprotnikovePoteze().size();
    }

}