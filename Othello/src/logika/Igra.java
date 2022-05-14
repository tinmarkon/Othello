package logika;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import splosno.Poteza;

public class Igra {
	// ustvari plosco, t. j. 8 x 8 seznam Polj

	private Polje[][] deska;
	private ArrayList<Poteza> moznePoteze = new ArrayList<>(); //to more bit morda public, ce rabiva v inteligenci? al kako ze to deluje ...
	private Set<Poteza> naMeji = new HashSet<Poteza>(); //sem shranjujemo prazna polja, ki mejijo na vsaj eno polno
	private Igralec naPotezi;

	public final int[][] smeri = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
	public final int[][] zacetnaMeja = {{2, 2}, {2, 3}, {2, 4}, {2, 5}, {3, 2}, {3, 5}, {4, 2}, {4, 5}, {5, 2}, {5, 3}, {5, 4}, {5, 5}};

	public Igra() {
		this.deska = new Polje[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				deska[i][j] = Polje.PRAZNO;
			}
		}
		deska[3][3] = Polje.BLACK;
		deska[4][4] = Polje.BLACK;
		deska[4][3] = Polje.WHITE;
		deska[3][4] = Polje.WHITE;

		naPotezi = Igralec.BLACK;
		for (int[] koordinate : zacetnaMeja) {
			Poteza trenutnaPoteza = new Poteza(koordinate[0], koordinate[1]);
			naMeji.add(trenutnaPoteza);
		}
	}
	
	public boolean jeVeljavenInt(int x) {
		return x >= 0 && x < 8;
	}
	
	public boolean jePraznoPolje(int i, int j) {
		return deska[i][j] == Polje.PRAZNO;
	}

	public boolean jeVeljavnaPoteza(Poteza poteza) {
		/* Preveri, če lahko igralec naPotezi igra dano potezo. Vrne true, če lahko in false sicer.
		V vsaki smeri od dane poteze išče neprekinjeno zaporedje nasprotnikovih žetonov,
		ki se konča z žetonom trenutnega naPotezi. */
		int i = poteza.getX();
		int j = poteza.getY();
		if (deska[i][j] == Polje.PRAZNO) {
			for (int k = 0; k < 8; k++) {
				boolean zastavica = false;
				i += smeri[k][0];
				j += smeri[k][1];
				while (jeVeljavenInt(i) && jeVeljavenInt(j)) {
					if (deska[i][j] == Polje.PRAZNO) break;
					if (deska[i][j] == naPotezi.getPolje() && !zastavica) break;
					if (deska[i][j] == naPotezi.getPolje() && zastavica) return true;
					i += smeri[k][0];
					j += smeri[k][1];
					zastavica = true;
				}
			}
		}
		return false;
	}

	public void posodobiNaMeji(Poteza poteza) {
		/* Sprejme potezo, ki smo jo pravkar igrali.
		To potezo izbriše iz naMeji ter v seznam doda vsa prazna polja, ki mejijo na to potezo in še niso v naMeji.
		*/
		naMeji.remove(poteza);
		for (int[] smer: smeri) {
			Poteza potencialnaPoteza = new Poteza(poteza.getX() + smer[0], poteza.getY() + smer[1]);
			if (jePraznoPolje(potencialnaPoteza.getX(), potencialnaPoteza.getY()) && jeVeljavenInt(potencialnaPoteza.getX()) && jeVeljavenInt(potencialnaPoteza.getY())) naMeji.add(poteza); 
		}
	}
	public void posodobiMoznePoteze() {
		/* Za vsako polje na deski preveri, če je to možna poteza.
		To je treba optimizirat tako, da išče samo po potezah v setu naMeji: prazna polja, ki mejijo na vsaj eno polno.
		*/
			for(Poteza poteza: naMeji) {
				if (jeVeljavnaPoteza(poteza)) moznePoteze.add(poteza);
			}
	}

	public boolean odigraj(Poteza poteza) {
		/* Če je poteza v seznamu možnih potez:
		Če da: Odigra potezo. Na desko položi nov žeton, obrne ustrezne nasprotnikove žetone, spremeni naPotezi. Vrne true.
		Če ne: Vrne false.
		 */
		int i = poteza.getX();
		int j = poteza.getY();
		if (moznePoteze.contains(poteza)) {
			deska[i][j] = naPotezi.getPolje();
			posodobiNaMeji(poteza);
			obrniZa(new Poteza(i, j));
			naPotezi = naPotezi.nasprotnik();
			posodobiMoznePoteze(); //kje točno je najbolje to izvest? tu, v jeKoncana, v kodi za samo izvedbo igre ...
			return true;
		}
		return false;
	}
	
	
	public void obrniZa(Poteza poteza) {
		/* Funkcija vzame potezo, ki jo bomo igrali (iz možnih potez).
		Od tega polja se premika posamezno v vse smeri na plošči.
		Če v neki smeri izbranemu polju sledi polje na katerem je nasprotnikov žeton,
		zaporedna nasprotnikova polja pripenja v seznam trenutnaPolja.
		Če se serija nasprotnikovih polj v neki smeri zaključi s poljem igralca naPotezi,
		potem vsa trenutnaPolja nabrana v tej smeri obrnejo barvo.
		 */
		int i = poteza.getX();
		int j = poteza.getY();
			for (int k = 0; k < 8; k++) {
				ArrayList<Poteza> trenutnaPolja = new ArrayList<>();
				i += smeri[k][0];
				j += smeri[k][1];
				while (jeVeljavenInt(i) && jeVeljavenInt(j)) {
					if (deska[i][j] == Polje.PRAZNO) break;
					if (deska[i][j] == naPotezi.getPolje()) {
						obrniZetone(trenutnaPolja);
						break;
					}
					trenutnaPolja.add(new Poteza(i, j));
					i += smeri[k][0];
					j += smeri[k][1];
				}
			}
	}
	
	public void obrniZetone(ArrayList<Poteza> trenutnaPolja) {
		/* Spremeni barvo žetonov na poljih iz vhodnega seznama v barvo igralca naPotezi.
		*  Morda bolje, če spremeni v nasprotno barvo. Lahko kar: deska[i][j] = (deska[i][j] == Polje.BLACK) itd. ? */
		for (Poteza p: trenutnaPolja) {
			int i = p.getX();
			int j = p.getY();
			Polje polje = deska[i][j];
			deska[i][j] = polje.nasprotno();
		}
	}
	
	public boolean jeKoncana() {
		/* Funkcija preverja, če je igra zaključena. Vrne true, če je, in false sicer.
		Če trenutni naPotezi nima možnih potez, je na vrsti nasprotnik.
		Če tudi ta nima možnih potez je igra zaključena. Preštejemo žetone in razglasimo zmagovalca.
		+ imamo še class STANJE: V_TEKU, ZMAGA_W in ZMAGA_B, bi ga blo treba uporabljat?
		*/
		if (moznePoteze.size() == 0) {
			naPotezi = naPotezi.nasprotnik();
			posodobiMoznePoteze();
			if (moznePoteze.size() == 0) {
				int[] st = prestejZetone();
				int black = st[0];
				int white = st[1];
				if (black > white) System.out.println("Slavo in cast crnemu igralcu.");
				else if (black < white) System.out.println("Slavo in cast belemu igralcu.");
				else System.out.println("Izenaceno");
				return true;
			}
				
		}
		return false;
	}
	
	public int[] prestejZetone() {
		/* Prešteje črne in bele žetone na deski. Vrne seznam {#crni, #beli}.
		 */
		int white = 0;
		int black = 0; 
		int[] zetoni = new int[2];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (deska[i][j] == Polje.WHITE) white++;
				else if (deska[i][j] == Polje.BLACK) black++;
			}
		}
		zetoni[0] = black;
		zetoni[1] = white;
		return zetoni;
	}
}

	
	
 
