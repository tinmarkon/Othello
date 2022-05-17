package inteligenca;

import logika.Igra;

public class TreeEntry {
	private final Igra gamePosition;
	private int obiski;
	// Število zmag če iz tega nodea igramo random poteze
	private double ocena;
	private TreeEntry stars;
	private Map<Poteza, TreeEntry>
	
	public TreeEntry(Igra gamePosition, ) {
		this.gamePosition = gamePosition;
		
	}

}
