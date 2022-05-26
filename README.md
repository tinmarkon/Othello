# Othello
Projektna naloga pri predmetu Programiranje 2. V programskem jeziku Java implementiramo igro za dva igralca [Othello](https://www.worldothello.org/about/about-othello/othello-rules/official-rules/english).

## Setup
V trenutni verziji zaženemo datoteko `Othello.java`, da odpremo uporabniški vmesnik. V meniju `Nova igra` lahko izbiramo tip igralcev: človek proti človeku, človek proti računalniku ali računalnik proti računalniku.
Trenutno so implementirane tri različice računalnikove inteligence:
0. neinteligenten igralec, ki izbira naključno potezo,
1. algoritem **minimax z alfa-beta rezi**, ki mu lahko nastavimo globino, ter
2. **Monte Carlo Tree Search** algoritem.
Inteligenco računalnika nastavimo v datoteki `Vodja.java` v spremenljivko `racunalnikovaInteligenca`.


## Status projekta
Uporabniški vmesnik in inteligenten igralec delujeta, a projekt še ni zaključen.
**Naslednji koraki:**
1. Izpopolniti uporabniški vmesnik:
   * Možnost, da človek igra proti različnim zahtevnostim računalnikove inteligence.
   * Izbira imena igralcev.
   * Poenotenje izpisov v statusni vrstici.
   * Izris možnih potez.
2. Izpopolniti Monte Carlo Tree Search algoritem, da se izogiba slabim potezam (ki nasprotniku dovolijo, da vzame kot).
3. Namigi za človeškega igralca: ponudimo mu najboljšo izmed možnih potez izbrano s poljubnim algoritmom.
