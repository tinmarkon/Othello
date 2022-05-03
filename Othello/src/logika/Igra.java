package logika;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import logika.Polje.Vrednost;
import splosno.Poteza;

public class Igra {
	// ustvari desko, t. j. 8 x 8 seznam Polj
	// nova deska ima privzeto nastavljene začetno STANJE polj na PRAZNO
	private final Deska deska;
	private Vrednost naPotezi; //ne vem ali potrebujemo posebej class igralec, ali lahko samo shranjujemo katera vrednost je na potezi
	
	public Igra() {
		deska = new Deska();
		//zacetno vrednost; to bi se lahko izvedlo tudi v konstruktorju class-a Deska
		deska.getPolje(3, 3).setVrednost(Vrednost.BLACK);
		deska.getPolje(4, 4).setVrednost(Vrednost.BLACK);
		deska.getPolje(4, 3).setVrednost(Vrednost.WHITE);
		deska.getPolje(3, 4).setVrednost(Vrednost.WHITE);
		
		//this.naPotezi = Vrednost.BLACK;
	}
	
	public boolean jeVeljavenInt(int x) {
		return x >= 0 && x < 8;
	}
	
	public boolean jePraznoPolje(Polje polje) {
		return polje.getVrednost() == Vrednost.PRAZNO;
	}

	public boolean jeVeljavnaPoteza(Polje polje, int smerX, int smerY) {
		int trenutniX = polje.getX();
		int trenutniY = polje.getY();
		Vrednost barvaNasprotnika = (naPotezi == Vrednost.BLACK) ? Vrednost.WHITE : Vrednost.BLACK;
		Vrednost barvaIgralca = (naPotezi == Vrednost.BLACK) ? Vrednost.BLACK : Vrednost.WHITE;
		if (polje.getVrednost() == barvaNasprotnika) {
			while(jeVeljavenInt(trenutniX) && jeVeljavenInt(trenutniY)) {
				trenutniX += smerX;
				trenutniY += smerY;
				if(deska.getPolje(trenutniX, trenutniY).getVrednost() == Vrednost.PRAZNO) {
					return false;
				}
				if(deska.getPolje(trenutniX, trenutniY).getVrednost() == barvaIgralca) {
					return true;
				}
				else 
				{
					
				}
				
			}
		}
		return false;
	}
	

	public List<Polje> moznePoteze() {
		Vrednost barvaNasprotnika = naPotezi == Vrednost.BLACK ? Vrednost.WHITE : Vrednost.BLACK;
		ArrayList<Polje> veljavnePoteze = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (deska.getPolje(i, j).getVrednost() == barvaNasprotnika && !veljavnePoteze.contains(deska.getPolje(i, j))) {
					if (jeVeljavnaPoteza(deska.getPolje(i, j), 1, 0) && jePraznoPolje(deska.getPolje(i - 1, j))) {
						veljavnePoteze.add(deska.getPolje(i - 1, j));
					}
					if (jeVeljavnaPoteza(deska.getPolje(i, j), 0, 1) && jePraznoPolje(deska.getPolje(i, j - 1))) {
						veljavnePoteze.add(deska.getPolje(i, j - 1));
					}
					if (jeVeljavnaPoteza(deska.getPolje(i, j), -1, 0)  && jePraznoPolje(deska.getPolje(i + 1, j))) {
						veljavnePoteze.add(deska.getPolje(i + 1, j));
					}
					if (jeVeljavnaPoteza(deska.getPolje(i, j), 0, -1) && jePraznoPolje(deska.getPolje(i, j + 1))) {
						veljavnePoteze.add(deska.getPolje(i, j + 1));
					}
					if (jeVeljavnaPoteza(deska.getPolje(i, j), 1, 1)  && jePraznoPolje(deska.getPolje(i - 1, j - 1))) {
						veljavnePoteze.add(deska.getPolje(i - 1, j - 1));
					}
					if (jeVeljavnaPoteza(deska.getPolje(i, j), 1, - 1)  && jePraznoPolje(deska.getPolje(i - 1, j + 1))) {
						veljavnePoteze.add(deska.getPolje(i - 1, j + 1));
					}
					if (jeVeljavnaPoteza(deska.getPolje(i, j), -1, 1)  && jePraznoPolje(deska.getPolje(i + 1, j - 1))) {
						veljavnePoteze.add(deska.getPolje(i + 1, j - 1));
					}
					if (jeVeljavnaPoteza(deska.getPolje(i, j), -1, - 1)  && jePraznoPolje(deska.getPolje(i + 1, j + 1))) {
						veljavnePoteze.add(deska.getPolje(i + 1, j + 1));
					}
				}
			}
		}
		return veljavnePoteze;
	}
	
	
	public boolean odigraj(Poteza poteza) {
		List<Polje> poljaPotez = moznePoteze();
		if (poljaPotez.size() == 0) {
			this.naPotezi = this.naPotezi == Vrednost.BLACK ? Vrednost.WHITE : Vrednost.BLACK;
			return true;
		}
		int xPoteza = poteza.getX();
		int yPoteza = poteza.getY();
		if (deska.getPolje(xPoteza, yPoteza).getVrednost() != Vrednost.PRAZNO || !poljaPotez.contains(deska.getPolje(xPoteza, yPoteza))) return false;
		deska.getPolje(xPoteza, yPoteza).setVrednost(naPotezi);
		this.naPotezi = this.naPotezi == Vrednost.BLACK ? Vrednost.WHITE : Vrednost.BLACK;
		return true;
		
	}
	
	
	
}
