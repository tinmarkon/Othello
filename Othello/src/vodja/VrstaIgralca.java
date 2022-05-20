package vodja;

public enum VrstaIgralca {
	R1, R2, C; 

	@Override
	public String toString() {
		switch (this) {
		case C: return "človek";
		case R1: return "prvi računalnik";
		case R2: return "drugi računalnik";
		default: assert false; return "";
		}
	}

}

