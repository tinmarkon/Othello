package si.lodrant.othello.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import si.lodrant.othello.vodja.Vodja;
import si.lodrant.othello.vodja.VrstaIgralca;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.logika.Stanje;

import javax.swing.*;

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
    private JLabel status;

    // Gumbi, ki jim spreminjamo visibility.
    private JButton namig;
    private IgralnoPolje polje;

    // ---------------------- barve ----------------------------
    private Color barvaOzadja = new Color(44, 144, 169);
    private final String barvaBesedila1 = "#121480";
    private final String barvaBesedila2 = "#0c27ad";
    private final String barvaBesedila3 = "#0c65ad";
    private final String barvaBesedilaIzhod = "#108fc2";
    private final String barvaBesedilaStart = "#27c1e8";

    // ---------------------- to posredujemo vodji -------------

    protected static VrstaIgralca izbira_igralecCrni;
    protected static VrstaIgralca izbira_igralecBeli;

    // ---------------------- fonti ----------------------------
    Font naslovFont = new Font("Dialog", Font.BOLD, 80);
    Font naslov2Font = new Font("Dialog", Font.PLAIN, 50);
    Font podnaslovFont = new Font("Dialog", Font.PLAIN, 25);
    Font mojFont = new Font("Dialog", Font.PLAIN, 18);


    public GlavnoOkno() {
        this.setTitle(Strings.TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        Container pane = this.getContentPane();
        pane.setBackground(barvaOzadja);

        this.igralnoPolje = ustvariIgralnoPolje();
        this.zacetniMeni = ustvariZacetniMeni();
        this.zahtevnostMeni = ustvariZahtevnostMeni();
        this.navodilaMeni = ustvariNavodilaMeni();

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.NONE;

        pane.add(igralnoPolje);
        pane.add(zacetniMeni, c);
        pane.add(zahtevnostMeni, c);

        pane.add(navodilaMeni);
        izberiPogled(0);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 800);
    }

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
        panel.setPreferredSize(new Dimension(700, 600));
        panel.setBackground(barvaOzadja);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;      //make this component tall
        c.ipadx = 40;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = 0;

        // ------------------ naslov ----------------------
        JLabel naslov = new JLabel("<html><font color=#ffffff><b>" + "Othello" + "</b></font>", JLabel.CENTER); //11aed1
        naslov.setFont(naslovFont);
        panel.add(naslov, c);

        JButton enIgralec = new HoverButton("<html><font color=" + barvaBesedila1 +"<b>" + Strings.EN_IGRALEC + "</b></font>", "big");
        c.gridy = 1;

        enIgralec.addActionListener((e) -> {
            izberiPogled(1);
        });
        panel.add(enIgralec, c);

        JButton dvaIgralca = new HoverButton("<html><font color=" + barvaBesedila2 +"<b>" + Strings.DVA_IGRALCA + "</b></font>",  "big");
        c.gridy = 2;

        dvaIgralca.addActionListener((e) -> {
            Vodja.igramoNovoIgro(VrstaIgralca.C, VrstaIgralca.C);
            izberiPogled(3);
        });
        panel.add(dvaIgralca, c);

        JButton navodila = new HoverButton("<html><font color=" + barvaBesedila3 +"<b>" + Strings.NAVODILA + "</b></font>",  "big");
        c.gridy = 3;

        navodila.addActionListener((e) -> {
            izberiPogled(2);
        });
        panel.add(navodila, c);

        JButton exit = new HoverButton("<html><font color=" + barvaBesedilaIzhod +"<b>" + Strings.IZHOD + "</b></font>",  "big");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 4;

        exit.addActionListener((e) -> {
            this.dispose();
        });
        panel.add(exit, c);

        JLabel podpis = new JLabel("<html><font color=#ffffff>" + "by Katka & Tin" + "</font>", JLabel.CENTER);
        Font podpisFont = new Font("Dialog", Font.PLAIN, 10);
        c.gridy = 5;
        podpis.setFont(podpisFont);
        panel.add(podpis, c);
        return panel;
    }

    private JPanel ustvariZahtevnostMeni() {
        /* En igralec
           Izberi svojo barvo
           [Črni igralec] [Beli igralec]
           Izberi inteligenco nasprotnik:
           [Povprečen] [Pameten] [Genialen]
           [Začni igro]
           [Izhod]
        */
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(700, 600));
        panel.setLayout(new GridBagLayout());
        panel.setBackground(barvaOzadja);

        GridBagConstraints c = new GridBagConstraints();
        c.ipady = 20;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 0;
        int vrstica = 0;

        // ------------------ naslov ----------------------
        JLabel naslov = new JLabel("<html><font color=#ffffff><b>" + "En igralec" + "</b></font>", JLabel.CENTER);
        c.ipady = 15;
        c.gridy = vrstica;
        naslov.setFont(naslov2Font);
        panel.add(naslov, c);

        // ----------------- izbira igralca ------------------------

        JLabel label1 = new JLabel("<html><font color=#ffffff>" + "Izberi svojo barvo:" + "</font>", JLabel.CENTER);
        label1.setFont(podnaslovFont);
        vrstica++;
        c.gridy = vrstica;
        panel.add(label1, c);

        JPanel izbiraIgralca = new JPanel();
        izbiraIgralca.setLayout(new GridBagLayout());
        izbiraIgralca.setBackground(barvaOzadja);
        c.ipady = 40;
        vrstica++;
        c.gridy = vrstica;
        panel.add(izbiraIgralca, c);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(0, 5, 5, 5);
        g.ipady = 40;
        g.ipadx = 40;

        MojToggleButton crniIgralec = new MojToggleButton("<html><font color=" + barvaBesedila1 +"<b>" + "ČRNI IGRALEC" + "</b></font>", true, "small");
        izbiraIgralca.add(crniIgralec, g);

        MojToggleButton beliIgralec = new MojToggleButton("<html><font color=" + barvaBesedila1 +"<b>" + "BELI IGRALEC" + "</b></font>", false, "small");
        izbiraIgralca.add(beliIgralec, g);

        crniIgralec.addActionListener((e) -> {
            beliIgralec.setSelected(!crniIgralec.isSelected());
            beliIgralec.setAlpha((!crniIgralec.isSelected()) ?  1f : 0.8f);
        });

        beliIgralec.addActionListener((e) -> {
            crniIgralec.setSelected(!beliIgralec.isSelected());
            crniIgralec.setAlpha((!beliIgralec.isSelected()) ?  1f : 0.8f);
        });

        // ----------------- izbira tezavnosti ------------------------

        JLabel label2 = new JLabel("<html><font color=#ffffff>" + "Izberi inteligenco nasprotnika:" + "</font>", JLabel.CENTER);
        label2.setFont(podnaslovFont);
        c.ipady = 15;
        vrstica++;
        c.gridy = vrstica;
        panel.add(label2, c);

        JPanel izbiraTezavnosti = new JPanel();
        izbiraTezavnosti.setLayout(new GridBagLayout());
        izbiraTezavnosti.setBackground(barvaOzadja);
        c.ipady = 40;
        vrstica++;
        c.gridy = vrstica;
        panel.add(izbiraTezavnosti, c);

        g.ipadx = 0;
        MojToggleButton tezavnost1 = new MojToggleButton("<html><font color=" + barvaBesedila2 +"<b>" + Strings.IGRALEC_R1 + "</b></font>", true, "small");
        izbiraTezavnosti.add(tezavnost1, g);

        MojToggleButton tezavnost2 = new MojToggleButton("<html><font color=" + barvaBesedila2 +"<b>" + Strings.IGRALEC_R2 + "</b></font>", false, "small");
        izbiraTezavnosti.add(tezavnost2, g);

        MojToggleButton tezavnost3 = new MojToggleButton("<html><font color=" + barvaBesedila2 +"<b>" + Strings.IGRALEC_R3 + "</b></font>", false, "small");
        izbiraTezavnosti.add(tezavnost3, g);

        tezavnost1.addActionListener((e) -> {
            tezavnost1.setSelected(true);
            tezavnost2.setSelected(false);
            tezavnost3.setSelected(false);
            tezavnost1.setAlpha(1f);
            tezavnost2.setAlpha(0.8f);
            tezavnost3.setAlpha(0.8f);


        });

        tezavnost2.addActionListener((e) -> {
            tezavnost1.setSelected(false);
            tezavnost2.setSelected(true);
            tezavnost3.setSelected(false);
            tezavnost1.setAlpha(0.8f);
            tezavnost2.setAlpha(1f);
            tezavnost3.setAlpha(0.8f);
        });

        tezavnost3.addActionListener((e) -> {
            tezavnost1.setSelected(false);
            tezavnost2.setSelected(false);
            tezavnost3.setSelected(true);
            tezavnost1.setAlpha(0.8f);
            tezavnost2.setAlpha(0.8f);
            tezavnost3.setAlpha(1f);
        });


        // ----------------- zacni igro ------------------------

        HoverButton zacniIgro = new HoverButton("<html><font color=" + barvaBesedilaStart + "<b>" + Strings.START_GUMB + "</b></font>", "medium");
        c.insets = new Insets(5, 5, 5, 0);
        c.ipady = 40;
        c.ipadx = 11;
        vrstica++;
        c.gridy = vrstica;

        zacniIgro.addActionListener((e) -> {
            VrstaIgralca vrstaRacunalnika = VrstaIgralca.R1;
            if (tezavnost2.isSelected()) vrstaRacunalnika = VrstaIgralca.R2;
            else if (tezavnost3.isSelected()) vrstaRacunalnika = VrstaIgralca.R3;
            izbira_igralecCrni = (crniIgralec.isSelected() ? VrstaIgralca.C : vrstaRacunalnika);
            izbira_igralecBeli = (!crniIgralec.isSelected() ? VrstaIgralca.C : vrstaRacunalnika);

            Vodja.igramoNovoIgro(izbira_igralecCrni, izbira_igralecBeli);
            izberiPogled(3);
        });

        panel.add(zacniIgro, c);

        // ----------------- izhod ------------------------
        
        HoverButton izhod = new HoverButton("<html><font color=" + barvaBesedilaIzhod + "<b>" + Strings.IZHOD + "</b></font>", "medium");
        vrstica++;
        c.gridy = vrstica;

        izhod.addActionListener((e) -> {
            izberiPogled(0);
        });
        
        panel.add(izhod, c);

        return panel;
    }

    private JPanel ustvariNavodilaMeni() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(Strings.NAVODILA);
        panel.add(label, BorderLayout.NORTH);
        // --------------------------------- navodila ----------------------------------------------
        
        JTextArea navodila = new JTextArea();
        navodila.setText(Strings.NAVODILA_TEXT);
        navodila.setColumns(20);
        navodila.setLineWrap(true);
        navodila.setRows(5);
        navodila.setWrapStyleWord(true);
        navodila.setEditable(false);
        panel.add(navodila, BorderLayout.CENTER);

        JButton izhodGumb = new JButton(Strings.IZHOD);
        izhodGumb.addActionListener((e) -> {
            izberiPogled(0);
        });
        panel.add(izhodGumb, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel ustvariIgralnoPolje() {
        /* [Črni igralec] [?zeton count?] [Beli igralec] [?zeton count?] [Vrsta nasprotnika]
           [IGRALNO POLJE]
           [namig][razveljavi][izhod] oz.. [ustavi igro][izhod]
           [statusna vrstica]
        */
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        // --------------------------------- igralno polje ----------------------------------------------
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 12;
        panel.add(statusIgralci, c);
        
        // --------------------------------- status igralcev  ----------------------------------------------
        
        JPanel crniIgralec = new JPanel(new BorderLayout());
        ImageIcon icon_crni = SwingUtils.createImageIcon("images/crni_small.png", "crni zeton");
        JLabel lab1 = new JLabel("Črni igralec:", icon_crni, JLabel.LEFT);
        crniIgralec.add(lab1, BorderLayout.LINE_START);
        
        JTextField igralec1 = new JTextField("Igralec 1");
        crniIgralec.add(igralec1, BorderLayout.LINE_END);
        
        
        JPanel beliIgralec = new JPanel(new BorderLayout());
        ImageIcon icon_beli = SwingUtils.createImageIcon("images/beli_small.png", "beli zeton");
        JLabel lab2 = new JLabel("Beli igralec:", icon_beli, JLabel.LEFT);
        beliIgralec.add(lab2, BorderLayout.LINE_START);
        
        JTextField igralec2 = new JTextField("Igralec 2");
        beliIgralec.add(igralec2, BorderLayout.LINE_END);
        
        statusIgralci.add(crniIgralec, BorderLayout.LINE_START);
        //statusIgralci.add(beliIgralec, BorderLayout.LINE_END);


        // --------------------------------- igralno polje ----------------------------------------------
        polje = new IgralnoPolje();
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 12;
        panel.add(polje, c);

        // ------ Spodnja vrstica z gumbi: [namig], [mozne poteze], [razveljavi potezo], [izhod] ----------------

        JPanel gumbi = new JPanel();
        c.gridx = 0;
        c.gridy = 2;
        panel.add(gumbi, c);
        
        namig = new JButton(Strings.NAMIG_GUMB);
        gumbi.add(namig);
        
       
        namig.addActionListener((e) -> {
            Vodja.pokaziNamig();
            polje.repaint();
        });
        namig.setToolTipText(
                "<html><font face=\"sansserif\" color=\"black\" >Prikaži najboljšo možno potezo, kot jo<br>izbere algoritem Monte Carlo Tree Search.<br>Iskanje traja 5 sekund!</font></html>");

        JButton razveljavi = new JButton(Strings.RAZVELJAVI_GUMB);
        gumbi.add(razveljavi);
        
        razveljavi.addActionListener((e) -> {
            Vodja.razveljaviPotezo();
            polje.repaint();
        });

        JButton izhod = new JButton(Strings.IZHOD);
        gumbi.add(izhod);
        
        
        izhod.addActionListener((e) -> {
                    izberiPogled(0);
                });

        // ------- statusna vrstica za sporočila ------------
        
        status = new JLabel();
        status.setFont(mojFont);
        status.setText(Strings.START_STATUS);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 12;
        panel.add(status, c);

      
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