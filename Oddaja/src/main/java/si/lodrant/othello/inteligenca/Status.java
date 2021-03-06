package si.lodrant.othello.inteligenca;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import si.lodrant.othello.logika.Igra;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.splosno.Poteza;

public class Status {
    /* Status vozlišča v drevesu za implementacijo Monte Carlo Tree Search algoritma. */
    private Igra gamePosition; //stanje na plošči, ki ga predstavlja to vozlišče
    private int obiski; //kolikokrat smo obiskali to vozlišče in potem odigrali naključno igro
    private double zmage; //kolikokrat smo v naključnih igrah iz tega vozlišča zmagali
    private static final Poteza[] zeloSlabePoteze = {new Poteza(1, 1), new Poteza(6, 1), new Poteza(1, 6), new Poteza(6, 6)};

    public Status() {
        this.gamePosition = new Igra();
    }

    public Status(Status status) {
        this.gamePosition = new Igra(status.gamePosition);
        this.obiski = status.obiski;
        this.zmage = status.zmage;
    }

    public Status(Igra igra) {
        this.gamePosition = new Igra(igra);
    }

    Igra getGamePosition() {
        return gamePosition;
    }

    void setGamePosition(Igra gamePosition) {
        this.gamePosition = gamePosition;
    }

    Igralec getIgralec() {
        return this.gamePosition.naPotezi();
    }

    public int getObiski() {
        return obiski;
    }

    public void setObiski(int obiski) {
        this.obiski = obiski;
    }

    double getZmage() {
        return zmage;
    }

    void setZmage(double zmage) {
        this.zmage = zmage;
    }

    public Map<Status, Poteza> getMozniStatusi() {
        /* Za dano vozlišče vrne slovar možnih stanj vozlišč povezanih s potezami. */
        Map<Status, Poteza> mozniStatusi = new HashMap<Status, Poteza>();
        ArrayList<Poteza> moznePoteze = this.gamePosition.poteze();
        for (Poteza p : moznePoteze) {
            Status newStatus = new Status(this.gamePosition);
            newStatus.getGamePosition().odigraj(p);
            mozniStatusi.put(newStatus, p);
        }
        ;
        return mozniStatusi;
    }

    void povecajObiske() {
        this.obiski++;
    }

    void posodobiZmage(double rezultatIgre) {
        /* Zmage posodabljaš po odigrani naključni igri v danem vozlišču in njegovih starših. */
        if (this.zmage != Integer.MIN_VALUE) {
            this.zmage += rezultatIgre;
        }
    }

    public Poteza izberiNakljucnoPotezo() {
        List<Poteza> moznePoteze = this.gamePosition.poteze();
        Random r = new Random();
        int index = r.nextInt(moznePoteze.size());
        return moznePoteze.get(index);

    }

    public void igrajNakljucno() {
        this.gamePosition.odigraj(this.izberiNakljucnoPotezo());
    }

    public void igrajBlokirano() {
        this.gamePosition.odigraj(new Poteza(-10, -10));
    }

    public void igrajDelnoNakljucno() {
        /* Skuša se izogibati zelo slabim potezam, ki nasprotniku podarijo kote. */
        Poteza poteza = this.izberiNakljucnoPotezo();
        if (Arrays.asList(zeloSlabePoteze).contains(poteza)) {
            poteza = this.izberiNakljucnoPotezo();
            if (Arrays.asList(zeloSlabePoteze).contains(poteza)) {
                poteza = this.izberiNakljucnoPotezo();
            }
        }
        this.gamePosition.odigraj(poteza);
    }

    public void printVozlisce() {
        gamePosition.printIgra();
        System.out.println("Zmage: " + zmage);
        System.out.println("Obiski: " + obiski);
    }

}
