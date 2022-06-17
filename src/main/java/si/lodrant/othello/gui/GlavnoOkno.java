package si.lodrant.othello.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import si.lodrant.othello.vodja.Vodja;
import si.lodrant.othello.vodja.VrstaIgralca;
import si.lodrant.othello.logika.Igralec;
import si.lodrant.othello.logika.Stanje;

import javax.swing.*;

/*
 * Glavno okno aplikacije sestavlja celoten uporabniški vmesnik. S funkcijo izberiPogled lahko izmenjujemo med
 * 4 različnimi pogledi/meniji - JPanel-i:
 * ZAČETNI MENI,
 * ZAHTEVNOST MENI: nastavitve igre igro Človek-Računalnik
 * IGRALNO POLJE: okno kjer igramo igro
 * NAVODILA: kratka navodila za igro Othello.
 * Za Layout uporabljamo GridBagLayout.
 */

public class GlavnoOkno extends JFrame implements ActionListener {
    // štiri glavne konfiguracije menijev med katerimi preklapljamo
    private final JPanel igralnoPolje, zacetniMeni, zahtevnostMeni, navodilaMeni;

    // Teksti ob igralnem polju, ki jih posodabljamo v osveziGUI()
    private JLabel status, crniLabel, beliLabel, beliZetoni, crniZetoni;

    // Gumbi, ki jim spreminjamo visibility.
    private HoverButton namig, razveljavi;
    private IgralnoPolje polje;
    private Boolean igraClovekClovek; //za prikaz funkcij, ki so specificne igri clovek-clovek oz. clovek-racunalnik

    // ---------------------- barve ----------------------------
    private final Color barvaOzadja = new Color(44, 144, 169);
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
    Font mojFont = new Font("Dialog", Font.PLAIN, 15);

    // osnovna dimenzija okna
    Dimension dimenzijaOkna = new Dimension(1000, 800);
    Dimension dimenzijaPolja = new Dimension(400, 400);

    public GlavnoOkno() {
        this.setTitle(Strings.NASLOV);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        izbira_igralecCrni = VrstaIgralca.C;
        izbira_igralecBeli = VrstaIgralca.C;

        this.igralnoPolje = ustvariIgralnoPolje();
        this.zacetniMeni = ustvariZacetniMeni();
        this.zahtevnostMeni = ustvariZahtevnostMeni();
        this.navodilaMeni = ustvariNavodilaMeni();

        Container pane = this.getContentPane();
        pane.setBackground(barvaOzadja);
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        pane.add(igralnoPolje, c);
        pane.add(zacetniMeni, c);
        pane.add(zahtevnostMeni, c);
        pane.add(navodilaMeni, c);

        izberiPogled(0); // zacetni meni
        this.setMinimumSize(dimenzijaOkna);
    }

    @Override
    public Dimension getPreferredSize() {
        return dimenzijaOkna;
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
        JLabel naslov = new JLabel("<html><font color=#ffffff><b>" + Strings.NASLOV + "</b></font>", JLabel.CENTER); //11aed1
        naslov.setFont(naslovFont);
        panel.add(naslov, c);

        HoverButton enIgralec = new HoverButton("<html><font color=" + barvaBesedila1 + "<b>" + Strings.EN_IGRALEC + "</b></font>", "big");
        c.gridy = 1;

        enIgralec.addActionListener((e) -> {
            igraClovekClovek = false;
            izberiPogled(1);
        });
        panel.add(enIgralec, c);

        HoverButton dvaIgralca = new HoverButton("<html><font color=" + barvaBesedila2 + "<b>" + Strings.DVA_IGRALCA + "</b></font>", "big");
        c.gridy = 2;

        dvaIgralca.addActionListener((e) -> {
            // S klikom na Dva igralca takoj začnemo novo igro
            igraClovekClovek = true;
            izbira_igralecCrni = VrstaIgralca.C;
            izbira_igralecBeli = VrstaIgralca.C;
            Vodja.igramoNovoIgro(izbira_igralecCrni, izbira_igralecBeli);
            izberiPogled(3);
        });
        panel.add(dvaIgralca, c);

        HoverButton navodila = new HoverButton("<html><font color=" + barvaBesedila3 + "<b>" + Strings.NAVODILA + "</b></font>", "big");
        c.gridy = 3;

        navodila.addActionListener((e) -> {
            izberiPogled(2);
        });
        panel.add(navodila, c);

        HoverButton exit = new HoverButton("<html><font color=" + barvaBesedilaIzhod + "<b>" + Strings.IZHOD + "</b></font>", "big");
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
        //panel.setPreferredSize(new Dimension(700, 600));
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
        //panel.add(naslov, c);

        // ----------------- izbira igralca ------------------------

        JLabel label1 = new JLabel("<html><font color=#ffffff>" + "Izberi svojo barvo:" + "</font>", JLabel.CENTER);
        label1.setFont(podnaslovFont);
        //vrstica++;
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

        MojToggleButton crniIgralec = new MojToggleButton("<html><font color=" + barvaBesedila1 + "<b>" + "ČRNI IGRALEC" + "</b></font>", true, "small");
        izbiraIgralca.add(crniIgralec, g);

        MojToggleButton beliIgralec = new MojToggleButton("<html><font color=" + barvaBesedila1 + "<b>" + "BELI IGRALEC" + "</b></font>", false, "small");
        izbiraIgralca.add(beliIgralec, g);

        crniIgralec.addActionListener((e) -> {
            beliIgralec.setSelected(!crniIgralec.isSelected());
            beliIgralec.setAlpha((!crniIgralec.isSelected()) ? 1f : 0.8f);
        });

        beliIgralec.addActionListener((e) -> {
            crniIgralec.setSelected(!beliIgralec.isSelected());
            crniIgralec.setAlpha((!beliIgralec.isSelected()) ? 1f : 0.8f);
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
        MojToggleButton tezavnost1 = new MojToggleButton("<html><font color=" + barvaBesedila2 + "<b>" + Strings.IGRALEC_R1 + "</b></font>", true, "small");
        izbiraTezavnosti.add(tezavnost1, g);

        MojToggleButton tezavnost2 = new MojToggleButton("<html><font color=" + barvaBesedila2 + "<b>" + Strings.IGRALEC_R2 + "</b></font>", false, "small");
        izbiraTezavnosti.add(tezavnost2, g);

        MojToggleButton tezavnost3 = new MojToggleButton("<html><font color=" + barvaBesedila2 + "<b>" + Strings.IGRALEC_R3 + "</b></font>", false, "small");
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
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(5, 5, 5, 0);
        c.ipady = 40;
        c.ipadx = 40;
        vrstica++;
        c.gridy = vrstica;

        zacniIgro.addActionListener((e) -> {
            VrstaIgralca vrstaRacunalnika = VrstaIgralca.R1;
            if (tezavnost2.isSelected()) vrstaRacunalnika = VrstaIgralca.R2;
            else if (tezavnost3.isSelected()) vrstaRacunalnika = VrstaIgralca.R3;
            izbira_igralecCrni = (crniIgralec.isSelected() ? VrstaIgralca.C : vrstaRacunalnika);
            izbira_igralecBeli = (beliIgralec.isSelected() ? VrstaIgralca.C : vrstaRacunalnika);

            Vodja.igramoNovoIgro(izbira_igralecCrni, izbira_igralecBeli);
            izberiPogled(3);
        });

        panel.add(zacniIgro, c);

        // ----------------- izhod ------------------------

        HoverButton izhod = new HoverButton("<html><font color=" + barvaBesedilaIzhod + "<b>" + Strings.IZHOD + "</b></font>", "small");
        vrstica++;
        c.ipady = 20;
        c.ipadx = 20;
        c.gridy = vrstica;

        izhod.addActionListener((e) -> {
            izberiPogled(0);
        });

        panel.add(izhod, c);

        return panel;
    }

    private JTextArea ustvariTekst() {
        // Nastavi format odstavka teksta za navodila
        JTextArea tekst = new JTextArea();
        tekst.setFont(mojFont);
        tekst.setBackground(barvaOzadja);
        tekst.setForeground(Color.WHITE);
        //tekst.setPreferredSize(new Dimension(600, 200));
        tekst.setLineWrap(true);
        tekst.setWrapStyleWord(true);
        tekst.setEditable(false);
        return tekst;

    }

    private JPanel ustvariNavodilaMeni() {
        JPanel panel = new JPanel();
        //panel.setPreferredSize(new Dimension(700, 600));
        panel.setLayout(new GridBagLayout());
        panel.setBackground(barvaOzadja);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        int vrstica = 0;

        JLabel naslov = new JLabel("<html><font color=#ffffff><b>" + "Kako igram igro Othello?" + "</b></font>", JLabel.CENTER);
        c.ipady = 15;
        c.gridy = vrstica;
        naslov.setFont(naslov2Font);
        panel.add(naslov, c);

        JTextArea navodila = ustvariTekst();
        navodila.setText(Strings.NAVODILA_TEXT);
        vrstica++;
        c.ipady = 10;
        c.gridy = vrstica;
        panel.add(navodila, c);

        JTextArea navodila2 = ustvariTekst();
        navodila2.setText(Strings.NAVODILA_TEXT2);
        vrstica++;
        c.gridy = vrstica;
        panel.add(navodila2, c);

        JPanel sliki = new JPanel(new GridBagLayout());
        sliki.setBackground(barvaOzadja);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0, 30, 0, 30);

        ImageIcon navodilaIcon1 = SwingUtils.createImageIcon("images/zacetek.png", "zacetna pozicija");
        //.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel navodilaImage1 = new JLabel(new ImageIcon(navodilaIcon1.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));

        ImageIcon navodilaIcon2 = SwingUtils.createImageIcon("images/prva_poteza.png", "pozicija po prvi potezi");
        //.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel navodilaImage2 = new JLabel(new ImageIcon(navodilaIcon2.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));

        g.gridx = 0;
        sliki.add(navodilaImage1, g);

        g.gridx = 1;
        sliki.add(navodilaImage2, g);

        vrstica++;
        c.fill = GridBagConstraints.NONE;
        c.gridy = vrstica;
        c.ipady = 20;
        panel.add(sliki, c);

        JLabel labelRazveljavi = new JLabel("<html><font color=#ffffff><b>" + "Razveljavi: " + "</b></font>", JLabel.CENTER);
        labelRazveljavi.setFont(mojFont);
        vrstica++;
        c.gridy = vrstica;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.ipady = 15;
        panel.add(labelRazveljavi, c);

        JTextArea navodila4 = ustvariTekst();
        navodila4.setText(Strings.NAVODILA_TEXT_RAZVELJAVI);
        vrstica++;
        c.gridy = vrstica;
        panel.add(navodila4, c);

        JLabel labelNamig = new JLabel("<html><font color=#ffffff><b>" + "Namig: " + "</b></font>", JLabel.CENTER);
        labelNamig.setFont(mojFont);
        vrstica++;
        c.gridy = vrstica;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.ipady = 15;
        panel.add(labelNamig, c);

        JTextArea navodila5 = ustvariTekst();
        navodila5.setText(Strings.NAVODILA_TEXT_NAMIG);
        vrstica++;
        c.gridy = vrstica;
        panel.add(navodila5, c);

        HoverButton izhod = new HoverButton("<html><font color=" + barvaBesedilaIzhod + "<b>" + Strings.IZHOD + "</b></font>", "small");
        vrstica++;
        c.fill = GridBagConstraints.NONE;
        c.ipady = 20;
        c.ipadx = 20;
        c.gridy = vrstica;

        izhod.addActionListener((e) -> {
            izberiPogled(0);
        });

        panel.add(izhod, c);

        return panel;
    }

    private JPanel ustvariIgralnoPolje() {
        /* [Črni igralec] [?zeton count?] [Beli igralec] [?zeton count?]
           [IGRALNO POLJE]
           [namig][razveljavi]
           [statusna vrstica]
           [izhod]
        */
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(barvaOzadja);
        GridBagConstraints c = new GridBagConstraints();
        int vrstica = 0;
        c.fill = GridBagConstraints.VERTICAL;
        c.anchor = GridBagConstraints.CENTER;

        // --------------------------------- status igralcev  ----------------------------------------------
        JPanel panelCrni = new JPanel(new GridBagLayout()); // zeton + ime + stevilo zetonov crnega
        panelCrni.setBackground(barvaOzadja);
        GridBagConstraints f = new GridBagConstraints();
        f.fill = GridBagConstraints.NONE;
        f.anchor = GridBagConstraints.CENTER;
        f.ipadx = 20;

        JPanel panelBeli = new JPanel(new GridBagLayout()); // zeton + ime + stevilo zetonov belega
        panelBeli.setBackground(barvaOzadja);

        JPanel igralca = new JPanel();
        igralca.setBackground(barvaOzadja);

        ImageIcon icon_crni = SwingUtils.createImageIcon("images/crni_small.png", "crni zeton");
        JLabel crniIconLabel = new JLabel(icon_crni, JLabel.CENTER); // zeton
        crniLabel = new JLabel(izbira_igralecCrni.toString(), JLabel.CENTER); // ime
        crniZetoni = new JLabel("st. zetonov", JLabel.CENTER); // steviloZetonov
        crniLabel.setFont(mojFont);
        crniZetoni.setFont(mojFont);

        ImageIcon icon_beli = SwingUtils.createImageIcon("images/beli_small.png", "beli zeton");
        JLabel beliIconLabel = new JLabel(icon_beli, JLabel.CENTER); // zeton
        beliLabel = new JLabel(izbira_igralecBeli.toString(), JLabel.CENTER); // ime
        beliZetoni = new JLabel("st. zetonov", JLabel.CENTER); // steviloZetonov
        beliLabel.setFont(mojFont);
        beliZetoni.setFont(mojFont);
        beliLabel.setBackground(barvaOzadja);
        beliLabel.setForeground(Color.WHITE);
        beliZetoni.setBackground(barvaOzadja);
        beliZetoni.setForeground(Color.WHITE);

        f.gridy = 0;
        panelCrni.add(crniIconLabel, f);
        panelBeli.add(beliIconLabel, f);
        f.gridy = 1;
        panelCrni.add(crniLabel, f);
        panelBeli.add(beliLabel, f);
        f.gridy = 2;
        panelCrni.add(crniZetoni, f);
        panelBeli.add(beliZetoni, f);

        c.gridy = 0;
        c.gridx = 0;
        c.ipady = 20;
        igralca.add(panelCrni);
        igralca.add(panelBeli);
        panel.add(igralca, c);
        // ------------------------------- igralno polje -------------------------------
        polje = new IgralnoPolje(dimenzijaPolja);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        vrstica++;
        c.gridy = vrstica;
        panel.add(polje, c);

        // ------ spodnja vrstica z gumbi: [namig], [razveljavi potezo] ----------------
        JPanel gumbi = new JPanel();
        gumbi.setLayout(new GridBagLayout());
        gumbi.setBackground(barvaOzadja);
        c.insets = new Insets(10, 10, 10, 10);
        c.ipady = 40;
        c.gridx = 0;
        vrstica++;
        c.gridy = vrstica;
        panel.add(gumbi, c);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(0, 5, 0, 5);
        g.ipady = 20;
        g.ipadx = 20;
        g.gridx = 0;

        namig = new HoverButton("<html><font color=" + barvaBesedila2 + "<b>" + Strings.NAMIG_GUMB + "</b></font>", "small");
        gumbi.add(namig, g);

        namig.addActionListener((e) -> {
            Vodja.pokaziNamig();
            polje.repaint();
        });
        namig.setToolTipText(
                "<html><font face=\"sansserif\" color=\"black\" >Prikaži najboljšo možno potezo, kot jo<br>izbere algoritem Monte Carlo Tree Search.<br>Iskanje traja 5 sekund!</font></html>");

        razveljavi = new HoverButton("<html><font color=" + barvaBesedila3 + "<b>" + Strings.RAZVELJAVI_GUMB + "</b></font>", "small");
        g.gridx = 1;
        gumbi.add(razveljavi, g);
        razveljavi.setVisible(false);
        razveljavi.setEnabled(false);

        razveljavi.addActionListener((e) -> {
            Vodja.razveljaviPotezo();
            polje.repaint();
        });

        // ------------------------ statusna vrstica za sporočila --------------------------
        status = new JLabel();
        status.setFont(mojFont);
        status.setForeground(Color.WHITE);
        status.setText(Strings.START_STATUS);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.ipady = 20;
        vrstica++;
        c.gridy = vrstica;
        panel.add(status, c);

        // -------------------------------- [izhod] -----------------------------------------
        HoverButton izhod = new HoverButton("<html><font color=" + barvaBesedilaIzhod + "<b>" + Strings.IZHOD + "</b></font>", "small");
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.ipady = 20;
        c.ipadx = 20;
        vrstica++;
        c.gridy = vrstica;
        panel.add(izhod, c);

        izhod.addActionListener((e) -> {
            izberiPogled(0);
        });

        return panel;
    }

    public void osveziGUI() {
        /* Pokliče se v metodi vodja.igramo(). Osvežuje zapise v statusne vrstici, število žetonov in aktivnosti gumbov.*/
        razveljavi.setVisible(igraClovekClovek);
        int[] st = Vodja.igra.prestejZetone();
        String poravnava1 = "";
        String poravnava2 = "";
        if (st[0] < 10) poravnava1 = " ";
        else poravnava1 = "";
        if (st[1] < 10) poravnava2 = " ";
        else poravnava2 = "";

        crniLabel.setText("<html><b>" + izbira_igralecCrni.toString() + "</b>");
        crniZetoni.setText("št. žetonov: " + st[0] + poravnava1);
        beliLabel.setText("<html><b>" + izbira_igralecBeli.toString() + "</b>");
        beliZetoni.setText("št. žetonov: " + st[1] + poravnava2);

        razveljavi.setEnabled((st[0] + st[1] > 4) && (Vodja.igra.jeVeljavnaZadnjaPoteza()));

        if (Vodja.igra == null) {
            status.setText("Igra ni v teku.");
        } else {
            namig.setEnabled(false);
            Stanje stanjeIgre = Vodja.igra.stanje();
            Igralec naPotezi = Vodja.igra.naPotezi();
            String imeNaPotezi = naPotezi.toString();
            String imeNasprotnik = naPotezi.nasprotnik().toString();
            switch (stanjeIgre) {
                case BLOKIRANO:
                    System.out.println("Stanje na deski blokirano. Še enkrat je na vrsti nasprotnik!");
                    status.setText(imeNaPotezi.toString().substring(0, 1).toUpperCase()
                            + imeNaPotezi.toString().substring(1) +"igralec nima možnih potez. Še enkrat je na vrsti " + imeNasprotnik + "!");
                    break;

                case V_TEKU:
                    if (Vodja.vrstaIgralca.get(naPotezi) == VrstaIgralca.C) {
                        namig.setEnabled(true);
                        if (Vodja.neveljavnaPoteza) {
                            status.setText("To ni možna poteza!");
                            Vodja.neveljavnaPoteza = false;
                        } else {
                            status.setText("Na potezi je " + naPotezi + " igralec."); //- " + imeNaPotezi + ".");
                        }
                    } else {
                        status.setText("Potezo izbira " + naPotezi + " igralec. To lahko traja nekaj sekund."); //- " + imeNaPotezi + ".");
                    }
                    break;

                case ZMAGA_B:
                    status.setText("Igra je zaključena!");
                    JOptionPane.showMessageDialog(this, "Zmagal je ČRNI igralec z rezultatom: " + st[0] + " | " + st[1] + ".");
                    break;

                case ZMAGA_W:
                    status.setText("Igra je zaključena!");
                    JOptionPane.showMessageDialog(this, "Zmagal je BELI igralec z rezultatom: " + st[1] + " | " + st[0] + ".");
                    break;

                case NEODLOCENO:
                    status.setText("Igra je zaključena!");
                    JOptionPane.showMessageDialog(this, stanjeIgre.toString());
                    break;
            }
        }
        polje.repaint();
    }

    public void prikazi() {
        // S tem prikažemo cel JFrame v metodi main
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }

}