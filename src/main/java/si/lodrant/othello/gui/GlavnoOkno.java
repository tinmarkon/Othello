package si.lodrant.othello.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import si.lodrant.othello.vodja.Vodja;
import si.lodrant.othello.vodja.VrstaIgralca;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.logika.Stanje;
import si.lodrant.othello.splosno.KdoIgra;

import javax.swing.*;

import java.awt.Container;

/*
 * Glavno okno aplikacije hrani trenutno stanje igre in nadzoruje potek
 * igre.
 * Nastavimo zgornji ToolBar (izbira igralcev in gumb za začetek igre),
 * ustvarimo novo IgralnoPolje,
 * nastavimo spodnji ToolBar (gumbi za prikaži namig, prikaži možne poteze, razveljavi zadnjo potezo) in
 * nastavimo vrstico za statusna sporočila na dnu okna.
 */

public class GlavnoOkno extends JFrame implements ActionListener {
    private final JPanel igralnoPolje;
    private final JPanel zacetniMeni;
    private final JPanel zahtevnostMeni;

    private String imeBeli;
    private String imeCrni;

    // Polje na katerem igramo igro.
    private IgralnoPolje polje;
    // Vrstici z gumbi na vrhu in dnu okna
    private JPanel zgornja_vrstica, p2;
    // Gumbi.
    private JButton zacniIgro, namig, razveljavi;
    private JToggleButton poteze;


    // Spremenljivki za JComboBox za izbiro igralca.
    protected JComboBox<VrstaIgralca> igralecCrni, igralecBeli;

    // Statusna vrstica v spodnjem delu okna.
    private JLabel status;

    // Izbrana vrsta črnega in belega igralca. Privzeto igra človek proti človeku.
    protected static VrstaIgralca izbira_igralecCrni = VrstaIgralca.C;
    protected static VrstaIgralca izbira_igralecBeli = VrstaIgralca.C;

    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    Font mojFont = new Font("Dialog", Font.PLAIN, 15);


    /**
     * Ustvari novo glavno okno in prični igrati igro.
     */
    public GlavnoOkno() {
        this.setTitle(Strings.TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        Container pane = this.getContentPane();

        this.igralnoPolje = ustvariIgralnoPolje();
        this.zacetniMeni = ustvariZacetniMeni();
        this.zahtevnostMeni = ustvariZahtevnostMeni();
        pane.add(igralnoPolje);
        pane.add(zacetniMeni);
        pane.add(zahtevnostMeni);
        izberiPogled(0);
    }

    private void izberiPogled(int i) {
        this.zacetniMeni.setVisible(i == 0);
        this.zahtevnostMeni.setVisible(i == 1);
        this.igralnoPolje.setVisible(i == 2);
        this.pack();
    }


    private JPanel ustvariZacetniMeni() {
        JPanel panel = new JPanel();
        JButton label = new JButton("hahaah");
        label.addActionListener((e) -> {
            izberiPogled(1);
        });
        panel.add(label);
        return panel;
    }

    private JPanel ustvariZahtevnostMeni() {
        JPanel panel = new JPanel();
        JButton label = new JButton("Polje");
        label.addActionListener((e) -> {
            izberiPogled(2);
        });
        panel.add(label);

        return panel;
    }

    private JPanel ustvariIgralnoPolje() {
        JPanel panel = new JPanel();
        JButton label = new JButton("Izhod");
        label.addActionListener((e) -> {
            izberiPogled(0);
        });
        panel.add(label);

        // ------------- igralno polje ----------------------
        polje = new IgralnoPolje();
        GridBagConstraints polje_layout = new GridBagConstraints();
        polje_layout.gridx = 0;
        polje_layout.gridy = 1;
        polje_layout.fill = GridBagConstraints.BOTH;
        polje_layout.anchor = GridBagConstraints.CENTER;
        polje_layout.weightx = 2.0;
        polje_layout.weighty = 2.0;
        panel.add(polje, polje_layout);

        // ---------- Spodnja vrstica z gumbi: [namig], [mozne poteze], [razveljavi potezo] ----------------
        p2 = new JPanel();

        GridBagConstraints panel2_layout = new GridBagConstraints();
        panel2_layout.gridx = 0;
        panel2_layout.gridy = 2;
        panel2_layout.weightx = 0.5;
        panel2_layout.fill = GridBagConstraints.HORIZONTAL;

        panel.add(p2, panel2_layout);

        namig = new JButton(Strings.NAMIG_GUMB);
        namig.addActionListener(namigGumbAL);
        namig.setToolTipText(
                "<html><font face=\"sansserif\" color=\"black\" >Prikaži najboljšo možno potezo, kot jo<br>izbere algoritem Monte Carlo Tree Search.<br>Iskanje traja 5 sekund!</font></html>");
        namig.setVisible(false); // radi bi, da se gumb namig (morda tudi moznePoteze) vidi le, kadar je na potezi človek

        poteze = new JToggleButton(Strings.POTEZE_GUMB);
        poteze.addActionListener(potezeGumbAL);

        razveljavi = new JButton(Strings.RAZVELJAVI_GUMB);
        razveljavi.addActionListener(razveljaviGumbAL);

        p2.add(namig);
        p2.add(poteze);
        p2.add(razveljavi);
        p2.setVisible(true);

        // ------- statusna vrstica za sporočila ------------
        status = new JLabel();

        status.setFont(mojFont);
        GridBagConstraints status_layout = new GridBagConstraints();
        status_layout.gridx = 0;
        status_layout.gridy = 3;
        status_layout.anchor = GridBagConstraints.CENTER;
        panel.add(status, status_layout);

        status.setText(Strings.START_STATUS);

        return panel;
    }


    // spodaj so definirani ActionListener-ji za posamezne gumbe
    ActionListener igralecCrniAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch ((String) igralecCrni.getSelectedItem()) {//check for a match
                case Strings.IGRALEC_C:
                    izbira_igralecCrni = VrstaIgralca.C;
                    break;
                case Strings.IGRALEC_R1:
                    izbira_igralecCrni = VrstaIgralca.R1;
                    break;
                case Strings.IGRALEC_R2:
                    izbira_igralecCrni = VrstaIgralca.R2;
                    break;
                case Strings.IGRALEC_R3:
                    izbira_igralecCrni = VrstaIgralca.R3;
                    break;
            }
        }
    };

    ActionListener igralecBeliAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch ((String) igralecBeli.getSelectedItem()) {
                case Strings.IGRALEC_C:
                    izbira_igralecBeli = VrstaIgralca.C;
                    break;
                case Strings.IGRALEC_R1:
                    izbira_igralecBeli = VrstaIgralca.R1;
                    break;
                case Strings.IGRALEC_R2:
                    izbira_igralecBeli = VrstaIgralca.R2;
                    break;
                case Strings.IGRALEC_R3:
                    izbira_igralecBeli = VrstaIgralca.R3;
                    break;
            }
        }
    };


    ActionListener namigGumbAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //status.setText("Iščem genialen namig!");
            //polje.repaint();
            Vodja.pokaziNamig();
            polje.repaint();
        }
    };

    ActionListener potezeGumbAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Vodja.pokaziPoteze();
            polje.repaint();
        }
    };

    ActionListener razveljaviGumbAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Vodja.razveljaviPotezo()
			/* Pazimo, da, če igra človek proti računalniku, razveljavi tako človekovo,
			 kot morebitno potezo, ki jo je že odigral računalnik.
			 (Če računalnik igra prehitro, bi vseeno radi dali človeku možnost, da si premisli...) */
        }
    };

    public void osveziGUI() {
        /* Pokliče se v metodi vodja.igramo(). */
        if (Vodja.igra == null) {
            status.setText("Igra ni v teku.");
        } else {
            namig.setVisible(false);
            Stanje stanjeIgre = Vodja.igra.stanje();
            Igralec naPotezi = Vodja.igra.naPotezi();
            KdoIgra imeNaPotezi = Vodja.kdoIgra.get(naPotezi);
            KdoIgra imeNasprotnik = Vodja.kdoIgra.get(naPotezi.nasprotnik());
            switch (stanjeIgre) {
                case BLOKIRANO:
                    System.out.println("Stanje na deski blokirano. Še enkrat je na vrsti nasprotnik!");
                    status.setText(imeNaPotezi.toString().substring(0, 1).toUpperCase()
                                           + imeNaPotezi.toString().substring(1) + " nima možnih potez. Še enkrat je na vrsti " + imeNasprotnik + "!");
                    break;

                case V_TEKU:
                    if (Vodja.vrstaIgralca.get(naPotezi) == VrstaIgralca.C) {
                        namig.setVisible(true);
                        if (Vodja.neveljavnaPoteza) {
                            status.setText("To ni možna poteza!");
                            Vodja.neveljavnaPoteza = false;
                        } else {
                            status.setText("Na potezi je " + naPotezi + " igralec - " + imeNaPotezi + ".");
                        }
                    } else {
                        status.setText("Potezo izbira " + naPotezi + " igralec - " + imeNaPotezi + ".");
                    }
                    break;

                case ZMAGA_B:
                    int[] st = Vodja.igra.prestejZetone();
                    status.setText("Igra je zaključena!");
                    JOptionPane.showMessageDialog(this, "Zmagal je črni - " + Vodja.kdoIgra.get(Igralec.BLACK) +
                            " - z rezultatom: " + st[0] + " | " + st[1] + ".");
                    status.setText(Strings.START_STATUS);
                    break;

                case ZMAGA_W:
                    int[] st1 = Vodja.igra.prestejZetone();
                    status.setText("Igra je zaključena!");
                    JOptionPane.showMessageDialog(this, "Zmagal je beli - " + Vodja.kdoIgra.get(Igralec.WHITE) +
                            " - z rezultatom: " + st1[1] + " | " + st1[0] + ".");
                    status.setText(Strings.START_STATUS);
                    break;

                case NEODLOCENO:
                    status.setText("Igra je zaključena!");
                    JOptionPane.showMessageDialog(this, stanjeIgre.toString());
                    status.setText(Strings.START_STATUS);
                    break;
            }
        }
        polje.repaint();
    }

    public void prikazi() {
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtils.nastaviLookAndFeel();
        GlavnoOkno glavno_okno = new GlavnoOkno();
        glavno_okno.prikazi();
        Vodja.igramoNovoIgro();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }

}