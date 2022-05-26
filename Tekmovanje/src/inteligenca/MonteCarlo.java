package inteligenca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import logika.Igra;
import logika.Igralec;
import logika.Stanje;
import splosno.Poteza;

public class MonteCarlo extends Inteligenca{

	private static final int ZMAGA = 1;
	private static final double NEODLOCENO = 0.5;
	private static final int MAX_CAS = 4900; //ms

	@Override
	public Poteza izberiPotezo(Igra igra) {
        /* Iščemo najboljšo izmed možnih potez z uporabo algoritma MONTE CARLO TREE SEARCH.
        Trajanje iskanja omejimo na 4,8 s. */

		//System.out.print(igra.naPotezi() + " Izbiram MCTS potezo. ");

		long start = System.currentTimeMillis();
		long end = start + MAX_CAS;

		Igralec igralec = igra.naPotezi();

		if (igra.poteze().size() == 1) {
			//System.out.println("Igral sem edino možno potezo.");
			return igra.poteze().get(0);
		}

		/* Drevo začneš z deblom = začetno pozicijo igre. */
		Drevo drevo = new Drevo();
		Vozlisce deblo = drevo.getDeblo();
		deblo.getStatus().setGamePosition(igra);

		while (System.currentTimeMillis() < end) {
			// (1) SELEKCIJA: premaknemo se v najbolj obetaven list na trenutnem drevesu
			Vozlisce obetavnoVozlisce = izberiObetavnoVozlisce(deblo);

            /* (2) RAZVOJ: ustvarimo vozlišča otrok obetavnoVozlisce in jih shranimo v njegov slovarOtrok.
            To storimo le v primeru, ko obetavnoVozlišče še ni končno (ima otroke). */
			if (obetavnoVozlisce.getStatus().getGamePosition().stanje() == Stanje.V_TEKU)
				razvijVozlisce(obetavnoVozlisce);

			// (3) SIMULACIJA
            /* Simulacijo vedno začnemo v vozlišču, ki ga še nikoli nismo obiskali.
            Od tam naprej odigramo naključno igro in si zapolnimo rezultat. */
			Vozlisce zaPreiskat =  ((obetavnoVozlisce.getSlovarOtrok().size() > 0) ? obetavnoVozlisce.nakljucenOtrok() : obetavnoVozlisce);
			Stanje rezultatSimulacije = simulirajNakljucnoIgro(zaPreiskat, igralec);

			// (4) POSODBI OCENE glede na rezultatSimulacije
			propagirajNazaj(zaPreiskat, rezultatSimulacije);
		}
		Vozlisce zmagovalnoVozlisce = deblo.najboljObiskanOtrok();
		drevo.setDeblo(zmagovalnoVozlisce);

		//System.out.println("Število obiskov zmagovalca: " + zmagovalnoVozlisce.getStatus().getObiski() + ".");
		//System.out.println("Število zmag v zmagovalcu: " + zmagovalnoVozlisce.getStars().getStatus().getZmage() + ".");

		return zmagovalnoVozlisce.getStars().getSlovarOtrok().get(zmagovalnoVozlisce); //vrnes potezo, ki te pripelje v zmagovalnoVozlisce
	}

	private Vozlisce izberiObetavnoVozlisce(Vozlisce deblo) {
        /* Zacne v deblu in se premakne do konca veje (lista).
        V vsakem koraku (vozliscu) se premakne v že ustvarjenega otroka z najboljso UCToceno. */

		Vozlisce vozlisce = deblo;
		while (vozlisce.getSlovarOtrok().size() != 0) {
			vozlisce = najboljeOcenjenoVozlisce(vozlisce);
		}
		return vozlisce;
	}

	static Vozlisce najboljeOcenjenoVozlisce(Vozlisce vozlisce) {
        /* Izmed vseh otrok danega vozlisca izbere tistega z najboljso (a) UCT oceno ali (b) skupnoOceno.
		Slednja upošteva UCT oceno, v začetku igre preferira poteze v središču plošče, v
		začetku igre teži k manj žetonom barve inteligenčnega igralca,
		boljšo oceno dajo poteze v kotih desko, slabšo pa poteze, ki igralcu "podarijo" kote.
		Ocenjevanje delno prirejeno po zgledu: https://royhung.com/reversi. */

		List<Vozlisce> otroci = new ArrayList<>(vozlisce.getSlovarOtrok().keySet());
		return Collections.max(otroci, Comparator.comparing(c -> c.UCTocena())); //Collections.max(otroci, Comparator.comparing(c -> c.skupnaOcena("max")));
	}

	public void razvijVozlisce(Vozlisce stars) {
        /* Ustvari otroke (objekt tipa Vozlisce) za vhodnega starsa.
        Nastavi starsa in jim pripise zmage = 0 in obiski = 0.
        Otroke pospravi v staršev slovarOtrok. */
		Map<Status, Poteza> moznaStanja = stars.getStatus().getMozniStatusi();
		for (Status s: moznaStanja.keySet()) {
			Vozlisce v = new Vozlisce(s);
			v.setStars(stars);
			stars.getSlovarOtrok().put(v, moznaStanja.get(s));
		};
	}

	private void propagirajNazaj(Vozlisce v, Stanje koncnoStanje) {
    	/* Začneš v vozlišču v, kjer si začel naključno igro, in greš nazaj do debla po vseh starših.
        Vsakemu glede na izid igre po koncu naključne igre (koncnoStanje) posodobiš vozlisce.zmage ter povečaš vozlisce.obiski za 1.
        Će naključno igro zmaga igralec, ki je na vrsti v danesm vozliscu, zmagam pristejemo 1, na neodloceno igro pa 0.5.
        */

		Vozlisce vozlisce = v;

		while (vozlisce != null) { //dokler ne pridemo do debla
			vozlisce.getStatus().povecajObiske();
			if (koncnoStanje == Stanje.NEODLOCENO) vozlisce.getStatus().posodobiZmage(NEODLOCENO);

			if (koncnoStanje == vozlisce.getStatus().getGamePosition().naPotezi().nasprotnik().getStanje()) vozlisce.getStatus().posodobiZmage(ZMAGA);
			vozlisce = vozlisce.getStars();
		}
	}
	private Stanje simulirajNakljucnoIgro(Vozlisce vozlisce, Igralec igralec) {
    	/* Igra delno-nakljucno igro iz vozlisca in vrne izid igre v zadnjem vozliscu - listu: zmaga_B, zmaga_W oz. NEODLOCENO.
		Delno-naključno pomeni, da se izogiba zelo slabim pozicijam (takim, ki nasprotniku podarijo kote.)
        Če smo želeli simulirati naključno igro iz lista v katerem zmaga nasprotnik, potem nastavimo zmage
        njegovega starša na Integer.MIN_VALUE --> to ni dobra poteza.*/

		Vozlisce kopija = new Vozlisce(vozlisce);
		Status stanjeVozlisca = kopija.getStatus();
		Stanje stanjeIgre = stanjeVozlisca.getGamePosition().stanje();

		if (stanjeIgre == igralec.nasprotnik().getStanje()) {//vozlisce.getStatus().getGamePosition().naPotezi().getStanje()) {
			kopija.getStars().getStatus().setZmage(Integer.MIN_VALUE);
			return stanjeIgre;
		}

		while (stanjeIgre == Stanje.V_TEKU) {
			/* Igraj naključne poteze. */
			stanjeVozlisca.igrajNakljucno(); //igrajDelnoNakljucno();
			stanjeIgre = stanjeVozlisca.getGamePosition().stanje();
		}
		return stanjeIgre;

	}
}