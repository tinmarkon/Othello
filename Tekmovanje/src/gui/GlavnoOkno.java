package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import vodja.Vodja;
import vodja.VrstaIgralca;
import logika.Igralec;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Container;  
import javax.swing.JButton;  
import javax.swing.JComboBox;  
import javax.swing.JToolBar;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/*
 * Glavno okno aplikacije hrani trenutno stanje igre in nadzoruje potek
 * igre. 
 * Nastavimo zgornji ToolBar (izbira igralcev in gumb za začetek igre), 
 * ustvarimo novo IgralnoPolje,
 * nastavimo spodnji ToolBar (gumbi za prikaži namig, prikaži možne poteze, razveljavi zadnjo potezo) in
 * nastavimo vrstico za statusna sporočila na dnu okna.
 */

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {

	// Polje na katerem igramo igro.
	private IgralnoPolje polje;

	// Orodna vrstica
	private JToolBar tb; // na vrhu okna
	private JToolBar tb2; // na dnu okna
 
    // Gumbi.
    private JButton zacniIgro, namig, razveljavi;
	private JToggleButton poteze;

	// Tekst na gumbih.
	private final static String START_GUMB = "Začni igro";
	private final static String NAMIG_GUMB = "Namig";
	private final static String POTEZE_GUMB = "Možne poteze";
	private final static String RAZVELJAVI_GUMB = "Razveljavi potezo!";

 
    // Spremenljivki za JComboBox za izbiro igralca.
	private JComboBox<String> igralecCrni, igralecBeli;
	// Možnosti v meniju za izbiro vrste igralca.
	private final static String IGRALEC_C = "Človek";
	private final static String IGRALEC_R1 = "Povprečen nasprotnik";
	private final static String IGRALEC_R2 = "Pameten nasprotnik";
	private final static String IGRALEC_R3 = "Genialen nasprotnik";

	// Statusna vrstica v spodnjem delu okna.
	private JLabel status;
	// Izpisi v statusni vrstici.
	private final static String START_STATUS = "Izberite igralca in začnite igro!";

	// Izbrana vrsta črnega in belega igralca. Privzeto igra človek proti človeku.
	private VrstaIgralca izbira_igralecCrni = VrstaIgralca.C;
	private VrstaIgralca izbira_igralecBeli = VrstaIgralca.C;

	final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;


	/**
	 * Ustvari novo glavno okno in prični igrati igro.
	 */
	public GlavnoOkno() {
		
		this.setTitle("Othello");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		Container pane = this.getContentPane();
		Font mojFont = new Font("Dialog", Font.PLAIN, 15);

		// ------------- igralno polje ----------------------
		polje = new IgralnoPolje(); //kako ga prisilim, da bi pri resizanju ostal na sredini okna?!?
		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 1;
		polje_layout.fill = GridBagConstraints.BOTH;
		polje_layout.anchor = GridBagConstraints.CENTER;
		polje_layout.weightx = 2.0;
		polje_layout.weighty = 2.0;
		pane.add(polje, polje_layout);
		
		// ------- statusna vrstica za sporočila ------------
		status = new JLabel();
		
		status.setFont(mojFont);
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 3;
		status_layout.anchor = GridBagConstraints.CENTER;
		pane.add(status, status_layout);
		
		status.setText(START_STATUS);

		// ---------- Zgornja orodna vrstica: [crni igralec], [beli igralec], [zacni igro] ----------------
		tb = new JToolBar();
		tb.setFont(mojFont);

		GridBagConstraints panel_layout = new GridBagConstraints();
		panel_layout.gridx = 0;
		panel_layout.gridy = 0;
		panel_layout.anchor = GridBagConstraints.NORTH;
		panel_layout.fill = GridBagConstraints.NORTH;

		pane.add(tb, panel_layout);

		// create a panel
		JPanel p = new JPanel();

		JLabel lab1 = new JLabel("Črni igralec:", JLabel.LEFT);
		JLabel lab2 = new JLabel("Beli igralec:", JLabel.LEFT);

		// create a combobox
		igralecCrni = new JComboBox<>(new String[] {IGRALEC_C, IGRALEC_R1, IGRALEC_R2, IGRALEC_R3});
		igralecBeli = new JComboBox<>(new String[] {IGRALEC_C, IGRALEC_R1, IGRALEC_R2, IGRALEC_R3});

		// create new buttons
		zacniIgro = new JButton(START_GUMB);
		zacniIgro.addActionListener(startGumbAL);
		
		// add buttons and labels to panel
		p.add(lab1);
		p.add(igralecCrni);
		p.add(lab2);
		p.add(igralecBeli);
		p.add(zacniIgro);
		tb.add(p);

		igralecCrni.addActionListener(igralecCrniAL);

		igralecBeli.addActionListener(igralecBeliAL);

		// ---------- Spodnja orodna vrstica: [namig], [mozne poteze], [razveljavi potezo] ----------------
		tb2 = new JToolBar();

		GridBagConstraints panel2_layout = new GridBagConstraints();
		panel2_layout.gridx = 0;
		panel2_layout.gridy = 2;
		panel2_layout.weightx = 0.5;
		panel2_layout.fill = GridBagConstraints.HORIZONTAL;

		pane.add(tb2, panel2_layout);

		JPanel p2 = new JPanel();

		namig = new JButton(NAMIG_GUMB);
		namig.addActionListener(namigGumbAL);
		// namig.setVisible(false); // radi bi, da se gumba namig in moznePoteze vidita le, kadar je na potezi človek

		poteze = new JToggleButton(POTEZE_GUMB);
		poteze.addActionListener(potezeGumbAL);
		// poteze.setVisible(false);

		razveljavi = new JButton(RAZVELJAVI_GUMB);
		razveljavi.addActionListener(razveljaviGumbAL);
		
		p2.add(namig);
		p2.add(poteze);
		p2.add(razveljavi);
		tb2.add(p2);
	}

	// spodaj so definirani ActionListener-ji za posamezne gumbe
	ActionListener igralecCrniAL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch ((String) igralecCrni.getSelectedItem()) {//check for a match
				case IGRALEC_C:
					izbira_igralecCrni = VrstaIgralca.C;
					break;
				case IGRALEC_R1:
					izbira_igralecCrni = VrstaIgralca.R1;
					break;
				case IGRALEC_R2:
					izbira_igralecCrni = VrstaIgralca.R2;
					break;
				case IGRALEC_R3:
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
					izbira_igralecBeli = VrstaIgralca.C;
					break;
				case IGRALEC_R1:
					izbira_igralecBeli = VrstaIgralca.R1;
					break;
				case IGRALEC_R2:
					izbira_igralecBeli = VrstaIgralca.R2;
					break;
				case IGRALEC_R3:
					izbira_igralecBeli = VrstaIgralca.R3;
					break;
		}
	}
	};

	ActionListener startGumbAL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.BLACK, izbira_igralecCrni); 
			Vodja.vrstaIgralca.put(Igralec.WHITE, izbira_igralecBeli); 
			Vodja.igramoNovoIgro();
		}
	};

	ActionListener namigGumbAL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
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
		}
		else {
			// namig.setVisible(false);
			// poteze.setVisible(false);
			switch(Vodja.igra.stanje()) {
			case NEODLOCENO:
				status.setText("Igra je zaključena!");
				JOptionPane.showMessageDialog(this, "Rezultat je neodločen.");
				status.setText(START_STATUS);
				break;

			case V_TEKU: 
				Igralec naPotezi = Vodja.igra.naPotezi();
				if (Vodja.vrstaIgralca.get(naPotezi) == VrstaIgralca.C) {
					status.setText("Na potezi je " + naPotezi + "."); 
					// namig.setVisible(true);
					// poteze.setVisible(true);
					}
				else status.setText("Potezo izbira " + naPotezi + " igralec."); 
				break;

			case ZMAGA_B: 
				int[] st = Vodja.igra.prestejZetone();
				status.setText("Igra je zaključena!");
				JOptionPane.showMessageDialog(this, "Zmagal je črni - " + Vodja.vrstaIgralca.get(Igralec.BLACK) + 
				" - z rezultatom: " + st[0] + "/" + st[1] + ".");
				status.setText(START_STATUS);
				break;

			case ZMAGA_W: 
				int[] st1 = Vodja.igra.prestejZetone();
				status.setText("Igra je zaključena!");
				JOptionPane.showMessageDialog(this, "Zmagal je beli - " + Vodja.vrstaIgralca.get(Igralec.WHITE) + 
				" - z rezultatom: " + st1[1] + "/" + st1[0] + ".");
				status.setText(START_STATUS);
				break;
			}
		}
		polje.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}