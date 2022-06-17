package si.lodrant.othello.inteligenca;
import java.util.List;
import si.lodrant.othello.logika.Igra;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.splosno.Poteza;

public class AlphaBeta extends Inteligenca {

	private static final int ZMAGA = 100;
	private static final int PORAZ = -ZMAGA;
	private static final int NEODLOCENO = 0;
	private static final int NESKONCNO = ZMAGA + 1;

	private int globina;
	private int alpha;
	private int beta;

	public AlphaBeta(int globina) {
		this.globina = globina;
		this.alpha = -NESKONCNO;
		this.beta = NESKONCNO;
	}

	@Override
	public Poteza izberiPotezo(Igra igra) {
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
	

	public OcenjenaPoteza minimax(Igra igra, int globina, Igralec jaz) {
		boolean maksimiziramo = (jaz == igra.naPotezi() ? true : false);
		if (igra.jeZakljucena()) {
			switch (igra.stanje()) {
			case ZMAGA_B:
				int ocena = (jaz == Igralec.BLACK ? ZMAGA : PORAZ);
				return new OcenjenaPoteza(null, ocena);
			case ZMAGA_W:
				ocena = (jaz == Igralec.WHITE ? ZMAGA : PORAZ);
				return new OcenjenaPoteza(null, ocena);
			case NEODLOCENO:
				ocena = NEODLOCENO;
				return new OcenjenaPoteza(null, ocena);
			default:
				break;
			}
		}
		else {
			if (globina == 0) {
				return new OcenjenaPoteza(null, OceniPozicijo.oceniPozicijo(igra, jaz));
			}
			else {
				Poteza ocenjenaPoteza = null;
				int najboljsaPoteza = -NESKONCNO;
				
				if (maksimiziramo) {
					for (Poteza p : igra.poteze()) {
						Igra kopijaIgre = new Igra(igra);
						kopijaIgre.odigraj(p);
						int ocenap;
						ocenap = minimax(kopijaIgre, globina - 1, jaz).ocena;
						if (ocenap > najboljsaPoteza) {
							najboljsaPoteza = ocenap;
							ocenjenaPoteza = p;
						}
					}
				}
				else {
					for (Poteza p : igra.poteze()) {
						Igra kopijaIgre = new Igra(igra);
						kopijaIgre.odigraj(p);
						int ocenap;
						ocenap = minimax(kopijaIgre, globina - 1, jaz).ocena;
						if (ocenap < najboljsaPoteza) {
							najboljsaPoteza = ocenap;
							ocenjenaPoteza = p;
							}
					}
				}
				return new OcenjenaPoteza(ocenjenaPoteza, najboljsaPoteza);
			}
		}
	}
}