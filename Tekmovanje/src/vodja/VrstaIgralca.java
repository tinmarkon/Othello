package vodja;

public enum VrstaIgralca {
	R1, R2, R3, C; 

	@Override
	public String toString() {
		switch (this) {
		case C: return "človek";
		case R1: return "povprečen nasprotnik";
		case R2: return "pameten nasprotnik";
		case R3: return "genialen nasprotnik";
		default: assert false; return "";
		}
	}

}

