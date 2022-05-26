package inteligenca;

import logika.Igra;
import splosno.Poteza;

public class NeumenIgralec extends Inteligenca{
	
	@Override
	public Poteza izberiPotezo(Igra igra) {
		Poteza prvaPoteza = igra.poteze().get(0);
		return prvaPoteza;

}
	
}
