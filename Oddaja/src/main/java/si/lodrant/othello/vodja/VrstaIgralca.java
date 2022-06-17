package si.lodrant.othello.vodja;

public enum VrstaIgralca {
    C, R1, R2, R3;

    @Override
    public String toString() {
        switch (this) {
            case C:
                return "Človek";
            case R1:
                return "Neumen nasprotnik";
            case R2:
                return "Pameten nasprotnik";
            case R3:
                return "Genialen nasprotnik";
            default:
                assert false;
                return "";
        }
    }

}

