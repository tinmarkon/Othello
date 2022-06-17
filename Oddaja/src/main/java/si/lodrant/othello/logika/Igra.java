package si.lodrant.othello.logika;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import si.lodrant.othello.splosno.Poteza;

public class Igra {
    private Polje[][] deska;
    private Set<Poteza> naMeji = new HashSet<Poteza>(); //sem shranjujemo prazna polja, ki mejijo na vsaj eno polno
    private Igralec naPotezi;
    private Igra prejsnjeStanje;
    private boolean igraJeZakljucena;

    public final int[][] smeri = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    public final int[][] zacetnaMeja = {{2, 2}, {2, 3}, {2, 4}, {2, 5}, {3, 2}, {3, 5}, {4, 2}, {4, 5}, {5, 2}, {5, 3}, {5, 4}, {5, 5}};

    public Igra() {
        /* Ustvari desko, t. j. 8 x 8 seznam Stanj. Nastavi začetne pogoje na plošči in igralca. */
        this.deska = new Polje[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                deska[i][j] = Polje.PRAZNO;
            }
        }
        deska[4][4] = Polje.BLACK;
        deska[3][3] = Polje.BLACK;
        deska[3][4] = Polje.WHITE;
        deska[4][3] = Polje.WHITE;

        naPotezi = Igralec.BLACK;
        for (int[] koordinate : zacetnaMeja) {
            Poteza trenutnaPoteza = new Poteza(koordinate[0], koordinate[1]);
            naMeji.add(trenutnaPoteza);
        }
        igraJeZakljucena = false;
        prejsnjeStanje = null;
    }

    public Igra(Igra igra) {
        /* Ustvari kopijo igre */
        this.deska = new Polje[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.deska[i][j] = igra.deska[i][j];
            }
        }
        this.naPotezi = igra.naPotezi;
        this.naMeji.addAll(igra.naMeji);
        this.prejsnjeStanje = igra.prejsnjeStanje;
    }

    public Polje[][] getDeska() {
        return deska;
    }

    public Set<Poteza> naMeji() {
        return naMeji;
    }

    public Igralec naPotezi() {
        return naPotezi;
    }

    public boolean jeVeljavenInt(int x) {
        return x >= 0 && x < 8;
    }

    public boolean jePraznoPolje(int i, int j) {
        return deska[i][j] == Polje.PRAZNO;
    }

    public boolean jeVeljavnaZgodovina() {
        if (Objects.isNull(prejsnjeStanje)) return false;
        else return true;
    }

    public boolean jeZakljucena() {
        return igraJeZakljucena;
    }

    public void odstraniZadnjoPotezo() {
        this.deska = prejsnjeStanje.deska;
        this.naPotezi = prejsnjeStanje.naPotezi;
        this.naMeji = prejsnjeStanje.naMeji;
        prejsnjeStanje = null;
    }

    private void posodobiMejo(Poteza poteza) {
		/* Sprejme potezo, ki smo jo pravkar igrali.
		To potezo izbriše iz naMeji ter v seznam doda vsa prazna polja, ki mejijo na to potezo in še niso v naMeji.
		*/
        naMeji.remove(poteza);
        for (int[] smer : smeri) {
            Poteza potencialnaPoteza = new Poteza(poteza.getX() + smer[0], poteza.getY() + smer[1]);
            if (jeVeljavenInt(potencialnaPoteza.getX()) && jeVeljavenInt(potencialnaPoteza.getY())) {
                if (jePraznoPolje(potencialnaPoteza.getX(), potencialnaPoteza.getY())) naMeji.add(potencialnaPoteza);
            }
        }
    }

    public int[] prestejZetone() {
        /* Prešteje črne in bele žetone na deski. Vrne seznam {#crni, #beli}.
         */
        int white = 0;
        int black = 0;
        int[] zetoni = new int[2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (deska[i][j] == Polje.WHITE) white++;
                else if (deska[i][j] == Polje.BLACK) black++;
            }
        }
        zetoni[0] = black;
        zetoni[1] = white;
        return zetoni;
    }

    public boolean jeVeljavnaPoteza(Poteza poteza) {
        if (deska[poteza.getX()][poteza.getY()] == Polje.PRAZNO) {
            for (int[] smer : smeri) {
                boolean zastavica = false;
                int i = poteza.getX() + smer[0];
                int j = poteza.getY() + smer[1];
                while (jeVeljavenInt(i) && jeVeljavenInt(j)) {
                    if (deska[i][j] == Polje.PRAZNO) break;
                    if (deska[i][j] == naPotezi.getPolje() && !zastavica) break;
                    if (deska[i][j] == naPotezi.getPolje() && zastavica) return true;
                    i += smer[0];
                    j += smer[1];
                    zastavica = true;
                }
            }
        }
        return false;
    }

    public ArrayList<Poteza> poteze() {
		/* Za vsako polje iz seznama potez naMeji preveri, če je to možna poteza
		za trenutnega igralca naPotezi ter jih shrani v seznam. */
        ArrayList<Poteza> moznePoteze = new ArrayList<>();
        for (Poteza poteza : naMeji) {
            if (jeVeljavnaPoteza(poteza)) moznePoteze.add(poteza);
        }
        return moznePoteze;
    }

    public ArrayList<Poteza> nasprotnikovePoteze() {
        naPotezi = naPotezi.nasprotnik();
        ArrayList<Poteza> moznePoteze = poteze();
        naPotezi = naPotezi.nasprotnik();
        return moznePoteze;
    }

    public boolean odigraj(Poteza poteza) {
		/* Če je poteza v seznamu možnih potez:
		Če da: Odigra potezo. Na desko položi nov žeton, obrne ustrezne nasprotnikove žetone, spremeni naPotezi. Vrne true.
		Če ne: Vrne false.
		 */
        if (poteze().contains(poteza)) {
            prejsnjeStanje = new Igra(this);
            deska[poteza.getX()][poteza.getY()] = naPotezi.getPolje();
            posodobiMejo(poteza);
            obrniZa(poteza);
            naPotezi = naPotezi.nasprotnik();
            return true;
        } else if ((poteza.getX() == -10) && (poteza.getY() == -10)) {
            prejsnjeStanje = new Igra(this);
            naPotezi = naPotezi.nasprotnik();
            return true;
        }
        return false;
    }

    public void obrniZa(Poteza poteza) {
		/* Funkcija vzame potezo, ki jo bomo igrali (iz možnih potez).
		Od tega polja se premika posamezno v vse smeri na plošči.
		Če v neki smeri izbranemu polju sledi polje na katerem je nasprotnikov žeton,
		zaporedna nasprotnikova polja pripenja v seznam trenutnaPolja.
		Če se serija nasprotnikovih polj v neki smeri zaključi s poljem igralca naPotezi,
		potem vsa trenutnaPolja nabrana v tej smeri obrnejo barvo.
		 */
        for (int[] smer : smeri) {
            ArrayList<Poteza> trenutnaPolja = new ArrayList<>();
            int i = poteza.getX() + smer[0];
            int j = poteza.getY() + smer[1];
            while (jeVeljavenInt(i) && jeVeljavenInt(j)) {
                if (deska[i][j] == Polje.PRAZNO) break;
                if (deska[i][j] == naPotezi.getPolje()) {
                    obrniZetone(trenutnaPolja);
                    break;
                }
                trenutnaPolja.add(new Poteza(i, j));
                i += smer[0];
                j += smer[1];
            }
        }
    }

    public void obrniZetone(ArrayList<Poteza> trenutnaPolja) {
        /* Spremeni barvo žetonov na poljih iz vhodnega seznama v nasprotno barvo. */
        for (Poteza p : trenutnaPolja) {
            int i = p.getX();
            int j = p.getY();
            Polje polje = deska[i][j];
            deska[i][j] = polje.nasprotno();
        }
    }

    public Stanje stanje() {
        if (poteze().size() == 0) {
            if (nasprotnikovePoteze().size() == 0) {
                int[] st = prestejZetone();
                int black = st[0];
                int white = st[1];
                if (black > white) {
                    //System.out.println("Slavo in cast crnemu igralcu.");
                    igraJeZakljucena = true;
                    return Stanje.ZMAGA_B;
                } else if (black < white) {
                    //System.out.println("Slavo in cast belemu igralcu.");
                    igraJeZakljucena = true;
                    return Stanje.ZMAGA_W;
                } else {
                    igraJeZakljucena = true;
                    return Stanje.NEODLOCENO;
                }

            } else return Stanje.BLOKIRANO;
        } else return Stanje.V_TEKU;
    }


    public void printIgra() {
        /* Izriše trenutno stanje igralne plošče */
        ArrayList<Poteza> moznePoteze = this.poteze();
        System.out.println("Stanje igre je: " + stanje());
        System.out.print(" --- --- --- --- --- --- --- ---");
        for (int i = 0; i < 8; i++) {
            System.out.println();
            System.out.print("| ");
            for (int j = 0; j < 8; j++) {
                if (deska[i][j] == Polje.BLACK) System.out.print("B | ");
                else if (deska[i][j] == Polje.WHITE) System.out.print("W | ");

                else if (naMeji.contains(new Poteza(i, j)) && (moznePoteze.contains(new Poteza(i, j))))
                    System.out.print("ox| ");
                else if (naMeji.contains(new Poteza(i, j)) && (!moznePoteze.contains(new Poteza(i, j))))
                    System.out.print("o | ");
                else if (deska[i][j] == Polje.PRAZNO) System.out.print("  | ");
            }
            System.out.println();
            System.out.print(" --- --- --- --- --- --- --- ---");
        }
        System.out.println();
        System.out.print("Mozne poteze: ");

        for (Poteza p : moznePoteze) {
            int i = p.getX();
            int j = p.getY();
            System.out.print("(" + i + ", " + j + ")");
        }
        System.out.println();
        System.out.print("Mejne poteze: ");

        for (Poteza p : naMeji) {
            int i = p.getX();
            int j = p.getY();
            System.out.print("(" + i + ", " + j + ")");
        }
        System.out.println();
        System.out.println("Na potezi je: " + naPotezi);
        System.out.println(". . . . . . . . . . . . . . . . . . . . .");
        System.out.println();
    }
}
