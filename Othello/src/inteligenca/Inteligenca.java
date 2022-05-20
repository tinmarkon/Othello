package inteligenca;

import java.util.Random;

import logika.Igra;
import splosno.KdoIgra;
import splosno.Poteza;

public class Inteligenca extends splosno.KdoIgra {
	
	public Inteligenca() {
		super("FIZFIN!");
		KdoIgra team = new KdoIgra("FIZFIN");
	}
	
	public Poteza izberiPotezo(Igra igra) {
		Poteza prvaPoteza = igra.poteze().get(0);
		return prvaPoteza;
	}
} 