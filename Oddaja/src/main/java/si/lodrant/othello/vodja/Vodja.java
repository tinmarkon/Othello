package si.lodrant.othello.vodja;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingWorker;

import si.lodrant.othello.gui.GlavnoOkno;
import si.lodrant.othello.inteligenca.AlphaBeta;
import si.lodrant.othello.inteligenca.Inteligenca;
import si.lodrant.othello.inteligenca.MonteCarlo;
import si.lodrant.othello.inteligenca.NeumenIgralec;
import si.lodrant.othello.logika.Igra;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.splosno.KdoIgra;
import si.lodrant.othello.splosno.Poteza;

public class Vodja {
    public static Map<Igralec, VrstaIgralca> vrstaIgralca; //računalnik ali človek

    public static Map<Igralec, KdoIgra> kdoIgra; //ime igralca
    public static GlavnoOkno okno;
    public static Igra igra = null;
    public static boolean clovekNaVrsti = false;
    public static Poteza namig;
    public static boolean pokaziPoteze;
    public static boolean neveljavnaPoteza = false;

    public static void igramoNovoIgro(VrstaIgralca vrstaIgralecCrni, VrstaIgralca vrstaIgralecBeli) {
        Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
        vrstaIgralca.put(Igralec.BLACK, vrstaIgralecCrni);
        vrstaIgralca.put(Igralec.WHITE, vrstaIgralecBeli);

        igra = new Igra();
        namig = null;
        igramo();
    }

    public static void igramo() {
        okno.osveziGUI();
        switch (igra.stanje()) {
            case ZMAGA_B:
                return;
            case ZMAGA_W:
                return;
            case NEODLOCENO:
                return;
            case BLOKIRANO:
                igra.odigraj(new Poteza(-10, -10));
                igramo();
                break;
            case V_TEKU:
                Igralec igralec = igra.naPotezi();
                VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
                switch (vrstaNaPotezi) {
                    case C -> clovekNaVrsti = true; // IgralnoPolje začne poskušati za mouseClicked dogodek
                    case R1 -> igrajRacunalnikovoPotezo(racunalnikovaInteligenca1);
                    case R2 -> igrajRacunalnikovoPotezo(racunalnikovaInteligenca2);
                    case R3 -> igrajRacunalnikovoPotezo(racunalnikovaInteligenca3);
                }
        }
    }

    ;
    public static Inteligenca racunalnikovaInteligenca1 = new NeumenIgralec(); // Neumen nasprotnik.
    public static Inteligenca racunalnikovaInteligenca2 = new AlphaBeta(6); // Pameten nasprotnik.
    public static Inteligenca racunalnikovaInteligenca3 = new MonteCarlo(3000); // Genialen nasprotnik.

    public static Inteligenca namigInteligenca = new MonteCarlo(1000); // Poišče namig.

    public static void igrajRacunalnikovoPotezo(Inteligenca racunalnikovaInteligenca) {
        Igra zacetnaIgra = igra;
        SwingWorker<Poteza, Void> worker = new SwingWorker<Poteza, Void>() {
            @Override
            protected Poteza doInBackground() {
                Poteza poteza = racunalnikovaInteligenca.izberiPotezo(igra);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                }
                ;
                return poteza;
            }

            @Override
            protected void done() {
                Poteza poteza = new Poteza(-10, -10);
                try {
                    poteza = get();
                } catch (Exception e) {
                }
                ;
                if (igra == zacetnaIgra) {
                    igra.odigraj(poteza);
                    igramo();
                }
            }
        };
        worker.execute();
    }

    public static void igrajClovekovoPotezo(Poteza poteza) {
		/* Se izvede po mouseClicked dogodku na IgralnoPolje. 
		Če smo kliknili na ustezno polje, odigramo izbrano potezo.
		Če izbrana poteza ni med možnimi potezami, to izpišemo v statusni vrstici.
		*/
        if (igra.odigraj(poteza)) {
            clovekNaVrsti = false;
            //ce smo igralcu prikazali ("vklopili") namig, naj se po njegovi potezi "izklopijo"
            namig = null;
            igramo();
        } else {
            neveljavnaPoteza = true;
            okno.osveziGUI();
        }
    }

    public static void pokaziNamig() {
		/* Izberi najboljšo možno potezo glede na trenutno stanje igre.
		Kot pomoč človeškemu igralcu. 
		Namig se izriše na IgralnemPolju v GlavnemOknu, če ni enak "null", kot
		določeno v IgralnoPolje.java. */
        namig = namigInteligenca.izberiPotezo(igra);
    }

    public static void pokaziPoteze() {
		/* Kot določeno v IgralnoPolje.java se možne poteze na IgralnemPolju v GlavnemOknu
		izrišejo, če je pokaziPoteze = true. */
        pokaziPoteze = true;
    }

    public static void skrijPoteze() {
		/* Kot določeno v IgralnoPolje.java se možne poteze na IgralnemPolju v GlavnemOknu
		izrišejo, če je pokaziPoteze = true. */
        pokaziPoteze = false;
    }

    public static void razveljaviPotezo() {
        /* Razveljavimo lahko največ eno potezo. */

        Igralec igralec = igra.naPotezi();
        VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
        if (vrstaNaPotezi == VrstaIgralca.C) igra.odstraniZadnjoPotezo();
        okno.osveziGUI();

    }


}
