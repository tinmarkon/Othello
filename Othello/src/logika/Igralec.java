package logika;

//Nisem prepricana, ce potrebujemo tak class, zaenkrat naj bo
public enum Igralec {
	BLACK, WHITE;
	
	public Igralec nasprotnik() {
		return (this == BLACK ? WHITE : BLACK);
	}
	
	//Zakaj že to potrebujemo ...
	public Polje getPolje() {
		return (this == BLACK ? Polje.BLACK : Polje.WHITE);
	}

	@Override
	public String toString() {
		// Izpiši kateri igralec je na potezi.
		return (this == BLACK ? "BLACK" : "WHITE");
	}
}
