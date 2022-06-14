package si.lodrant.othello.logika;

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
		return (this == BLACK ? "črni" : "beli");
	}

}
