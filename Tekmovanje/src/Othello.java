import logika.Igra;
import splosno.Poteza;

import gui.GlavnoOkno;
import inteligenca.Vozlisce;
import vodja.Vodja;

public class Othello {

	public static void main(String[] args) {
		
		GlavnoOkno glavno_okno = new GlavnoOkno();
		glavno_okno.pack();
		glavno_okno.setVisible(true);
		Vodja.okno = glavno_okno; 
		/*
		
		Igra test = new Igra();
		System.out.println(test.poteze());
		System.out.println(test.naPotezi());
		Poteza prva = new Poteza(2, 4);
		test.odigraj(prva);
		
		//test.printIgra();
		
		//test.printIgra();
		
		
		Poteza druga = new Poteza(4, 5);
		test.odigraj(druga);
		test.printIgra();
		Poteza tretja = new Poteza(5, 4);
		test.odigraj(tretja);
		test.printIgra();
		test.odigraj(tretja);
		test.printIgra();
		test.odigraj(new Poteza(6, 5));
		test.printIgra();
		test.odigraj(new Poteza(6, 4));
		test.printIgra();
		test.odigraj(new Poteza(6, 3));
		test.printIgra();
		test.odigraj(new Poteza(7, 4));
		test.printIgra();
		
		
		Vozlisce vozlisce = new Vozlisce(test, null);
		vozlisce.gamePosition.printIgra();
		System.out.print(vozlisce.getOcena());
		System.out.print(vozlisce.getOtroci());
		
		Poteza druga = new Poteza(2, 3);
		
		vozlisce.getOtroci().get(druga).printVozlisce();
		*/
	}   

}
