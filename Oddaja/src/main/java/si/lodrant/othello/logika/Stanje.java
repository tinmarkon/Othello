package si.lodrant.othello.logika;

public enum Stanje {
    V_TEKU, BLOKIRANO, ZMAGA_B, ZMAGA_W, NEODLOCENO;


    public String toString() {
        if (this == V_TEKU) return "Igra je v teku.";
        else if (this == BLOKIRANO) return "Igralec nima možnih potez. Še enkrat je na vrsti nasprotnik.";
        else if (this == ZMAGA_B) return "Zmagal je črni igralec.";
        else if (this == ZMAGA_W) return "Zmagal je beli igralec.";
        else return "Rezultat je neodločen.";
    }

}