package vodja;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingWorker;

import gui.GlavnoOkno;
import inteligenca.AlphaBeta;
import inteligenca.Inteligenca;
import inteligenca.MonteCarlo;
import inteligenca.NeumenIgralec;
import logika.Igra;
import logika.Igralec;
import splosno.KdoIgra;
import splosno.Poteza;

public class Vodja {
	
	public static Map<Igralec, VrstaIgralca> vrstaIgralca; //računalnik ali človek
	public static Map<Igralec, KdoIgra> kdoIgra; //ime igralca
	
	public static GlavnoOkno okno;
	
	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	public static Poteza namig;
	public static boolean pokaziPoteze;

	public static void igramoNovoIgro () {
		igra = new Igra ();
		namig = null;
		pokaziPoteze = false;
		igramo ();
	}
	
	public static void igramo () {
		okno.osveziGUI();

		switch (igra.stanje()) {
		case ZMAGA_B:
		case ZMAGA_W:
		case NEODLOCENO: 
			return;
		case V_TEKU: 
			Igralec igralec = igra.naPotezi();
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true;
				break;
			case R1:
				igrajRacunalnikovoPotezo(racunalnikovaInteligenca1);
				break;
			case R2:
				igrajRacunalnikovoPotezo(racunalnikovaInteligenca2);
				break;
			case R3:
				igrajRacunalnikovoPotezo(racunalnikovaInteligenca3);
				break;
			}
		}
	}
;
	public static Inteligenca racunalnikovaInteligenca1 = new AlphaBeta(3); // Povprečen nasprotnik.
	public static Inteligenca racunalnikovaInteligenca2 = new AlphaBeta(7); // Pameten nasprotnik.
	public static Inteligenca racunalnikovaInteligenca3 = new MonteCarlo(); // Genialen nasprotnik.

	public static Inteligenca namigInteligenca = new AlphaBeta(7); // Poišče namig.
	
	public static void igrajRacunalnikovoPotezo(Inteligenca racunalnikovaInteligenca) {
		Igra zacetnaIgra = igra;
		SwingWorker<Poteza, Void> worker = new SwingWorker<Poteza, Void> () {
			@Override
			protected Poteza doInBackground() {
				Poteza poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
				return poteza;
			}
			@Override
			protected void done () {
				Poteza poteza = null;
				try {poteza = get();} catch (Exception e) {};
				if (igra == zacetnaIgra) {
					igra.odigraj(poteza);
					igramo ();
				}
			}
		};
		worker.execute();
	}
		
	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (igra.odigraj(poteza)) {
			clovekNaVrsti = false;
			//ce smo igralcu prikazali ("vklopili") namig, naj se po njegovi potezi "izklopijo"
			namig = null;
			igramo ();
		}
		else {
			//TODO Izpis v statusni vrstici, da je izbrana poteza neveljavna.
		}
		
	}

	public static void pokaziNamig() {
		namig = namigInteligenca.izberiPotezo(igra);
	}

	public static void pokaziPoteze() {
		pokaziPoteze = !pokaziPoteze;
	}
	
	
}
