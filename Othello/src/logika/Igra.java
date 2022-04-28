package logika;

import java.util.LinkedList;
import java.util.List;

import logika.Polje.Stanje;
import splosno.Poteza;

public class Igra {
	// ustvari desko, t. j. 8 x 8 seznam Polj
	// nova deska ima privzeto nastavljene začetno STANJE polj na PRAZNO
	private final Deska deska;
	private Stanje naPotezi; //ne vem ali potrebujemo posebej class igralec, ali lahko samo shranjujemo katera vrednost je na potezi
	
	public Igra() {
		deska = new Deska();
		//zacetno stanje; to bi se lahko izvedlo tudi v konstruktorju class-a Deska
		deska.getPolje(3, 3).setStanje(Stanje.BLACK);
		deska.getPolje(4, 4).setStanje(Stanje.BLACK);
		deska.getPolje(4, 3).setStanje(Stanje.WHITE);
		deska.getPolje(3, 4).setStanje(Stanje.WHITE);
		
		//this.naPotezi = Stanje.BLACK;
	}
	
	
	public boolean odigraj(Poteza poteza) {
		// TODO metoda vra�a true/false �e je poteza veljavna
		// Poteza(0,0) je �isto levo zgoraj in Poteza(7,7) je �isto desno spodaj
		return false;
	}

}
