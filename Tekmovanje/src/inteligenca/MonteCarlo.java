package inteligenca;

import java.util.ArrayList;

import logika.Igra;
import logika.Stanje;
import splosno.Poteza;

public class MonteCarlo extends Inteligenca {
	
	/* Določiš št. iteracij. Ponavljaš toliko iteracij. Izbereš najboljšo možnost. */
	
	@Override
	public Poteza izberiPotezo (Igra igra) {
		// Ta metoda izbere najboljso potezo glede na simulacijo
	}
	
	public igrajDoKonca (Vozlisce vozlisce) {
		/* Izbereš naključnega otroka in igraš igro. To ponavljaš dokler se igra ne zaključi. 
		 * Glede na končno stanje posodobiš oceno vsem igranim otrokom = vsem staršem.
		 * Vsem otrokom posodobiš obiske.
		 */
		
	}
	
	public Poteza nakljucnaPoteza(ArrayList<Poteza> poteze) {
		int index = (int)(Math.random() * poteze.size());
		return poteze.get(index);
	}
	
	public void odigrajNakljucnoPotezo(Vozlisce vozlisce) {
		/* Odigraš naključno potezo. V otrokovem vozlišču posodobiš obisk.*/
		
	}
	
	public void posodobiOceno(Vozlisce vozlisce) {
		/* Back propagation. Staršu posodabljaš oceno. */
		
	}
	
}
