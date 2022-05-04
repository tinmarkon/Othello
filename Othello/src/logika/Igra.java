package logika;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import logika.Polje.Vrednost;
import splosno.Poteza;

public class Igra {
	// ustvari desko, t. j. 8 x 8 seznam Polj
	// nova deska ima privzeto nastavljene zaÄetno STANJE polj na PRAZNO
	private final Deska deska;
	private Vrednost naPotezi;
	public final int[][] smeri = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
	
	
	public Igra() {
		deska = new Deska();
		//zacetno vrednost; to bi se lahko izvedlo tudi v konstruktorju class-a Deska
		deska.getPolje(3, 3).setVrednost(Vrednost.BLACK);
		deska.getPolje(4, 4).setVrednost(Vrednost.BLACK);
		deska.getPolje(4, 3).setVrednost(Vrednost.WHITE);
		deska.getPolje(3, 4).setVrednost(Vrednost.WHITE);
		this.naPotezi = Vrednost.BLACK;
	}
	
	public boolean jeVeljavenInt(int x) {
		return x >= 0 && x < 8;
	}
	
	public boolean jePraznoPolje(Polje polje) {
		return polje.getVrednost() == Vrednost.PRAZNO;
	}

	public boolean jeVeljavnaPoteza(Polje polje) {
		int i = polje.getX();
		int j = polje.getY();
		if (deska.getPolje(i, j).getVrednost() == Vrednost.PRAZNO) {
			for (int k = 0; k < 8; k++) {
				boolean zastavica = false;
				i += smeri[k][0];
				j += smeri[k][1];
				while (jeVeljavenInt(i) && jeVeljavenInt(j)) {
					if (deska.getPolje(i, j).getVrednost() == Vrednost.PRAZNO) break;
					if (deska.getPolje(i, j).getVrednost() == this.naPotezi && !zastavica) break;
					if (deska.getPolje(i, j).getVrednost() == this.naPotezi && zastavica) return true;
					i += smeri[k][0];
					j += smeri[k][1];
					zastavica = true;
				}
			}
		}
		return false;
	}
	

	public ArrayList<Polje> moznePoteze() {
		ArrayList<Polje> veljavnePoteze = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (jeVeljavnaPoteza(deska.getPolje(i, j))) veljavnePoteze.add(deska.getPolje(i, j));
			}
		}
		return veljavnePoteze;
	}
	
	
	public boolean odigraj(Poteza poteza) {
		int i = poteza.getX();
		int j = poteza.getY();
		ArrayList<Polje> poteze = moznePoteze();
		if (poteze.contains(poteza)) {
			deska.getPolje(i, j).setVrednost(naPotezi);
			obrniZa(deska.getPolje(i, j));
			this.naPotezi = (this.naPotezi == Vrednost.BLACK) ? Vrednost.WHITE : Vrednost.BLACK;
			return true;
		}
		return false;
	}
	
	
	public void obrniZa(Polje polje) {
		int i = polje.getX();
		int j = polje.getY();
			for (int k = 0; k < 8; k++) {
				ArrayList<Polje> trenutnaPolja = new ArrayList<>();
				i += smeri[k][0];
				j += smeri[k][1];
				while (jeVeljavenInt(i) && jeVeljavenInt(j)) {
					if (deska.getPolje(i, j).getVrednost() == Vrednost.PRAZNO) break;
					if (deska.getPolje(i, j).getVrednost() == this.naPotezi) {
						obrniZetone(trenutnaPolja);
						break;
					}
					trenutnaPolja.add(deska.getPolje(i, j));
					i += smeri[k][0];
					j += smeri[k][1];
				}
			}
	}
	
	public void obrniZetone(ArrayList<Polje> trenutnaPolja) {
		for (Polje p: trenutnaPolja) p.obrniVrednost();
	}
	
	public boolean jeKoncana() {
		if (moznePoteze().size() == 0) {
			naPotezi = (this.naPotezi == Vrednost.BLACK) ? Vrednost.WHITE : Vrednost.BLACK;
			if (moznePoteze().size() == 0) {
				if (prestejZetone()[0] > prestejZetone()[1]) System.out.println("Zmagovalec je èrni");
				else if (prestejZetone()[0] < prestejZetone()[1]) System.out.println("Zmagovalec je bel");
				else System.out.println("Izenaèeno");
				return true;
			}
				
		}
		return false;
	}
	
	public int[] prestejZetone() {
		int white = 0;
		int black = 0; 
		int[] zetoni = new int[2];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (deska.getPolje(i, j).getVrednost() == Vrednost.WHITE) white++;
				else if (deska.getPolje(i, j).getVrednost() == Vrednost.BLACK) black++;
			}
		}
		zetoni[0] = black;
		zetoni[1] = white;
		return zetoni;
	}
}

	
	
 
