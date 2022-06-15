package si.lodrant.othello.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import si.lodrant.othello.vodja.Vodja;
import si.lodrant.othello.vodja.VrstaIgralca;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.logika.Stanje;
import si.lodrant.othello.splosno.KdoIgra;

import javax.swing.*;

import static si.lodrant.othello.gui.SwingUtils.createImageIcon;

/*
 * Glavno okno aplikacije hrani trenutno stanje igre in nadzoruje potek
 * igre.
 * Nastavimo zgornji ToolBar (izbira igralcev in gumb za začetek igre),
 * ustvarimo novo IgralnoPolje,
 * nastavimo spodnji ToolBar (gumbi za prikaži namig, prikaži možne poteze, razveljavi zadnjo potezo) in
 * nastavimo vrstico za statusna sporočila na dnu okna.
 */

public class GlavnoOkno extends JFrame implements ActionListener {
    private final JPanel igralnoPolje, zacetniMeni, zahtevnostMeni, navodilaMeni;
    private Color barvaOzadja;

    private JLabel status;

    // Gumbi, ki jim spreminjamo visibility.
    private JButton namig;
    private IgralnoPolje polje;
    protected static VrstaIgralca izbira_igralecCrni;
    protected static VrstaIgralca izbira_igralecBeli;

    Font mojFont = new Font("Dialog", Font.PLAIN, 15);

    public GlavnoOkno() {
        this.setTitle(Strings.TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        Container pane = this.getContentPane();

        this.igralnoPolje = ustvariIgralnoPolje();
        this.zacetniMeni = ustvariZacetniMeni();
        this.zahtevnostMeni = ustvariZahtevnostMeni();
        this.navodilaMeni = ustvariNavodilaMeni();
        barvaOzadja = new Color(206, 234, 237);
        pane.setBackground(new Color(208, 233, 242));


        pane.add(igralnoPolje);
        pane.add(zacetniMeni);
        pane.add(zahtevnostMeni);
        pane.add(navodilaMeni);
        izberiPogled(0);
    }
    /*@Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 500);
    } */

    public static GridBagConstraints EnostavenLayout(int vrstica, int stolpec) {
        GridBagConstraints layout = new GridBagConstraints();
        layout.gridx = stolpec;
        layout.gridy = vrstica;
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.HORIZONTAL;
        return layout;
    }

    private void izberiPogled(int i) {
        /* Preklaplja med možnimi meniji. */
        this.zacetniMeni.setVisible(i == 0);
        this.zahtevnostMeni.setVisible(i == 1);
        this.navodilaMeni.setVisible(i == 2);
        this.igralnoPolje.setVisible(i == 3);
        this.pack();
    }


    private JPanel ustvariZacetniMeni() {
        /* Štirje gumbi: OTHELLO
                         [En igralec]
                         [Dva igralca]
                         [Navodila]
                         [Izhod]
        */
        JPanel panel = new JPanel();
        panel.setBackground(barvaOzadja);
        //panel.setPreferredSize(getPreferredSize());
        panel.setLayout(new GridBagLayout());

        // ------------------ naslovna slika ----------------------

        JPanel p0 = new JPanel();
        p0.setBackground(barvaOzadja);


        GridBagConstraints panel0_layout = new GridBagConstraints();
        panel0_layout.gridx = 0;
        panel0_layout.gridy = 0;
        panel0_layout.anchor = GridBagConstraints.NORTH;
        panel0_layout.fill = GridBagConstraints.CENTER;
        panel0_layout.gridwidth = 2;
        

        panel.add(p0, panel0_layout);

        //ImageIcon naslov = SwingUtils.createImageIcon("images/naslov.png", "naslov");
        JLabel lab0 = new JLabel("<html><font color=#11aed1><b>" + "Othello" + "</b></font>", JLabel.CENTER);
        Font naslovFont = new Font("Dialog", Font.PLAIN, 80);
        lab0.setFont(naslovFont);

        p0.add(lab0);
        

        // ------------------ Izberi igralce: ----------------------

        GridBagConstraints c = new GridBagConstraints();

        JButton enIgralec = new HoverButton("<html><font color=#2c90a9><b>" + Strings.EN_IGRALEC + "</b></font>", "big");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;

        enIgralec.addActionListener((e) -> {
            izberiPogled(1);
        });
        panel.add(enIgralec, c);

        JButton dvaIgralca = new HoverButton("<html><font color=#028bb5><b>" + Strings.DVA_IGRALCA + "</b></font>",  "big");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 2;

        dvaIgralca.addActionListener((e) -> {
            Vodja.igramoNovoIgro(VrstaIgralca.C, VrstaIgralca.C);
            izberiPogled(3);
        });
        panel.add(dvaIgralca, c);

        JButton navodila = new HoverButton("<html><font color=#0874c2><b>" + Strings.NAVODILA + "</b></font>",  "big");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 3;

        navodila.addActionListener((e) -> {
            izberiPogled(2);
        });
        panel.add(navodila, c);

        JButton exit = new HoverButton("<html><font color=#06528a><b>" + Strings.IZHOD + "</b></font>",  "big");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 4;

        exit.addActionListener((e) -> {
            this.dispose();
        });
        panel.add(exit, c);

        JLabel podpis = new JLabel("<html><font color=#ffffff>" + "by Katka & Tin" + "</font>", JLabel.CENTER);
        Font podpisFont = new Font("Dialog", Font.PLAIN, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 5;
        podpis.setFont(podpisFont);
        panel.add(podpis, c);
        return panel;
    }

    private JPanel ustvariZahtevnostMeni() {
        /* Igral bom: [Črnega igralca] [Belega igralca]
           Inteligenca nasprotnika: [Povprečen] [Pameten] [Genialen]
           [Začni igro]
           [Izhod]
        */
        JPanel panel = new JPanel();

        // ----------------- izbira igralca ------------------------
        JPanel izbiraIgralca = new JPanel();
        GridBagConstraints izbiraIgralca_layout = EnostavenLayout(0, 0);
        panel.add(izbiraIgralca, izbiraIgralca_layout);

        JLabel label1 = new JLabel("Jaz bom: ");

        JToggleButton crniIgralec = new JToggleButton(Strings.CRNI);
        JToggleButton beliIgralec = new JToggleButton(Strings.BELI);
        crniIgralec.setSelected(true);
        beliIgralec.setSelected(false);

        crniIgralec.addActionListener((e) -> {
            beliIgralec.setSelected(!crniIgralec.isSelected());
        });

        beliIgralec.addActionListener((e) -> {
            crniIgralec.setSelected(!beliIgralec.isSelected());
        });

        izbiraIgralca.add(label1);
        izbiraIgralca.add(crniIgralec);
        izbiraIgralca.add(beliIgralec);

        // ----------------- izbira tezavnosti ------------------------
        JPanel izbiraTezavnosti = new JPanel();
        GridBagConstraints izbiraTezavnosti_layout = EnostavenLayout(1, 0);
        panel.add(izbiraTezavnosti, izbiraTezavnosti_layout);

        JLabel label2 = new JLabel("Inteligenca nasprotnika: ");

        JToggleButton tezavnost1 = new JToggleButton(Strings.IGRALEC_R1);
        JToggleButton tezavnost2 = new JToggleButton(Strings.IGRALEC_R2);
        JToggleButton tezavnost3 = new JToggleButton(Strings.IGRALEC_R3);
        tezavnost1.setSelected(true);

        tezavnost1.addActionListener((e) -> {
            tezavnost1.setSelected(true);
            tezavnost2.setSelected(false);
            tezavnost3.setSelected(false);
        });

        tezavnost2.addActionListener((e) -> {
            tezavnost1.setSelected(false);
            tezavnost2.setSelected(true);
            tezavnost3.setSelected(false);
        });

        tezavnost3.addActionListener((e) -> {
            tezavnost1.setSelected(false);
            tezavnost2.setSelected(false);
            tezavnost3.setSelected(true);
        });

        izbiraTezavnosti.add(label2);
        izbiraTezavnosti.add(tezavnost1);
        izbiraTezavnosti.add(tezavnost2);
        izbiraTezavnosti.add(tezavnost3);

        // ----------------- zacni igro ------------------------
        GridBagConstraints zacniIgro_layout = EnostavenLayout(2, 0);

        JButton zacniIgro = new JButton(Strings.START_GUMB);
        zacniIgro.addActionListener((e) -> {
            VrstaIgralca vrstaRacunalnika = VrstaIgralca.R1;
            if (tezavnost2.isSelected()) vrstaRacunalnika = VrstaIgralca.R2;
            else if (tezavnost3.isSelected()) vrstaRacunalnika = VrstaIgralca.R3;
            izbira_igralecCrni = (crniIgralec.isSelected() ? VrstaIgralca.C : vrstaRacunalnika);
            izbira_igralecBeli = (!crniIgralec.isSelected() ? VrstaIgralca.C : vrstaRacunalnika);

            Vodja.igramoNovoIgro(izbira_igralecCrni, izbira_igralecBeli);
            izberiPogled(3);
        });

        panel.add(zacniIgro, zacniIgro_layout);

        // ----------------- izhod ------------------------
        //GridBagConstraints izhod_layout = EnostavenLayout(3, 0);

        JButton izhod = new JButton(Strings.IZHOD);
        izhod.addActionListener((e) -> {
            izberiPogled(0);
        });
        panel.add(izhod);

        return panel;
    }

    private JPanel ustvariNavodilaMeni() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(Strings.NAVODILA);
        panel.add(label);

        JButton izhodGumb = new JButton(Strings.IZHOD);
        izhodGumb.addActionListener((e) -> {
            izberiPogled(0);
        });
        panel.add(izhodGumb);

        return panel;
    }

    private JPanel ustvariIgralnoPolje() {
        /* [Črni igralec] [?zeton count?] [Beli igralec] [?zeton count?] [Vrsta nasprotnika]
           [IGRALNO POLJE]
           [namig][razveljavi][izhod] oz.. [ustavi igro][izhod]
           [statusna vrstica]
        */
        JPanel panel = new JPanel();
        JPanel igralci_panel = new JPanel();
        GridBagConstraints igralci_layout = EnostavenLayout(0, 0);
        panel.add(igralci_panel);

        JTextField igralec1 = new JTextField("Igralec 1");
        JTextField igralec2 = new JTextField("Igralec 2");

        ImageIcon icon_beli = createImageIcon("images/beli_small.png", "beli zeton");
        ImageIcon icon_crni = createImageIcon("images/crni_small.png", "crni zeton");

        JLabel lab1 = new JLabel("Črni igralec:", icon_crni, JLabel.LEFT);
        JLabel lab2 = new JLabel("Beli igralec:", icon_beli, JLabel.LEFT);

        igralci_panel.add(lab1);
        igralci_panel.add(igralec1);
        igralci_panel.add(lab2);
        igralci_panel.add(igralec2);

        // --------------------------------- igralno polje ----------------------------------------------
        polje = new IgralnoPolje();
        GridBagConstraints polje_layout = EnostavenLayout(1, 0);
        panel.add(polje);

        // ------ Spodnja vrstica z gumbi: [namig], [mozne poteze], [razveljavi potezo], [izhod] ----------------
        JPanel spodnjiGumbi = new JPanel();

        GridBagConstraints spodnjiGumbi_layout = EnostavenLayout(2, 0);
        panel.add(spodnjiGumbi);

        namig = new JButton(Strings.NAMIG_GUMB);
        namig.addActionListener((e) -> {
            Vodja.pokaziNamig();
            polje.repaint();
        });
        namig.setToolTipText(
                "<html><font face=\"sansserif\" color=\"black\" >Prikaži najboljšo možno potezo, kot jo<br>izbere algoritem Monte Carlo Tree Search.<br>Iskanje traja 5 sekund!</font></html>");

        JButton razveljavi = new JButton(Strings.RAZVELJAVI_GUMB);
        razveljavi.addActionListener((e) -> {
            //Vodja.razveljaviPotezo();
            polje.repaint();
        });

        JButton izhod = new JButton(Strings.IZHOD);
        izhod.addActionListener((e) -> {
                    izberiPogled(0);
                });
        spodnjiGumbi.add(namig);
        spodnjiGumbi.add(razveljavi);
        spodnjiGumbi.add(izhod);

        // ------- statusna vrstica za sporočila ------------
        status = new JLabel();

        status.setFont(mojFont);
        GridBagConstraints status_layout = EnostavenLayout(3, 0);
        panel.add(spodnjiGumbi, spodnjiGumbi_layout);

        panel.add(status);

        status.setText(Strings.START_STATUS);

        return panel;
    }

    public void osveziGUI() {
        /* Pokliče se v metodi vodja.igramo(). */
        if (Vodja.igra == null) {
            status.setText("Igra ni v teku.");
        } else {
            namig.setVisible(false);
            Stanje stanjeIgre = Vodja.igra.stanje();
            Igralec naPotezi = Vodja.igra.naPotezi();
            String imeNaPotezi = naPotezi.toString();
            String imeNasprotnik = naPotezi.nasprotnik().toString();
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
                            status.setText("Na potezi je " + naPotezi + " igralec."); //- " + imeNaPotezi + ".");
                        }
                    } else {
                        status.setText("Potezo izbira " + naPotezi + " igralec."); //- " + imeNaPotezi + ".");
                    }
                    break;

                case ZMAGA_B:
                    int[] st = Vodja.igra.prestejZetone();
                    status.setText("Igra je zaključena!");
                    JOptionPane.showMessageDialog(this, "Zmagal je ČRNI igralec z rezultatom: " + st[0] + " | " + st[1] + ".");
                    status.setText(Strings.START_STATUS);
                    break;

                case ZMAGA_W:
                    int[] st1 = Vodja.igra.prestejZetone();
                    status.setText("Igra je zaključena!");
                    JOptionPane.showMessageDialog(this, "Zmagal je BELI igralec z rezultatom: " + st1[1] + " | " + st1[0] + ".");
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

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }

}