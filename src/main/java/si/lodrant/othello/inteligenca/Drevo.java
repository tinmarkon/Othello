package si.lodrant.othello.inteligenca;

public class Drevo {
    Vozlisce deblo;

    public Drevo() {
        deblo = new Vozlisce();
    }

    public Drevo(Vozlisce deblo) {
        this.deblo = deblo;
    }

    public Vozlisce getDeblo() {
        return deblo;
    }

    public void setDeblo(Vozlisce deblo) {
        this.deblo = deblo;
    }

}