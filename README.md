# Othello
Projektna naloga pri predmetu Programiranje 2. V programskem jeziku Java implementiramo igro za dva igralca [Othello](https://www.worldothello.org/about/about-othello/othello-rules/official-rules/english).

## Setup
V trenutni verziji zaženemo datoteko `Othello.java`, da odpremo uporabniški vmesnik. V meniju `Nova igra` lahko izbiramo tip igralcev: človek proti človeku, človek proti računalniku ali računalnik proti računalniku.
Trenutno so implementirane tri različice računalnikove inteligence:
1. **neumen igralec**, ki izbira naključno potezo,
2. algoritem **minimax z alfa-beta rezi**, ki mu lahko določimo globino, ter
3. **Monte Carlo Tree Search** algoritem z nastavljivo časovno omejitvijo.

Inteligenco računalnika nastavimo v datoteki `Vodja.java` v spremenljivko `racunalnikovaInteligenca`.


## Status projekta
Uporabniški vmesnik in inteligenten igralec delujeta, a projekt še ni zaključen.
**Naslednji koraki:**
1. Izpopolniti uporabniški vmesnik:
   * Možnost, da človek igra proti različnim zahtevnostim računalnikove inteligence.
   * Izbira imena igralcev.
   * Poenotenje izpisov v statusni vrstici.
   * Izris možnih potez.
2. Izpopolniti Monte Carlo Tree Search algoritem:
   * Naj se izogiba slabim potezam (ki nasprotniku dovolijo, da vzame kot).
   * Izpopolni časovno učinkovitost: da ne presega časovne omejitve brez `try` in `catch TimeoutException`.
   * Funkcija igra.stanje() je časovno naporna - se da to optimizirati?
3. Namigi za človeškega igralca: ponudimo mu najboljšo izmed možnih potez izbrano s poljubnim algoritmom.
