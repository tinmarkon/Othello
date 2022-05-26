package logika;

public enum Stanje {
	V_TEKU, ZMAGA_B, ZMAGA_W, NEODLOCENO;
}

public String toString() {
	if (this == V_TEKU) return "V TEKU";
	else if (this == ZMAGA_B) return "ZMAGA B";
	else if (this == ZMAGA_W) return "ZMAGA W";
	else return "NEODLOCENO";
	}
}
