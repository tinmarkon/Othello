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
    private JLabel tekstCrni;
    private JLabel tekstBeli;

    // Gumbi, ki jim spreminjamo visibility.
    private HoverButton namig;
    private HoverButton razveljavi;
 
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
    Font navodilaFont = new Font("Dialog", Font.PLAIN, 20);

    private Boolean igraClovekClovek;

    public GlavnoOkno() {
        this.setTitle(Strings.TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        Container pane = this.getContentPane();
        pane.setBackground(barvaOzadja);
        
        izbira_igralecCrni = VrstaIgralca.C;
        izbira_igralecBeli = VrstaIgralca.C;
        
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
        
        HoverButton enIgralec = new HoverButton("<html><font color=" + barvaBesedila1 +"<b>" + Strings.EN_IGRALEC + "</b></font>", "big");
        c.gridy = 1;

        enIgralec.addActionListener((e) -> {
        	igraClovekClovek = false;
            izberiPogled(1);
        });
        panel.add(enIgralec, c);

        HoverButton dvaIgralca = new HoverButton("<html><font color=" + barvaBesedila2 +"<b>" + Strings.DVA_IGRALCA + "</b></font>",  "big");
        c.gridy = 2;

        dvaIgralca.addActionListener((e) -> {
        	igraClovekClovek = true;
            izbira_igralecCrni = VrstaIgralca.C;
            izbira_igralecBeli = VrstaIgralca.C;
            Vodja.igramoNovoIgro(izbira_igralecCrni, izbira_igralecBeli);
            izberiPogled(3);
        });
        panel.add(dvaIgralca, c);

        HoverButton navodila = new HoverButton("<html><font color=" + barvaBesedila3 +"<b>" + Strings.NAVODILA + "</b></font>",  "big");
        c.gridy = 3;

        navodila.addActionListener((e) -> {
            izberiPogled(2);
        });
        panel.add(navodila, c);

        HoverButton exit = new HoverButton("<html><font color=" + barvaBesedilaIzhod +"<b>" + Strings.IZHOD + "</b></font>",  "big");
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
        JPanel panel = new JPanel(new BorderLayout(0, 50));
        panel.setBackground(barvaOzadja);
        
        // --------------------------------- navodila ----------------------------------------------
        
        JLabel naslovNavodila = new JLabel("<html><font color=#ffffff><b>" + Strings.NAVODILA + "</b></font>", JLabel.CENTER);
        naslovNavodila.setFont(naslov2Font);
        panel.add(naslovNavodila, BorderLayout.PAGE_START);
        
        JTextArea navodila = new JTextArea();
        navodila.setText(Strings.NAVODILA_TEXT);
        navodila.setFont(navodilaFont);
        navodila.setBackground(barvaOzadja);
        navodila.setForeground(Color.WHITE);
        navodila.setPreferredSize(new Dimension(400, 400));
        navodila.setColumns(20);
        navodila.setLineWrap(true);
        navodila.setRows(5);
        navodila.setWrapStyleWord(true);
        navodila.setEditable(false);
        
        
        
        panel.add(navodila, BorderLayout.CENTER);
       

        HoverButton izhodGumb = new HoverButton("<html><font color=" + barvaBesedila2 + "<b>" + Strings.IZHOD + "</b></font>", "small");
        izhodGumb.addActionListener((e) -> {
            izberiPogled(0);
        });
        panel.add(izhodGumb, BorderLayout.PAGE_END);

        return panel;
    }

    private JPanel ustvariIgralnoPolje() {
        /* [Črni igralec] [?zeton count?] [Beli igralec] [?zeton count?] [Vrsta nasprotnika]
           [IGRALNO POLJE]
           [namig][razveljavi][izhod] oz.. [ustavi igro][izhod]
           [statusna vrstica]
        */
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(barvaOzadja);
        GridBagConstraints c = new GridBagConstraints();
        
        // --------------------------------- status igralcev  ----------------------------------------------
        
        JPanel crniIgralec = new JPanel();
        crniIgralec.setBackground(barvaOzadja);
        
        ImageIcon icon_crni = SwingUtils.createImageIcon("images/crni_small.png", "crni zeton");
        tekstCrni = new JLabel("Črni igralec:" + izbira_igralecCrni.toString(), icon_crni, JLabel.LEFT);
        crniIgralec.add(tekstCrni);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 12;
        panel.add(crniIgralec, c);
        
        
        JPanel beliIgralec = new JPanel();
        beliIgralec.setBackground(barvaOzadja);
        ImageIcon icon_beli = SwingUtils.createImageIcon("images/beli_small.png", "beli zeton");
        tekstBeli = new JLabel("Beli igralec:" + izbira_igralecBeli.toString(), icon_beli, JLabel.RIGHT);
        tekstBeli.setForeground(Color.WHITE);
        beliIgralec.add(tekstBeli);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 12;
        panel.add(beliIgralec, c);
      


        // --------------------------------- igralno polje ----------------------------------------------
        polje = new IgralnoPolje();
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 12;
        panel.add(polje, c);

     // ------ Spodnja vrstica z gumbi: [namig], [mozne poteze], [razveljavi potezo], [izhod] ----------------
        JPanel gumbi = new JPanel();
        gumbi.setLayout(new GridBagLayout());
        gumbi.setBackground(barvaOzadja);
        c.ipady = 40;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(gumbi, c);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
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
        razveljavi.setEnabled(false);
        g.gridx = 1;
        gumbi.add(razveljavi, g);
        razveljavi.setVisible(false);
        
        razveljavi.addActionListener((e) -> {
            Vodja.razveljaviPotezo();
            polje.repaint();
        });

        // ------- statusna vrstica za sporočila ------------
        
        status = new JLabel();
        status.setFont(mojFont);
        status.setForeground(Color.WHITE);
        status.setText(Strings.START_STATUS);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.ipady = 40;
        c.gridy = 4;
        c.gridwidth = 12;
        panel.add(status, c);

        HoverButton izhod = new HoverButton("<html><font color=" + barvaBesedilaIzhod + "<b>" + Strings.IZHOD + "</b></font>", "small");
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.ipady = 20;
        c.ipadx = 20;
        c.gridy = 5;
        panel.add(izhod, c);

        izhod.addActionListener((e) -> {
            izberiPogled(0);
        });

      
        return panel;
    }

    public void osveziGUI() {
        /* Pokliče se v metodi vodja.igramo(). */
    	razveljavi.setVisible(igraClovekClovek);
        int[] st = Vodja.igra.prestejZetone();
        String zeton1 = "žeton";
        if (st[0] == 2) zeton1 = "žetona";
        else if ((st[0] < 5) && (st[0] > 2)) zeton1 = "žetone";
        else if (st[0] > 4) zeton1 = "žetonov";
        String zeton2 = "žeton";
        if (st[1] == 2) zeton2 = "žetona";
        else if ((st[1] < 5) && (st[1] > 2)) zeton2 = "žetone";
        else if (st[1] > 4) zeton2 = "žetonov";

        tekstCrni.setText("<html><b>" + izbira_igralecCrni.toString()+ "</b>" + " ima " + st[0] + " " + zeton1 + ".");
        tekstBeli.setText("<html><b>" + izbira_igralecBeli.toString()+ "</b>" + " ima " + st[1] + " " + zeton2 + ".");
        
        if (st[0] + st[1] > 4 && Vodja.igra.jeVeljavnaZgodovina()) razveljavi.setEnabled(true);
        else razveljavi.setEnabled(false);
        
        
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
                                           + imeNaPotezi.toString().substring(1) + " nima možnih potez. Še enkrat je na vrsti " + imeNasprotnik + "!");
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
                        status.setText("Potezo izbira " + naPotezi + " igralec."); //- " + imeNaPotezi + ".");
                    }
                    break;

                case ZMAGA_B:
                    status.setText("Igra je zaključena!");
                    JOptionPane.showMessageDialog(this, "Zmagal je ČRNI igralec z rezultatom: " + st[0] + " | " + st[1] + ".");
                    status.setText(Strings.START_STATUS);
                    break;

                case ZMAGA_W:
                    status.setText("Igra je zaključena!");
                    JOptionPane.showMessageDialog(this, "Zmagal je BELI igralec z rezultatom: " + st[1] + " | " + st[0] + ".");
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