# Othello
Projektna naloga pri predmetu Programiranje 2. V programskem jeziku Java implementiramo logiko, inteligenco in uporabniški vmesnik za igro za dva igralca [Othello](https://www.worldothello.org/about/about-othello/othello-rules/official-rules/english).

Trenutno so implementirane tri različice računalnikove inteligence:
1. **neumen igralec** (`new NeumenIgralec()`), ki izbira naključno potezo,
2. algoritem **minimax z alfa-beta rezi** (`new AlphaBeta(globina)`), ki mu lahko določimo globino, ter
3. **Monte Carlo Tree Search** (`new MonteCarlo()`) algoritem z nastavljivo časovno omejitvijo.

## Setup
V trenutni verziji zaženemo datoteko `Tekmovanje/Othello.java`, da odpremo uporabniški vmesnik. V orodni vrstici na vrhu strani izberemo
tip črnega in belega igralca. Na voljo so: `Človek`, `Povprečen nasprotnik` (`AlphaBeta(globina: 3)`), `Pameten nasprotnik` (`AlphaBeta(globina: 7)`) in `Genialen nasprotnik` (`MonteCarlo()`, `MAX_TIME = 3,5 s`).

Klik na gumb `Začni igro` zažene novo igro, igramo s klikom na ustrezno polje na igralni plošči.

## Blokirane pozicije
Blokirane pozicije so pozicije v katerih igralec na potezi nima možnih potez, njegov nasprotnik pa. V takih primerih je dvakrat zaporedoma na vrsti nasprotnik. 

V trenutni implementaciji se to preveri z metodo na objektu `igra`: `igra.stanje()`. Metoda se pokliče v razredu `Vodja.java` v funkciji `igramo()`. `igra.stanje()` preveri, če ima igralec na potezi kakšno možno potezo: v tem primeru vrne stanje igre `Stanje.V_TEKU`. V primeru da igralec na potezi nima možnih potez, spremeni polje `igra.naPotezi` v nasprotnika ter preveri, če ima ta kakšno možno potezo. Če jo ima, vrne `Stanje.V_TEKU`, če je nima, pa to pomeni, da je igra zaključena in ustrezno vrne `Stanje.ZMAGA_W`, `Stanje.ZMAGA_B` ali `Stanje.NEODLOCENO`.

Ob vsakem zagonu `igramo()` se na `igri` požene `.stanje()`. Glede na rezultat se igra nadaljuje ali zaključi.

Po premisleku to res ni najbolj elegantna rešitev, metoda tudi ni najbolje poimenovana glede na vse funkcije, ki jih izvaja. v končnem projektu bomo to popravili.

## Status projekta
Uporabniški vmesnik in inteligenten igralec delujeta, a nista izpopolnjena - projekt še ni zaključen.

**Naslednji koraki:**
1. Izpopolniti uporabniški vmesnik:
   * Možnost, da človek igra proti različnim zahtevnostim računalnikove inteligence. X
   * Poenotenje izpisov v statusni vrstici. X
     * TODO: Opozori človeka, da izbrana poteza ni veljavna!
   * Izris možnih potez. X
   * Namigi za človeškega igralca: ponudimo mu najboljšo izmed možnih potez izbrano s poljubnim algoritmom. X 
   * TODO: Gumb `Razveljavi potezo` za človeškega igralca: Zapomni si prejšnje stanje na deski. 
   * TODO: Polepšanje gumbov, orodnih vrstic, menijev itd ... morda uporabiti kak lep LookAndFeel?
2. Izpopolniti Monte Carlo Tree Search algoritem:
   * Naj se izogiba slabim potezam (ki nasprotniku dovolijo, da vzame kot).
   * Izpopolni časovno učinkovitost: da ne presega časovne omejitve brez `try` in `catch TimeoutException`. Za potrebe tekmovanja smo bili posebej pozorni, da metoda ne bi močno presegla časovne omejitve. V osnovi je dovolj štiri korake MCTS algoritma zapreti v preprosto zanko `while (System.currentTimeMillis() < end)` ter opustiti metodo `preveriCas()`. X
3. Izpopolniti logiko igre:
   * Funkcija `igra.stanje()` je časovno naporna - se da to optimizirati?
   * TODO: Kako se soočamo z BLOKIRANIMI POZICIJAMI?
4. Dopolniti README z osnovnimi opisi metod in funkcij ter viri in literaturo.