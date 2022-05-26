package logika;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import splosno.Poteza;

public class IgraOth {

	public static final int N = 8;
	private Set<Poteza> naMeji = new HashSet<Poteza>();
	private Polje[][] deska;
	private Igralec naPotezi;
	public Stanje stanje;

	public final int[][] smeri = { { 1, 0 }, { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 1 }, { 1, -1 }, { -1, 1 },
			{ -1, -1 } };
	public final int[][] zacetnaMeja = { { 2, 2 }, { 2, 3 }, { 2, 4 }, { 2, 5 }, { 3, 2 }, { 3, 5 }, { 4, 2 }, { 4, 5 },
			{ 5, 2 }, { 5, 3 }, { 5, 4 }, { 5, 5 } };

	public IgraOth() {
		this.deska = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				deska[i][j] = Polje.PRAZNO;
			}
		}
		deska[3][3] = Polje.BLACK;
		deska[4][4] = Polje.BLACK;
		deska[4][3] = Polje.WHITE;
		deska[3][4] = Polje.WHITE;

		naPotezi = Igralec.BLACK;
		stanje = Stanje.V_TEKU;
		for (int[] koordinate : zacetnaMeja) {
			Poteza trenutnaPoteza = new Poteza(koordinate[0], koordinate[1]);
			naMeji.add(trenutnaPoteza);
		}
	}

	public IgraOth(IgraOth igra) {
		this.deska = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.deska[i][j] = igra.deska[i][j];
			}
		}
		this.naPotezi = igra.naPotezi;
	}

	public void printIgra() {
		System.out.print(" --- --- --- --- --- --- --- ---");
		for (int i = 0; i < 8; i++) {
			System.out.println();
			System.out.print("| ");
			for (int j = 0; j < 8; j++) {
				if (deska[i][j] == Polje.BLACK)
					System.out.print("O | ");
				if (deska[i][j] == Polje.WHITE)
					System.out.print("X | ");
				if (deska[i][j] == Polje.PRAZNO)
					System.out.print("  | ");
			}
			System.out.println();
			System.out.print(" --- --- --- --- --- --- --- ---");
			System.out.println();
		}
	}

	public Stanje stanje() {
		return stanje;
	}

	public Polje[][] getDeska() {
		return deska;
	}

	public Igralec naPotezi() {
		return naPotezi;
	}

	public boolean jeVeljavenInt(int x) {
		return x >= 0 && x < 8;
	}

	public boolean jePraznoPolje(int i, int j) {
		return deska[i][j] == Polje.PRAZNO;
	}

	private void posodobiMejo(Poteza poteza) {
		/*
		 * Sprejme potezo, ki smo jo pravkar igrali. To potezo izbriše iz naMeji ter v
		 * seznam doda vsa prazna polja, ki mejijo na to potezo in še niso v naMeji.
		 */
		naMeji.remove(poteza);
		for (int[] smer : smeri) {
			Poteza potencialnaPoteza = new Poteza(poteza.getX() + smer[0], poteza.getY() + smer[1]);
			if (jePraznoPolje(potencialnaPoteza.getX(), potencialnaPoteza.getY())
					&& jeVeljavenInt(potencialnaPoteza.getX()) && jeVeljavenInt(potencialnaPoteza.getY()))
				naMeji.add(poteza);
		}
	}

	public int[] prestejZetone() {
		/*
		 * Prešteje črne in bele žetone na deski. Vrne seznam {#crni, #beli}.
		 */
		int white = 0;
		int black = 0;
		int[] zetoni = new int[2];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (deska[i][j] == Polje.WHITE)
					white++;
				else if (deska[i][j] == Polje.BLACK)
					black++;
			}
		}
		zetoni[0] = black;
		zetoni[1] = white;
		return zetoni;
	}

	public boolean jeVeljavnaPoteza(Poteza poteza) {
		if (deska[poteza.getX()][poteza.getY()] == Polje.PRAZNO) {
			for (int[] smer : smeri) {
				boolean zastavica = false;
				int i = poteza.getX() + smer[0];
				int j = poteza.getY() + smer[1];
				while (jeVeljavenInt(i) && jeVeljavenInt(j)) {
					if (deska[i][j] == Polje.PRAZNO)
						break;
					if (deska[i][j] == naPotezi.getPolje() && !zastavica)
						break;
					if (deska[i][j] == naPotezi.getPolje() && zastavica)
						return true;
					i += smer[0];
					j += smer[1];
					zastavica = true;
				}
			}
		}
		return false;
	}

	public ArrayList<Poteza> poteze() {
		/*
		 * Za vsako polje na deski preveri, če je to možna poteza. To je treba
		 * optimizirat tako, da išče samo po potezah v setu naMeji: prazna polja, ki
		 * mejijo na vsaj eno polno.
		 */
		ArrayList<Poteza> moznePoteze = new ArrayList<>();
		for (Poteza poteza : naMeji) {
			if (jeVeljavnaPoteza(poteza))
				moznePoteze.add(poteza);
		}
		return moznePoteze;
	}

	public boolean odigraj(Poteza poteza) {
		/*
		 * Če je poteza v seznamu možnih potez: Če da: Odigra potezo. Na desko položi
		 * nov žeton, obrne ustrezne nasprotnikove žetone, spremeni naPotezi. Vrne true.
		 * Če ne: Vrne false.
		 */
		if (poteze().contains(poteza)) {
			deska[poteza.getX()][poteza.getY()] = naPotezi.getPolje();
			posodobiMejo(poteza);
			obrniZa(poteza);
			naPotezi = naPotezi.nasprotnik();
			return true;
		}
		return false;
	}

	public void obrniZa(Poteza poteza) {
		/*
		 * Funkcija vzame potezo, ki jo bomo igrali (iz možnih potez). Od tega polja se
		 * premika posamezno v vse smeri na plošči. Če v neki smeri izbranemu polju
		 * sledi polje na katerem je nasprotnikov žeton, zaporedna nasprotnikova polja
		 * pripenja v seznam trenutnaPolja. Če se serija nasprotnikovih polj v neki
		 * smeri zaključi s poljem igralca naPotezi, potem vsa trenutnaPolja nabrana v
		 * tej smeri obrnejo barvo.
		 */
		for (int[] smer : smeri) {
			ArrayList<Poteza> trenutnaPolja = new ArrayList<>();
			int i = poteza.getX() + smer[0];
			int j = poteza.getY() + smer[1];
			while (jeVeljavenInt(i) && jeVeljavenInt(j)) {
				if (deska[i][j] == Polje.PRAZNO)
					break;
				if (deska[i][j] == naPotezi.getPolje()) {
					obrniZetone(trenutnaPolja);
					break;
				}
				trenutnaPolja.add(new Poteza(i, j));
				i += smer[0];
				j += smer[1];
			}
		}
	}

	public void obrniZetone(ArrayList<Poteza> trenutnaPolja) {
		/*
		 * Spremeni barvo žetonov na poljih iz vhodnega seznama v barvo igralca
		 * naPotezi. Morda bolje, če spremeni v nasprotno barvo. Lahko kar: deska[i][j]
		 * = (deska[i][j] == Polje.BLACK) itd. ?
		 */
		for (Poteza p : trenutnaPolja) {
			int i = p.getX();
			int j = p.getY();
			Polje polje = deska[i][j];
			deska[i][j] = polje.nasprotno();
		}
	}

	public boolean jeKoncana() {
		/*
		 * Funkcija preverja, če je igra zaključena. Vrne true, če je, in false sicer.
		 * Če trenutni naPotezi nima možnih potez, je na vrsti nasprotnik. Če tudi ta
		 * nima možnih potez je igra zaključena. Preštejemo žetone in razglasimo
		 * zmagovalca. + imamo še class STANJE: V_TEKU, ZMAGA_W in ZMAGA_B, bi ga blo
		 * treba uporabljat?
		 */
		if (poteze().size() == 0) {
			naPotezi = naPotezi.nasprotnik();
			if (poteze().size() == 0) {
				int[] st = prestejZetone();
				int black = st[0];
				int white = st[1];
				if (black > white) {
					System.out.println("Slavo in cast crnemu igralcu.");
					stanje = Stanje.ZMAGA_W;

				} else if (black < white) {
					System.out.println("Slavo in cast belemu igralcu.");
					stanje = Stanje.ZMAGA_B;
				} else {
					System.out.println("Izenaceno");
					stanje = Stanje.NEODLOCENO;
				}
				return true;
			}

		}
		return false;
	}
}