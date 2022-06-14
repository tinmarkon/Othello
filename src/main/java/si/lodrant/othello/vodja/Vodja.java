package si.lodrant.othello.vodja;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingWorker;

import si.lodrant.othello.gui.GlavnoOkno;
import si.lodrant.othello.inteligenca.AlphaBeta;
import si.lodrant.othello.inteligenca.Inteligenca;
import si.lodrant.othello.inteligenca.MonteCarlo;
import si.lodrant.othello.logika.Igra;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.splosno.KdoIgra;
import si.lodrant.othello.splosno.Poteza;

public class Vodja {
	
	public static Map<Igralec, VrstaIgralca> vrstaIgralca; //računalnik ali človek
	public static Map<Igralec, KdoIgra> kdoIgra; //ime igralca
	public static Map<VrstaIgralca, KdoIgra> vsiBeliIgralci = new HashMap<VrstaIgralca, KdoIgra>(); //ime igralca
	public static Map<VrstaIgralca, KdoIgra> vsiCrniIgralci = new HashMap<VrstaIgralca, KdoIgra>(); //ime igralca
	protected final static String IGRALEC_C = "Človek";
	protected final static String IGRALEC_R1 = "Povprečen nasprotnik";
	protected final static String IGRALEC_R2 = "Pameten nasprotnik";
	protected final static String IGRALEC_R3 = "Genialen nasprotnik";

	public static GlavnoOkno okno;
	
	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	public static Poteza namig;
	public static boolean pokaziPoteze;
	public static boolean neveljavnaPoteza = false;

	public static void nastaviIgralce () {
		vsiBeliIgralci.put(VrstaIgralca.C, new KdoIgra(IGRALEC_C));
		vsiBeliIgralci.put(VrstaIgralca.R1, new KdoIgra(IGRALEC_R1));
		vsiBeliIgralci.put(VrstaIgralca.R2, new KdoIgra(IGRALEC_R2));
		vsiBeliIgralci.put(VrstaIgralca.R3, new KdoIgra(IGRALEC_R3));
		vsiCrniIgralci.put(VrstaIgralca.C, new KdoIgra(IGRALEC_C));
		vsiCrniIgralci.put(VrstaIgralca.R1, new KdoIgra(IGRALEC_R1));
		vsiCrniIgralci.put(VrstaIgralca.R2, new KdoIgra(IGRALEC_R2));
		vsiCrniIgralci.put(VrstaIgralca.R3, new KdoIgra(IGRALEC_R3));
	}

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
			return;
		case ZMAGA_W:
			return;
		case NEODLOCENO: 
			return;
		case BLOKIRANO:
			System.out.println("Naletel sem na blokirano potezo.");
			igra.odigraj(new Poteza(-10, -10));
			igramo ();
			break;
		case V_TEKU: 
			Igralec igralec = igra.naPotezi();
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true; // IgralnoPolje začne poskušati za mouseClicked dogodek
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
	public static Inteligenca racunalnikovaInteligenca3 = new MonteCarlo(1500); // Genialen nasprotnik.

	public static Inteligenca namigInteligenca = new MonteCarlo(1000); // Poišče namig.
	
	public static void igrajRacunalnikovoPotezo(Inteligenca racunalnikovaInteligenca) {
		Igra zacetnaIgra = igra;
		SwingWorker<Poteza, Void> worker = new SwingWorker<Poteza, Void> () {
			@Override
			protected Poteza doInBackground() {
				System.out.println("Izbiram računalnikovo potezo.");
				Poteza poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
				System.out.println("Izbral sem računalnikovo potezo.");
				return poteza;
			}
			@Override
			protected void done () {
				Poteza poteza = new Poteza(-10, -10);
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
		/* Se izvede po mouseClicked dogodku na IgralnoPolje. 
		Če smo kliknili na ustezno polje, odigramo izbrano potezo.
		Če izbrana poteza ni med možnimi potezami, to izpišemo v statusni vrstici.
		*/
		if (igra.odigraj(poteza)) {
			clovekNaVrsti = false;
			//ce smo igralcu prikazali ("vklopili") namig, naj se po njegovi potezi "izklopijo"
			namig = null;
			igramo ();
		}
		else {
			neveljavnaPoteza = true;
			okno.osveziGUI();
		}
		
	}

	public static void pokaziNamig() {
		/* Izberi najboljšo možno potezo glede na trenutno stanje igre.
		Kot pomoč človeškemu igralcu. 
		Namig se izriše na IgralnemPolju v GlavnemOknu, če ni enak "null", kot
		določeno v IgralnoPolje.java. */
		namig = namigInteligenca.izberiPotezo(igra);
	}

	public static void pokaziPoteze() {
		/* Kot določeno v IgralnoPolje.java se možne poteze na IgralnemPolju v GlavnemOknu
		izrišejo, če je pokaziPoteze = true. */
		pokaziPoteze = !pokaziPoteze;
	}
	
	
}
