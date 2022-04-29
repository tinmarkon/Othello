package logika;

public class Polje {
	// na Deski je 8 x 8 Polj, ki imajo lahko VREDNOST: ČRNO, BELO ali PRAZNO
	// vsako polje ima svoji KOORDINATI x in y
	
	public static enum Vrednost {
		BLACK, WHITE, PRAZNO;
	}
	
	private final int x;
	private final int y;
	private Vrednost vrednost;
	
	public Polje(int x, int y) {
		this.x = x;
		this.y = y;
		this.vrednost = Vrednost.PRAZNO;
	}

	public Vrednost getVrednost() {
		return vrednost;
	}

	public void setVrednost(Vrednost vrednost) {
		this.vrednost = vrednost;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Vrednost obrniVrednost() {
    // vrne obrnjeno vrednost: črno spremeni v belo in obratno.
	// morda bolje ce kar obrne this.vrednost = novoVrednost in nic ne vrne?;
	return (this.vrednost == Vrednost.BLACK ? Vrednost.WHITE : Vrednost.BLACK);
    // se izjema, ce je vrednost prazno in se ga ne da obrnit
	}
}