import logika.Igra;
import logika.IgraOth;
import splosno.Poteza;

public class othello {

	public static void main(String[] args) {
		IgraOth test = new IgraOth();
		System.out.println(test.poteze());
		System.out.println(test.naPotezi());
		test.printIgra();
		Poteza prva = new Poteza(2, 4);
		test.odigraj(prva);
		test.printIgra();
		System.out.println(test.poteze());
		System.out.println(test.naPotezi());
		Poteza druga = new Poteza(4, 5);
		test.odigraj(druga);
		test.printIgra();
		System.out.println(test.stanje());
		System.out.println(test.poteze());
		Poteza tretja = new Poteza(5, 4);
		test.odigraj(tretja);
		test.printIgra();
		
	}

}
