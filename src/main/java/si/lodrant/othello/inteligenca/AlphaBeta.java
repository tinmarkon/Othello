package si.lodrant.othello.inteligenca;
import java.util.List;
import si.lodrant.othello.logika.Igra;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.splosno.Poteza;

public class AlphaBeta extends Inteligenca {

	private static final int ZMAGA = 100;
	private static final int PORAZ = -ZMAGA;
	private static final int NEODLOCENO = 0;

	private int globina;
	private int alpha;
	private int beta;

	public AlphaBeta(int globina) {
		this.globina = globina;
		this.alpha = PORAZ;
		this.beta = ZMAGA;
	}

	@Override
	public Poteza izberiPotezo(Igra igra) {
		System.out.println("Izbiram ALPHABETA potezo.");
		//System.out.print(igra.naPotezi() + " Izbiram ALFABETA potezo. ");
		OcenjenaPoteza najboljsaPoteza = alphabetaPoteze(igra, this.globina, this.alpha, this.beta, igra.naPotezi());
		return najboljsaPoteza.poteza;
	}

	public OcenjenaPoteza alphabetaPoteze(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		int ocena;
		if (igra.naPotezi() == jaz) {
			ocena = PORAZ;
		} else {
			ocena = ZMAGA;
		}
		List<Poteza> moznePoteze = igra.poteze();
		Poteza kandidat = moznePoteze.get(0);
		for (Poteza p : moznePoteze) {
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
			int ocenap;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_B:
				ocenap = (jaz == Igralec.BLACK ? ZMAGA : PORAZ);
				break;
			case ZMAGA_W:
				ocenap = (jaz == Igralec.WHITE ? ZMAGA : PORAZ);
				break;
			case NEODLOCENO:
				ocenap = NEODLOCENO;
				break;
			case BLOKIRANO:
				ocenap = (jaz == igra.naPotezi() ? -igra.nasprotnikovePoteze().size() : +igra.nasprotnikovePoteze().size());
				break;
			default:
				if (globina == 0)
					ocenap = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
				else
					ocenap = alphabetaPoteze(kopijaIgre, globina - 1, alpha, beta, jaz).ocena;
			}
			if (igra.naPotezi() == jaz) {
				if (ocenap > ocena) {
					ocena = ocenap;
					kandidat = p;
					alpha = Math.max(alpha, ocena);
				}
			} else {
				if (ocenap < ocena) {
					ocena = ocenap;
					kandidat = p;
					beta = Math.min(beta, ocena);
				}
			}
			if (alpha >= beta)
				return new OcenjenaPoteza(kandidat, ocena);
		}
		return new OcenjenaPoteza(kandidat, ocena);
	}
}