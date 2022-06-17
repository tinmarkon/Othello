package si.lodrant.othello.inteligenca;

import java.util.ArrayList;
import java.util.Random;

import si.lodrant.othello.logika.Igra;
import si.lodrant.othello.splosno.Poteza;

public class NeumenIgralec extends Inteligenca {

    @Override
    public Poteza izberiPotezo(Igra igra) {
        System.out.println(igra.naPotezi() + " Izbiram NAKLJUČNO potezo. ");
        Poteza nakljucnaPoteza = nakljucnaPoteza(igra.poteze());
        return nakljucnaPoteza;

    }

    public Poteza nakljucnaPoteza(ArrayList<Poteza> poteze) {
        Random r = new Random();
        int index = r.nextInt(poteze.size());
        return poteze.get(index);
    }

}
