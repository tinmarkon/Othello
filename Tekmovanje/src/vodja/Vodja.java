package vodja;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import gui.GlavnoOkno;
import inteligenca.AlphaBeta;
import inteligenca.Inteligenca;
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

	public static void igramoNovoIgro () {
		igra = new Igra ();
		igramo ();
	}
	
	public static void igramo () {
		okno.osveziGUI();
		igra.printIgra();

		switch (igra.stanje()) {
		case ZMAGA_B:
		case ZMAGA_W:
		case NEODLOCENO: 
			return; // odhajamo iz metode igramo
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
			}
		}
	}
	
	public static Inteligenca racunalnikovaInteligenca1 = new AlphaBeta(7); //igra proti človeku, igra ČRNO proti računalnikoviInteligenci
	public static Inteligenca racunalnikovaInteligenca2 = new NeumenIgralec();
	
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
			igramo ();
		}
		else System.out.println("Izbrana poteza ni veljavna!");
		
	}
	
	
}
