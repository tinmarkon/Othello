package inteligenca;

import java.util.List;

import logika.Igra;
import logika.Igralec;
import splosno.Poteza;

public class AlphaBeta extends Inteligenca {

	private static final int ZMAGA = 100;
	private static final int PORAZ = -ZMAGA;
	private static final int NEODLOCENO = 0;

	private int globina;
	private int alpha;
	private int beta;

	public AlphaBeta(int globina) {
		// super("minimax globina " + globina + ",alpha " + alpha + ",beta " + beta);
		this.globina = globina;
		this.alpha = PORAZ;
		this.beta = ZMAGA;
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