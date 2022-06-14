package si.lodrant.othello.inteligenca;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.splosno.Poteza;

public class Vozlisce {
    /* Objekt vozlisce predstavlja razvejišče (angl. Node) v drevesu za implementacijo Monte Carlo Tree Search algoritma.
    Vozlišče ima
    (1) stanje -- predstavlja ga class Status (da ne mešamo z razredom logika.Stanje,
        ki predstavlja stanje igre) in ima 3 polja:
        * Igra gamePosition: Stanje na igralni deski v tem vozlišču je unikatno posameznemu vozlišču;
        * int obiski: kolikokrat smo že izbrali to vozlišče in v naslendjih korakih simulirali naključno
        igro;
        * int zmage: kolikokrat smo zmagali, ko smo izbrali to vozlišče in naprej do konca igrali naključno
        igro (+1 za zmago, +0,5 za nedoločen rezultat oz. odvisno od tega kdo je na vrsti);
    (2) starša (tipa vozlišče) in
    (3) slovarOtrok (ključi so vozlišča orok, vrednosti pa poteze, ki jih moramo igrati
        na gamePosition, da pridemo v to vozlišče). */

	final double c = 1/Math.sqrt(2);
	private static final Poteza[] zeloSlabePoteze = {new Poteza(1, 1), new Poteza(6, 1), new Poteza(1, 6), new Poteza(6, 6)};
	private static final Poteza[] slabePoteze = {new Poteza(0, 1), new Poteza(1, 0), new Poteza(6, 0), new Poteza(0, 6), new Poteza(7, 1), new Poteza(1, 7), new Poteza(7, 6), new Poteza(6, 7)};
	private static final Poteza[] dobrePoteze = {new Poteza(0, 0), new Poteza(7, 7), new Poteza(0, 7), new Poteza(7, 0)};

	private Status status;
	private  Vozlisce stars;
	private Map<Vozlisce, Poteza> slovarOtrok;

	public Vozlisce() {
		this.status = new Status();
		this.slovarOtrok = new HashMap<Vozlisce, Poteza>();
	}

	public Vozlisce(Status status) {
		this.status = new Status(status);
		this.slovarOtrok = new HashMap<Vozlisce, Poteza>();
	}

	public Vozlisce(Status status, Vozlisce stars, Map<Vozlisce, Poteza> otroci) {
		this.stars = stars;
		this.status = status;
		this.slovarOtrok = otroci;
	}

	public Vozlisce(Vozlisce vozlisce) {
		/* Kopija vozlisca. */
		this.slovarOtrok = new HashMap<Vozlisce, Poteza>();
		this.status = new Status(vozlisce.getStatus());

		if (vozlisce.getStars() != null) //ce je to prvo vozlisce = deblo in nima starsa
			this.stars = vozlisce.getStars();

		List<Vozlisce> otroci = new ArrayList<Vozlisce>(slovarOtrok.keySet());
		for (int i = 0; i < vozlisce.slovarOtrok.size(); i++) {
			this.slovarOtrok.put(otroci.get(i), slovarOtrok.get(otroci.get(i))); //moram tu ustvarit novo vozlišče?
			//this.slovarOtrok.put(new Vozlisce(otroci.get(i)), slovarOtrok.get(otroci.get(i)));
		}
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Vozlisce getStars() {
		return stars;
	}

	public void setStars(Vozlisce stars) {
		this.stars = stars;
	}

	public Map<Vozlisce, Poteza> getSlovarOtrok() {
		return slovarOtrok;
	}

	public Vozlisce nakljucenOtrok() {
		Set<Vozlisce> otrociSet = slovarOtrok.keySet();
		List<Vozlisce> otroci = new ArrayList<>(otrociSet);

		Random r = new Random();
		int index = r.nextInt(otroci.size());
		return otroci.get(index);
	}

	public Vozlisce najboljObiskanOtrok() {
		List<Vozlisce> otroci = new ArrayList<>(this.getSlovarOtrok().keySet());
		Vozlisce najboljsi = Collections.max(otroci, Comparator.comparing((Vozlisce c) -> c.getStatus().getObiski()));
		return najboljsi;
	}

	public void printVozlisce(){
		this.getStatus().getGamePosition().printIgra();
		System.out.println("Zmage: " + this.getStatus().getZmage());
		System.out.println("Obiski: " + this.getStatus().getObiski());
	}

	public double UCTocena() {
		/* UCT (Upper Confidence Bound) ocena vozlišča za selekcijo. */
		int N = this.stars.getStatus().getObiski();
		int n = this.getStatus().getObiski();
		double zmage = this.getStatus().getZmage();
		if (n == 0) {
			/* Radi bi obiskali vse otroke. */
			return Integer.MAX_VALUE;
		}
		double o = zmage / n + c * Math.sqrt(Math.log(N)/n);
		return o;

	}

	// ----- za potencialno izboljšavo MCTS algoritma -----
	public double notranjePoteze() {
		/* V začetku igre naj preferira poteze v notranjosti deske */
		int i = this.getStars().getSlovarOtrok().get(this).getX();
		int j = this.getStars().getSlovarOtrok().get(this).getY();
		return 1 / Math.sqrt( Math.pow(i - 3.5, 2) + Math.pow(j - 3.5, 2));
	}

	public double kaznujPozresnost() {
		/* V začetku igre naj ne želi imeti več žetonov svoje barve. */
		int[] st = this.getStatus().getGamePosition().prestejZetone();
		int crni = st[0];
		int beli = st[1];
		int t = 1;
		if (this.getStatus().getGamePosition().naPotezi() == Igralec.WHITE) t = -1;
		return t * (beli - crni) /  Math.pow(beli + crni, 4);
	}

	public double ocenaPoteze() {
		/* Dobre so poteze v kotih deske, slabe so poteze, ki dajo kote nasprotniku.*/
		Poteza poteza = this.getStars().getSlovarOtrok().get(this);
		double ocenaPoteze = 0;
		if (Arrays.asList(slabePoteze).contains(poteza)) ocenaPoteze = -0.55;
		else if (Arrays.asList(zeloSlabePoteze).contains(poteza)) ocenaPoteze = -100;
		else if (Arrays.asList(dobrePoteze).contains(poteza)) ocenaPoteze = 1.5;
		return ocenaPoteze;
	}

	public double skupnaOcena(String minmax) {
		int[] st = this.getStatus().getGamePosition().prestejZetone();
		if ((st[0] + st[1]) > 50) return this.UCTocena();

		else {
			if (minmax == "min") return this.UCTocena() * (1 - this.notranjePoteze() - this.ocenaPoteze());
			else return this.UCTocena() * (1 + this.notranjePoteze() + this.ocenaPoteze());
		}
	}
}