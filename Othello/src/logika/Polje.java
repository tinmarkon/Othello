package logika;

public class Polje {
	// na Deski je 8 x 8 Polj, ki so lahko v STANJU: ČRNO, BELO ali PRAZNO
	// vsako polje ima svoji KOORDINATI x in y
	
	public static enum Stanje {
		BLACK, WHITE, PRAZNO;
	}
	
	private final int x;
	private final int y;
	private Stanje stanje;
	
	public Polje(int x, int y) {
		this.x = x;
		this.y = y;
		this.stanje = Stanje.PRAZNO;
	}

	public Stanje getStanje() {
		return stanje;
	}

	public void setStanje(Stanje stanje) {
		this.stanje = stanje;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Stanje obrniStanje() {
    // vrne obrnjeno stanje: črno spremeni v belo in obratno.
	// morda bolje ce kar obrne this.stanje = novoStanje in nic ne vrne?;
	return (this.stanje == Stanje.BLACK ? Stanje.BLACK : Stanje.WHITE);
    // se izjema, ce je stanje prazno in se ga ne da obrnit
	}
}