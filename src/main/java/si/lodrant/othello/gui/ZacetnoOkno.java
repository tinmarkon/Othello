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
import si.lodrant.othello.splosno.KdoIgra;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import javax.swing.JPanel;


public class ZacetnoOkno extends JFrame implements ActionListener {

    private JPanel p0, p1, p2, p20, p21, p31, p3, p4, p5;
    private JButton zacniIgro;
    private JTextField imeCrnega_izbira, imeBelega_izbira;
    private JComboBox igralecBeli, igralecCrni;
    private String imeCrnega, imeBelega;

    // Tekst na gumbih.
    protected final static String START_GUMB = "Začni igro";

    protected final static String IGRALEC_C = "Človek";
    protected final static String IGRALEC_R1 = "Povprečen nasprotnik";
    protected final static String IGRALEC_R2 = "Pameten nasprotnik";
    protected final static String IGRALEC_R3 = "Genialen nasprotnik";

    // Izbrana vrsta črnega in belega igralca. Privzeto igra človek proti človeku.
    protected static VrstaIgralca izbira_igralecCrni = VrstaIgralca.C;
    protected static VrstaIgralca izbira_igralecBeli = VrstaIgralca.C;

    public ZacetnoOkno() {
        this.setTitle("Othello");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        Container pane = this.getContentPane();

        // ------------------ naslovna slika ----------------------

        p0 = new JPanel();

        GridBagConstraints panel0_layout = new GridBagConstraints();
        panel0_layout.gridx = 0;
        panel0_layout.gridy = 0;
        panel0_layout.anchor = GridBagConstraints.NORTH;
        panel0_layout.fill = GridBagConstraints.CENTER;
        panel0_layout.gridwidth = 2;

        pane.add(p0, panel0_layout);

        ImageIcon naslov = SwingUtils.createImageIcon("images/naslov.png", "naslov");
        JLabel lab0 = new JLabel(naslov, JLabel.CENTER);

        p0.add(lab0);

        // ------------------ Izberi igralce: ----------------------
        p1 = new JPanel();

        GridBagConstraints panel1_layout = new GridBagConstraints();
        panel1_layout.gridx = 0;
        panel1_layout.gridy = 1;
        panel1_layout.gridwidth = 2;
        //panel1_layout.anchor = GridBagConstraints.NORTH;
        //panel1_layout.fill = GridBagConstraints.NORTH;
        pane.add(p1, panel1_layout);

        JLabel lab1 = new JLabel("Izberite igralca:", JLabel.CENTER);
        Font mojFont = new Font("Dialog", Font.PLAIN, 20);
        lab1.setFont(mojFont);
        p1.add(lab1);

        // ----------------------- Igralca ---------------------

        p2 = new JPanel(); // [ikona+label] [JCombobox] [label + JTextbox]
        p21 = new JPanel(); // label + JTextbox

        p3 = new JPanel(); // [ikona+label] [JCombobox] [label + JTextbox]
        p31 = new JPanel(); // label + JTextbox

        GridBagConstraints panel2_layout = new GridBagConstraints();
        GridBagConstraints panel3_layout = new GridBagConstraints();
        GridBagConstraints panel21_layout = new GridBagConstraints();
        GridBagConstraints panel31_layout = new GridBagConstraints();

        // labels
        /** Returns an ImageIcon, or null if the path was invalid. */
        ImageIcon beli_icon = SwingUtils.createImageIcon("images/beli_small.png", "beli zeton");
        ImageIcon crni_icon = SwingUtils.createImageIcon("images/crni_small.png", "beli zeton");
        ;

        JLabel crni_label = new JLabel("Črni igralec:", crni_icon, JLabel.LEFT);
        JLabel beli_label = new JLabel("Beli igralec: ", beli_icon, JLabel.LEFT);

        JLabel imeCrni_label = new JLabel("Ime igralca:", JLabel.LEFT);
        JLabel imeBeli_label = new JLabel("Ime igralca:", JLabel.LEFT);

        // create a combobox
        igralecCrni = new JComboBox<>(new String[]{IGRALEC_C, IGRALEC_R1, IGRALEC_R2, IGRALEC_R3});
        igralecBeli = new JComboBox<>(new String[]{IGRALEC_C, IGRALEC_R1, IGRALEC_R2, IGRALEC_R3});

        imeCrnega_izbira = new JTextField("Katka");
        imeBelega_izbira = new JTextField("Tin");

        igralecCrni.addActionListener(igralecCrniAL);
        igralecBeli.addActionListener(igralecBeliAL);

        p2.add(crni_label);
        p2.add(igralecCrni);

        p3.add(beli_label);
        p3.add(igralecBeli);

        panel21_layout.gridx = 1;
        panel21_layout.gridy = 2;
        panel21_layout.weightx = 0.5;
        panel21_layout.anchor = GridBagConstraints.LINE_END;

        p21.add(imeCrni_label);
        p21.add(imeCrnega_izbira);
        p2.add(p21, panel21_layout);
        p21.setVisible(true);

        panel2_layout.gridx = 0;
        panel2_layout.gridy = 2;
        panel2_layout.gridwidth = 2;
        panel2_layout.anchor = GridBagConstraints.LINE_START;
        panel2_layout.anchor = GridBagConstraints.LINE_START;

        panel31_layout.gridx = 1;
        panel31_layout.gridy = 3;
        panel31_layout.weightx = 0.5;
        panel31_layout.anchor = GridBagConstraints.LINE_END;

        p31.add(imeBeli_label);
        p31.add(imeBelega_izbira);
        p3.add(p31, panel31_layout);
        p31.setVisible(true);

        panel3_layout.gridx = 0;
        panel3_layout.gridy = 3;
        panel3_layout.gridwidth = 2;
        panel3_layout.anchor = GridBagConstraints.LINE_START;
        panel3_layout.anchor = GridBagConstraints.LINE_START;

        pane.add(p2, panel2_layout);
        pane.add(p3, panel3_layout);

        // ----------------- Start gumb ---------------------------
        p4 = new JPanel();

        GridBagConstraints panel4_layout = new GridBagConstraints();
        panel4_layout.gridx = 0;
        panel4_layout.gridy = 4;
        panel4_layout.anchor = GridBagConstraints.CENTER;
        panel4_layout.fill = GridBagConstraints.HORIZONTAL;
        panel4_layout.gridwidth = 2;
        panel4_layout.gridheight = 2;

        zacniIgro = new JButton(START_GUMB);
        zacniIgro.addActionListener(startGumbAL);

        //p4.add(zacniIgro);
        pane.add(zacniIgro, panel4_layout);
    }


    ActionListener igralecCrniAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch ((String) igralecCrni.getSelectedItem()) {//check for a match
                case IGRALEC_C:
                    p21.setVisible(true);
                    izbira_igralecCrni = VrstaIgralca.C;
                    break;
                case IGRALEC_R1:
                    p21.setVisible(false);
                    izbira_igralecCrni = VrstaIgralca.R1;
                    break;
                case IGRALEC_R2:
                    p21.setVisible(false);
                    izbira_igralecCrni = VrstaIgralca.R2;
                    break;
                case IGRALEC_R3:
                    p21.setVisible(false);
                    izbira_igralecCrni = VrstaIgralca.R3;
                    break;
            }
        }
    };

    ActionListener igralecBeliAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch ((String) igralecBeli.getSelectedItem()) {
                case IGRALEC_C:
                    p31.setVisible(true);
                    izbira_igralecBeli = VrstaIgralca.C;
                    break;
                case IGRALEC_R1:
                    p31.setVisible(false);
                    izbira_igralecBeli = VrstaIgralca.R1;
                    break;
                case IGRALEC_R2:
                    p31.setVisible(false);
                    izbira_igralecBeli = VrstaIgralca.R2;
                    break;
                case IGRALEC_R3:
                    p31.setVisible(false);
                    izbira_igralecBeli = VrstaIgralca.R3;
                    break;
            }
        }
    };

    ActionListener startGumbAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Vodja.nastaviIgralce();

            Vodja.kdoIgra = new EnumMap<Igralec, KdoIgra>(Igralec.class);
            Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);

            if (izbira_igralecCrni != VrstaIgralca.C) {
                imeCrnega = igralecCrni.getSelectedItem().toString();
            } else {
                imeCrnega = imeCrnega_izbira.getText();
            }
            System.out.println("Crni igralec:" + imeCrnega);

            if (izbira_igralecBeli != VrstaIgralca.C) {
                imeBelega = igralecBeli.getSelectedItem().toString();
            } else {
                imeBelega = imeBelega_izbira.getText();
            }
            System.out.println("Beli igralec:" + imeBelega);

            Vodja.kdoIgra.put(Igralec.BLACK, new KdoIgra(imeCrnega));
            Vodja.kdoIgra.put(Igralec.WHITE, new KdoIgra(imeBelega));

            Vodja.vrstaIgralca.put(Igralec.BLACK, izbira_igralecCrni);
            Vodja.vrstaIgralca.put(Igralec.WHITE, izbira_igralecBeli);

            Vodja.vsiCrniIgralci.put(izbira_igralecCrni, new KdoIgra(imeCrnega));
            Vodja.vsiBeliIgralci.put(izbira_igralecBeli, new KdoIgra(imeBelega));

        }
    };

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }


}
