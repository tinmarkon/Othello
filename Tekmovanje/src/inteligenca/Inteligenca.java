package inteligenca;
import logika.Igra;
import splosno.KdoIgra;
import splosno.Poteza;

public class Inteligenca extends splosno.KdoIgra {
	
	public Inteligenca() {
		super("FIZFIN!");
		KdoIgra team = new KdoIgra("FIZFIN");
	}
	
	public Poteza izberiPotezo(Igra igra) {
		Inteligenca inteligenca = new MonteCarlo();
		return inteligenca.izberiPotezo(igra);
	}
} 