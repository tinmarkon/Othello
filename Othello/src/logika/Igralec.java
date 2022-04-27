package logika;

public enum Igralec {
	B, W;
	
	public Igralec nasprotnik() {
		return (this == B ? W : B);
	}
	
	public Polje getPolje() {
		return (this == B ? Polje.B : Polje.W);
	}

}
