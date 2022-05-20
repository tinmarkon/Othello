package inteligenca;

import java.util.HashMap;
import java.util.Map;

import logika.Igra;

import splosno.Poteza;

public class Vozlisce {
	public final Igra gamePosition;
	private int obiski;
	// Število zmag če iz tega nodea igramo random poteze
	private double ocena;
	private Vozlisce stars;
	private Map<Poteza, Vozlisce> otroci = new HashMap<Poteza, Vozlisce>();
	
	public Vozlisce(Igra gamePosition, Vozlisce stars) {
		this.gamePosition = gamePosition;
		this.obiski = 1; //ali 0
		this.ocena = 0;
		this.stars = stars;
	}
	
	public double getOcena() {
		return ocena;
	}
	
	public double getObiski() {
		return obiski;
	}
	
	public Map<Poteza, Vozlisce> getOtroci() {
		Map<Poteza, Vozlisce> otroci = new HashMap<Poteza, Vozlisce>();
		for (Poteza p: gamePosition.poteze()) {
			Igra kopijaIgre = new Igra(gamePosition);
			kopijaIgre.odigraj(p);
			otroci.put(p, new Vozlisce(kopijaIgre, this));
		}
		return otroci;	
	}
	
	public void printVozlisce() {
		gamePosition.printIgra();
	}
	
	public void getPoteze() {
		gamePosition.poteze();
	}


}
