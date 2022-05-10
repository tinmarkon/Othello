packpackage logika;
public enum Polje {
	BLACK, WHITE, PRAZNO

	public Polje nasprotno() { //deluje samo, če je polje belo ali crno!!!
		if (this == PRAZNO) {
			System.out.println("Tega polja pa ne znam obrnit.");
			return PRAZNO;
		}
		else return (this == BLACK ? WHITE : BLACK);
	}

	}
