# Othello
Projektna naloga pri predmetu Programiranje 2. V programskem jeziku Java implementiramo igro za dva igralca [Othello](https://www.worldothello.org/about/about-othello/othello-rules/official-rules/english).

## Setup
V trenutni verziji zaženemo datoteko `Othello.java`, da odpremo uporabniški vmesnik. V meniju `Nova igra` lahko izbiramo tip igralcev: človek proti človeku, človek proti računalniku ali računalnik proti računalniku.
Trenutno so implementirane tri različice računalnikove inteligence:
1. **neumen igralec**, ki izbira naključno potezo,
2. algoritem **minimax z alfa-beta rezi**, ki mu lahko določimo globino, ter
3. **Monte Carlo Tree Search** algoritem z nastavljivo časovno omejitvijo.

Inteligenco računalnika nastavimo v datoteki `Vodja.java` v spremenljivko `racunalnikovaInteligenca`.

## Blokirane pozicije
Blokirane pozicije so pozicije v katerih igralec na potezi nima možnih potez, njegov nasprotnik pa. V takih primerih je dvakrat zaporedoma na vrsti nasprotnik. 

V trenutni implementaciji se to preveri z metodo na objektu `igra`: `igra.stanje()`. Metoda se pokliče v razredu `Vodja.java` v funkciji `igramo()`. `igra.stanje()` preveri, če ima igralec na potezi kakšno možno potezo: v tem primeru vrne stanje igre `Stanje.V_TEKU`. V primeru da igralec na potezi nima možnih potez, spremeni polje `igra.naPotezi` v nasprotnika ter preveri, če ima ta kakšno možno potezo. Če jo ima, vrne `Stanje.V_TEKU`, če je nima, pa to pomeni, da je igra zaključena in ustrezno vrne `Stanje.ZMAGA_W`, `Stanje.ZMAGA_B` ali `Stanje.NEODLOCENO`.

Ob vsakem zagonu `igramo()` se na `igri` požene `.stanje()`. Glede na to se igra nadaljuje ali zaključi.

Po premisleku to res ni najbolj elegantna rešitev, metoda tudi ni najbolje poimenovana glede na vse funkcije, ki jih izvaja. v končnem projektu bomo to popravili.

## Status projekta
Uporabniški vmesnik in inteligenten igralec delujeta, a nista izpopoljena -- projekt še ni zaključen.

**Naslednji koraki:**
1. Izpopolniti uporabniški vmesnik:
   * Možnost, da človek igra proti različnim zahtevnostim računalnikove inteligence.
   * Izbira imena igralcev.
   * Poenotenje izpisov v statusni vrstici.
   * Izris možnih potez.
   * Prilagodljiva velikost okna.
2. Izpopolniti Monte Carlo Tree Search algoritem:
   * Naj se izogiba slabim potezam (ki nasprotniku dovolijo, da vzame kot).
   * Izpopolni časovno učinkovitost: da ne presega časovne omejitve brez `try` in `catch TimeoutException`. Za potrebe tekmovanja smo bili posebej pozorni, da metoda ne bi močno presegla časovne omejitve. V osnovi je dovolj štiri korake MCTS algoritma zapreti v preprosto zanko `while (System.currentTimeMillis() < end)` ter opustiti metodo `preveriCas()`.
   * Funkcija igra.stanje() je časovno naporna - se da to optimizirati?
3. Namigi za človeškega igralca: ponudimo mu najboljšo izmed možnih potez izbrano s poljubnim algoritmom.
4. Gumb Razveljavi potezo za človeškega igralca: Zapomni si prejšnje stanje na deski.
5. Morda: izbira večje ali manjše igralne deske.