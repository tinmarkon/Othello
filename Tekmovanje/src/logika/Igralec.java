package logika;

public enum Igralec {
	BLACK, WHITE;
	
	public Igralec nasprotnik() {
		return (this == BLACK ? WHITE : BLACK);
	}

	public Polje getPolje() {
		return (this == BLACK ? Polje.BLACK : Polje.WHITE);
	}

	@Override
	public String toString() {
		// Izpiši kateri igralec je na potezi.
		return (this == BLACK ? "BLACK" : "WHITE");
	}
	public Stanje getStanje() {
		return (this == WHITE ? Stanje.ZMAGA_W : Stanje.ZMAGA_B);
	}
}
