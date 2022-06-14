package si.lodrant.othello.inteligenca;
import si.lodrant.othello.logika.Igra;
import si.lodrant.othello.splosno.KdoIgra;
import si.lodrant.othello.splosno.Poteza;

public class Inteligenca extends si.lodrant.othello.splosno.KdoIgra {
	
	public Inteligenca() {
		super("FIZFIN!");
		KdoIgra team = new KdoIgra("FIZFIN");
	}
	
	public Poteza izberiPotezo(Igra igra) {
		Inteligenca inteligenca = new MonteCarlo(1000);
		return inteligenca.izberiPotezo(igra);
	}
} 