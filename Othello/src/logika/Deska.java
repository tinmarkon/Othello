package logika;

import java.util.ArrayList;
import java.util.List;
public class Deska {
	// Deska je seznam 8 x 8 Polj
	// Vsako polje ima koordinati x in y ter Vrednost, ki je lahko BLACK, WHITE ali PRAZNO
	private final List<List<Polje>> deska;
	
	public Deska() {
		deska = new ArrayList<>();
		for(int i = 0; i < 8; i++) {
			List<Polje> vrsta = new ArrayList<>();
			deska.add(vrsta);
			for(int j = 0; j < 8; j++) {
				vrsta.add(new Polje(i, j));
			}
		}	
	}
	
	public Polje getPolje(int x, int y) {
		return deska.get(x).get(y);
	}
	
}
