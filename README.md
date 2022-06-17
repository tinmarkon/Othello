# Othello
Projektna naloga pri predmetu Programiranje 2. V programskem jeziku Java implementiramo logiko, inteligenco in uporabniški vmesnik za igro za dva igralca [Othello](https://www.worldothello.org/about/about-othello/othello-rules/official-rules/english).

Trenutno so implementirane tri različice računalnikove inteligence:
1. **neumen igralec** (`new NeumenIgralec()`), ki izbira naključno potezo,
2. algoritem **minimax z alfa-beta rezi** (`new AlphaBeta(globina)`), ki mu lahko določimo globino, ter
3. **Monte Carlo Tree Search** (`new MonteCarlo(MAX_CAS)`) algoritem z nastavljivo časovno omejitvijo.

### Setup
V trenutni verziji se metoda main skriva v datoteki `Othello.java`. Ta odpre uporabniški vmesnik.

### Uporabniški vmesnik
Na začetnem oknu lahko izberemo igro `EN IGRALEC`, ki odpre meni za izbiro barve igralca in izbiro težavnosti nasprotnika - računalnikove inteligence. Na voljo so: **Povprečen nasprotnik** (`AlphaBeta(globina: 3)`), **Pameten nasprotnik** (`AlphaBeta(globina: 7)`) in **Genialen nasprotnik** (`MonteCarlo(MAX_TIME : 3500 ms)`). S klikom na `ZAČNI IGRO` smo preusmerjeni na igralno polje. Igro igramo s klikom na ustrezno mesto na igralni plošči, kjer se izriše žeton naše barve. Možne poteze igralca na potezi so prikazane s svetlejšo barvo. 

Gumb `DVA IGRALCA` nas pošlje neposredno na igralno polje, kjer lahko dva človeška uporabnika igrata drug proti drugemu. Igro vedno začne črni.

V začetnem meniju s klikom na gumb `NAVODILA` odpremo zavihek z osnovnimi navodili za igro Othello ter povzetkom osnovnih funkcij uporabniškega vmesnika.

### Blokirane pozicije
Blokirane pozicije so pozicije v katerih igralec na potezi nima možnih potez, njegov nasprotnik pa. V takih primerih je dvakrat zaporedoma na vrsti nasprotnik. 

V prvi implementaciji inteligence se je to preverilo z metodo na objektu `igra`: `igra.stanje()`. `igra.stanje()` preveri, če ima igralec na potezi kakšno možno potezo: v tem primeru vrne stanje igre `Stanje.V_TEKU`. V primeru da igralec na potezi nima možnih potez, spremeni polje `igra.naPotezi` v nasprotnika ter preveri, če ima ta kakšno možno potezo. Če jo ima, vrne `Stanje.V_TEKU`, če je nima, pa to pomeni, da je igra zaključena in ustrezno vrne `Stanje.ZMAGA_W`, `Stanje.ZMAGA_B` ali `Stanje.NEODLOCENO`.

Ob vsakem zagonu `igramo()` v `Vodja.java` se na `igri` požene `.stanje()`. Glede na rezultat se igra nadaljuje ali zaključi.

To ni bila najboljša rešitev, saj funkcija kot je `igra.stanje()` ne bi smela posegati v igro - naj le vrača stanje. Zato smo uvedli novo stanje igre `Stanje.BLOKIRANO`. V primeru, da `igra.stanje()` vrne `Stanje.BLOKIRANO` se v `Vodja.java` pokliče metoda `igra.odigraj(new Poteza(-10, -10))`, kjer smo argument `Poteza(-10, -10)` definirali kot *dummy* potezo, ki le spremeni igralca na potezi v nasprotnika, stanje na deski pa ohrani.

### Status projekta
Uporabniški vmesnik in inteligenten igralec delujeta, a nista izpopolnjena - projekt še ni
zaključen.

**Naslednji koraki:**
2. Izpopolniti Monte Carlo Tree Search algoritem:
   * Naj se izogiba slabim potezam (ki nasprotniku dovolijo, da vzame kot).
3. Izpopolniti logiko igre:
   * Funkcija `igra.stanje()` je časovno naporna - se da to optimizirati?
4. Dopolniti README z osnovnimi opisi metod in funkcij ter viri in literaturo.