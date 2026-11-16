# ESTUDI - ACTIVITAT1: MULTI-SNAKE G_A34

## Entorn d'execució i configuració de proves

* **CPU:** *13th Gen Intel(R) Core(TM) i7-13620H*
* **Memòria RAM total:** *15,7*
* **Sistema Operatiu:** Windows (64 bits)
* **Entorn Java:** Java 17 amb Maven, memòria màxima `-Xmx2g` per prova i temps límit de `120 s`.
* **Límit de profunditat de DFS:** Fixat a `50` per evitar el pou de perdició.

---

## Primera Part: Resultats en PDDL (`Mapa A`)

Després d'executar el planificador **LAMA-first** a *editor.planning.domains* amb `domain.pddl` i `problem.pddl`, hem obtingut un pla òptim de **longitud 12 accions**:

```lisp
(moure b c3x1 c3x2)
(moure b c3x2 c3x3)
(moure a c1x6 c1x5)
(moure a c1x5 c1x4)
(moure a c1x4 c1x3)
(moure a c1x3 c1x2)
(moure a c1x2 c1x1)
(moure a c1x1 c1x0)
(moure b c3x3 c3x4)
(moure b c3x4 c3x5)
(moure b c3x5 c3x6)
(moure b c3x6 c3x7)
; cost = 12 (unit cost)

```
---

## Tercera Part: Estudi
### Pregunta `[1]`: Quadre resum dels resultats obtinguts

Aquesta taula unifica els resultats de totes les execucions (`results.csv` i `results_pendents.csv`) sobre els 7 mapes de prova: 

| Mapa | Algoritme | usaLNT | Trobat | Vàlida | Longitud | Nodes Explorats | Nodes Tallats | Mem. Pic Oberts | Mem. Pic Tancats | Mem. Pic Total | Temps (ms) |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **mapA** | BFS | NO | YES | YES | 12 | 539.471 | 0 | 452.539 | 0 | 452.539 | 627 |
| **mapA** | BFS | YES | YES | YES | 12 | 5.052 | 5.514 | 1.235 | 6.003 | 6.955 | 49 |
| **mapA** | DFS | NO | YES | YES | 16 | 191.896 | 0 | 34 | 0 | 34 | 164 |
| **mapA** | DFS | YES | YES | YES | 16 | 2.170 | 1.799 | 34 | 2.192 | 2.215 | 35 |
| **mapA** | IDS | NO | YES | YES | 12 | 1.348.840 | 697.814 | 31 | 0 | 31 | 651 |
| **mapA** | IDS | YES | YES | YES | 12 | 20.968 | 24.067 | 31 | 5.223 | 5.237 | 100 |
| **mapA** | A*(H.bas) | NO | YES | YES | 12 | 13 | 0 | 23 | 0 | 23 | 17 |
| **mapA** | A*(H.bas) | YES | YES | YES | 12 | 13 | 0 | 23 | 35 | 58 | 19 |
| **mapA** | A*(H.av) | NO | YES | YES | 12 | 13 | 0 | 23 | 0 | 23 | 15 |
| **mapA** | A*(H.av) | YES | YES | YES | 12 | 13 | 0 | 23 | 35 | 58 | 19 |
| **mapB1** | BFS | YES | NO (OOM) | - | - | 1.706.372 | 3.082.624 | 1.439.356 | 3.145.727 | 4.585.083 | 17.053 |
| **mapB1** | DFS | YES | YES | YES | 35 | 1.459.356 | 1.597.285 | 112 | 1.459.440 | 1.459.526 | 4.196 |
| **mapB1** | IDS | YES | NO (OOM) | - | - | 8.743.961 | 12.558.771 | 81 | 3.511.901 | 3.511.939 | 85.190 |
| **mapB1** | A*(H.bas) | NO | YES | YES | 17 | 28 | 0 | 66 | 0 | 66 | 8 |
| **mapB1** | A*(H.bas) | YES | YES | YES | 17 | 28 | 0 | 66 | 93 | 159 | 2 |
| **mapB1** | A*(H.av) | NO | YES | YES | 17 | 18 | 0 | 60 | 0 | 60 | 12 |
| **mapB1** | A*(H.av) | YES | YES | YES | 17 | 18 | 0 | 60 | 77 | 137 | 6 |
| **mapB2** | BFS | YES | NO (OOM) | - | - | 1.632.748 | 3.009.528 | 1.512.981 | 3.145.727 | 4.658.708 | 21.184 |
| **mapB2** | DFS | YES | NO (OOM) | - | - | 3.501.832 | 3.732.315 | 121 | 3.501.896 | 3.501.961 | 69.130 |
| **mapB2** | IDS | YES | NO (OOM) | - | - | 8.439.302 | 12.118.900 | 80 | 3.501.770 | 3.501.834 | 106.935 |
| **mapB2** | A*(H.bas) | NO | YES | YES | 19 | 648.328 | 0 | 1.061.058 | 0 | 1.061.058 | 3.803 |
| **mapB2** | A*(H.bas) | YES | YES | YES | 19 | 4.745 | 3.920 | 9.024 | 13.768 | 22.792 | 85 |
| **mapB2** | A*(H.av) | NO | YES | YES | 19 | 1.268 | 0 | 5.526 | 0 | 5.526 | 138 |
| **mapB2** | A*(H.av) | YES | YES | YES | 19 | 230 | 187 | 850 | 1.079 | 1.929 | 19 |
| **mapC** | BFS | YES | NO (OOM) | - | - | 67.793 | 359.249 | 183.187 | 250.979 | 434.166 | 6.364 |
| **mapC** | DFS | YES | NO (OOM) | - | - | 252.272 | 531.913 | 479 | 252.692 | 253.116 | 6.616 |
| **mapC** | IDS | YES | NO (OOM) | - | - | 486.228 | 1.040.874 | 82 | 252.792 | 252.829 | 10.899 |
| **mapC** | A*(H.bas) | YES | YES | YES | 112 | 113 | 0 | 547 | 659 | 1.206 | 11 |
| **mapC** | A*(H.av) | YES | YES | YES | 112 | 113 | 0 | 547 | 659 | 1.206 | 453 |
| **mapD** | A*(H.bas) | YES | NO (OOM) | - | - | 705.981 | 641.353 | 1.246.863 | 1.952.843 | 3.199.706 | 29.978 |
| **mapD** | A*(H.av) | YES | YES | YES | 42 | 50.011 | 51.774 | 120.684 | 170.694 | 291.378 | 2.812 |
| **mapE** | A*(H.av) | YES | NO (OOM) | - | - | 87.895 | 76.148 | 370.998 | 458.892 | 829.890 | 112.249 |
| **mapF** | A*(H.av) | YES | NO (TIMEOUT) | - | - | - | - | - | - | - | 120.000 |

---


### Pregunta `[2]`: Té sentit buscar cicles en l'arbre de cerca pujant per la branca actual (`usarLNT=false`)?


**No té cap sentit en el nostre problema Multi-Snake**, ja que cada cop que un agent es mou, la casella que abandona es converteix en una paret (`#`). Cada pas cap a baix en l'arbre de cerca té més parets que tots els anteriors, per tant, mai podrem tornar a ell seguint aquesta mateixa branca. El que vol dir que l'espai d'estats és **acíclic**. A més, recórrer els nodes pare cap amunt afegeix un cost inútil de $O(d)$ a cada generació de node.


Si observem la columna `Nodes Tallats` de la taula anterior en les execucions amb `usarLNT = NO` de **BFS** , **DFS** i **A\*** dels mapes A, B1 i B2, el valor és sempre **`0`**
*En el cas IDS amb usarLNT = NO, els `697.814` nodes tallats corresponenen a la poda pel límit de profunditat de cada iteració*


### Pregunta `[3]`: Calculeu el factor d’embrancament mitjà (real) del mapa A per BFS usant LNT i sense fer-la servir. Justifiqueu els avantatges d’usar la LNT respecte a no fer-la servir.

Al `mapA`, la solució òptima la trobem a profunditat $d = 12$. El factor d'embrancament efectiu $b^*$ es calcula resolent l'equació que relaciona el nombre total de nodes explorats $N$ amb la profunditat $d$:

$$N = \sum_{i=0}^{d} (b^*)^i = \frac{(b^*)^{d+1} - 1}{b^* - 1}$$

* **BFS sense LNT (`usaLNT = NO`):** $N = 539.471$ nodes explorats a profunditat $d = 12$.
  * Resolent el polinomi: **$b^* = 2,90$** (aproximació $539.471^{1/12} = 3,00$).
* **BFS amb LNT (`usaLNT = YES`):** $N = 5.052$ nodes explorats a profunditat $d = 12$.
  * Resolent el polinomi: **$b^* = 1,91$** (aproximació $5.052^{1/12} = 2,04$).

**Avantatges de fer servir la LNT respecte a no fer-la servir:** Malgrat que dins d'una mateixa branca no hi ha cicles, sí que existeixen branques diferents que arriben al mateix estat (per exemple, intercalar els moviments de l'agent `A` i de l'agent `B` en ordres diferents dona exactament les mateixes posicions i el mateix rastre).
Sense LNT, BFS torna a expandir el mateix estat moltes vegades; usar la LNT talla aquestes repeticions (`5.514` nodes tallats) i ens dona tres avantatges:
1. **Reducció molts clara dels nodes explorats:** passem de `539.471` a `5.052`.
2. **Menys temps d'execució:** passem de `627 ms` a `49 ms`.
3. **Estalvi global de memòria en graelles obertes:** encara que la LNT ocupa espai, evita l'explosió de la cua d'oberts reduint el pic total de memòria de `452.539` a `6.955` estats.

---

### Pregunta `[4]`: Expliqueu les diferències de resultats entre el mapa B1 i B2.

Els dos mapes tenen exactament les mateixes dimensions ($7 \times 10$, interior $5 \times 8$), cap paret interior, les mateixes sortides (`a` a `(4,0)` i `b` a `(5,9)`) i la mateixa posició inicial per a l'agent `B` (`(2,2)`). L'única diferència és la posició inicial de l'agent `A`:
* A **`mapB1`**, `A` comença a `(3,6)` (per sota de `B`).
* A **`mapB2`**, `A` comença dues files més amunt, a `(1,6)` (per sobre de `B`).

Aquest canvi que sembla petit altera completament la interacció entre les trajectòries dels dos agents:

1. **La longitud de la solució òptima canvia de `17` a `19`:** A `mapB1`, `A` és a distància Manhattan 7 de `a` i `B` a distància 10 de `b`, amb trajectòries separades que no interfereixen ($7 + 10 = \mathbf{17}$ moviments). En canvi, a `mapB2`, en començar dues files més amunt, la distància mínima de `A` puja a 9 ($9 + 10 = \mathbf{19}$ moviments) i, a més, el seu camí d'a dalt a la dreta cap a baix a l'esquerra **es creua al centre del tauler** amb el camí de `B`.
2. **DFS amb LNT (`YES` vs `OUT OF MEMORY ERROR`):** Al `mapB1`, com que els agents no es bloquegen, `DFS` acaba trobant una solució subòptima de longitud `35` després d'explorar `1.459.356` nodes (`1.597.285` tallats, pic de memòria total de `1.459.526` estats i temps de `4.196 ms`). Al `mapB2`, en canvi, el rastre del primer agent que creua el centre talla el pas a l'altre, provocant *backtracking* massiu dins el límit de profunditat 50 fins a esgotar els 2 GB de RAM (`3.501.832` nodes explorats, pic de `3.501.961` estats i `69.130 ms` sense trobar solució).
3. **Impacte en A\* i diferència entre heurístiques:** Al `mapB1` gairebé no hi ha interferències i totes dues heurístiques resolen a l'instant (`28` nodes en `2 ms` amb `A*(H.bas)` i `18` nodes en `6 ms` amb `A*(H.av)` usant LNT). Al `mapB2`, com que `H.bas` ignora els rastres, dispara l'exploració (`648.328` nodes i `3.803 ms` sense LNT; `4.745` nodes, `3.920` tallats i `85 ms` amb LNT), mentre que `H.av` detecta els bloquejos mitjançant BFS (`INF`) i ho resol en només **`230` nodes** (`187` tallats, pic de memòria de `1.929` estats i `19 ms`) amb LNT.

---

### Pregunta `[5]`: Expliqueu de forma justificada si les heurístiques proposades són admissibles. Són també consistents? Quina conseqüència té la consistència per a A* amb LNT?

* **Admissibilitat (Sí, totes dues):** Cap de les dues sobreestima mai el cost real mínim ($h(n) \le h^*(n)$). `HeuristicaBasica` suma les distàncies Manhattan individuals (ignorant parets i rastres), mentre que `HeuristicaAvancada` suma les distàncies reals amb BFS sobre la graella actual (retornant `INF` si algun agent queda aïllat) sense tenir en compte els rastres futurs.
* **Consistència (Sí, totes dues):** Al fer un pas de cost $1$, la distància de l'agent que es mou disminueix com a màxim en $1$ unitat, i la nova paret deixada pel seu rastre només pot mantenir igual o augmentar la distància de la resta d'agents ($h(n) \le 1 + h(n')$).
* **Conseqüència per a A\* amb LNT:** Com que $f(n) = g(n) + h(n)$ no és decreixent al llarg de qualsevol camí, el primer cop que A\* extreu un estat de la cua d'oberts ja hi ha arribat amb el cost òptim $g^*(n)$. Això garanteix **trobar sempre la solució òptima** i permet **descartar de forma segura** qualsevol successor ja present a la LNT sense necessitat de torntar-lo a obrir.

---

### Pregunta `[6]`: Quina heurística dona millors resultats? Per què?

En conjunt, **`HeuristicaAvancada`(`H.av`) ofereix resultats molts superiors** a `HeuristicaBasica`, tot i que existeix un compromís entre qualitat de la guia i cost computacional per node.

* **Superioritat en mapes amb obstacles o interaccions (`mapB2` i `mapD`):** `H.av` és una heurística més informada ($h_{\text{bas}}(n) \le h_{\text{av}}(n) \le h^*(n)$). Quan calcula les distàncies reals per BFS, té en compte les parets interiors i els rastres acumulats pels agents. A més, aporta una **poda anticipada molt potent**: si un agent queda tancat i no pot arribar a la seva sortida, `H.av` retorna `INF` i descarta immediatament la branca. Al `mapB2` (amb LNT), `H.av` redueix els nodes explorats de `4.745` (`H.bas`) a només `230` (20 vegades menys), i al `mapD` marca la diferència entre resoldre el mapa en `2,8 s` o esgotar la memòria (`OOM`).
* **Avantatge de `H.bas` en mapes oberts sense obstacles (`mapA`, `mapB1` i `mapC`):** Avaluar `H.bas` té cost $O(K)$ (on $K$ és el nombre d'agents), mentre que `H.av` executa $K$ recorreguts BFS sobre tota la graella ($O(K \cdot N \cdot M)$) per cada node generat. Quan no hi ha parets pel mig (com al `mapC`), ambdues exploren exactament els mateixos nodes (`113`), però `H.bas` triga només `11 ms` enfront dels `453 ms` de `H.av` (41 vegades més ràpida).

---

### Pregunta `[7]`: Expliqueu la influència de les heurístiques al mapa C.

El `mapC` és una graella molt gran de $25 \times 78$ **sense cap paret interior**, amb 3 agents (`A`, `B` i `C`) que tenen el camí completament lliure fins a les seves sortides a una distància òptima total de **112 passos**:

* **Les cerques cegues fallen totes per memòria (`OOM`):** Tant `BFS` (`67.793` nodes explorats, pic total de `434.166` estats i `6.364 ms`), com `DFS` (`252.272` nodes i `6.616 ms`) i `IDS` (`486.228` nodes i `10.899 ms`) esgoten els 2 GB de RAM abans d'arribar a profunditat 112. Amb 3 agents en un espai tan obert, l'arbre creix massa ràpid sense cap guia.
* **Comportament perfecte d'A\* amb les dues heurístiques:** Tant `A*(H.bas)` com `A*(H.av)` troben la solució òptima (`Longitud = 112`) explorant únicament **`113` nodes** (l'estat inicial més un sol node per cada pas, amb `0` nodes tallats i un pic de memòria total de només `1.206` estats: `547` oberts i `659` tancats). Això passa perquè, al no haver-hi parets ni creuaments entre els camins dels agents, la distància Manhattan i la distància real via BFS coincideixen exactament amb el cost real restant ($h(n) = h^*(n)$), fent que A\* vagi en línia recta fins a la meta sense desviar-se ni un cop.
* **Diferència en el temps d'execució:** L'única diferència real entre totes dues és el temps: `A*(H.bas)` tarda només **`11 ms`**, mentre que `A*(H.av)` puja a **`453 ms`** (unes 41 vegades més lenta), ja que calcular 3 recorreguts BFS sobre un tauler de $25 \times 78$ a cada node té un cost innecessari quan no hi ha obstacles.

---

### Pregunta `[8]`: Expliqueu la influència de les heurístiques al mapa D.

El `mapD` ($10 \times 16$, 2 agents `A` i `B`, solució òptima en **42 moviments**) ja incorpora parets interiors que formen obstacles i passos estrets on els agents han de fer la volta i coordinar-se per no tancar-se el pas. Aquí la diferència entre les dues heurístiques és decisiva:

* **`A*(H.bas)` no acaba i falla per falta de memòria (`OOM`):** Esgota els 2 GB de RAM als `29.978 ms` després d'explorar `705.981` nodes (`641.353` tallats) i acumular un pic de memòria total de `3.199.706` estats (`1.246.863` oberts i `1.952.843` tancats). Com que `H.bas` calcula la distància Manhattan ignorant les parets, es pensa que pot passar recte a través dels murs i queda atrapada explorant milers de combinacions contra la paret abans d'acceptar que cal rodejar l'obstacle; a més, tampoc detecta quan el rastre d'un agent talla el pas a l'altre.
* **`A*(H.av)` troba la solució òptima (`Longitud = 42`):** Acaba en **`2.812 ms`**, explorant només **`50.011` nodes** (`51.774` tallats) i amb un pic total de memòria de `291.378` estats (`120.684` oberts i `170.694` tancats). Al calcular les distàncies reals esquivant parets i rastres amb BFS, `H.av` guia els agents al voltant dels murs des del primer moment i poda de seguida (`INF`) qualsevol branca on un agent deixi tancat al company.

---

### Pregunta `[9]`: Problemes mapes E i F

Els algoritmes estàndard no són capaços de donar una solució en auqests mapes, perquè superen els límits de 120 segons d'execució i els 2GB de RAM de la JVM.

- BFS i $A^*$ (amb LNT): Esgoten els 2 GB de memòria (OutOfMemoryError) en emmagatzemar més de 3-5 times x 10^6 objectes Mapa a la LNO/LNT.
- IDS i $A^*$ (sense LNT): Esgoten el temps màxim de 120 s (Timeout) re-explorant permutacions repetides.
- DFS: Està limitat a profunditat 50, per la qual cosa és estructuralment incapaç d'assolir solucions més llargues.

### Pregunta `[12]`: Solució optima

Si tenim en compte que en aquest exercici tenim on totes les accions tenen cost unitari c = 1 i els camins són finits, BFS, A* amb totes les dues heuristiques i IDS, només sense LNT, donen una solució optima. Sent DFS i IDS amb LNT els únics que no et proporcionen la solució optima. 

### Pregunta `[13]`: BFS vs IDS

BFS té menys temps d'exevcusió però gasta més memòria en comparació amb IDS. Això s'ajusta amb la teoria ja que IDS serveix per aconseiguir el mateix resultat que BFS però amb un cost menor de memoria, però com funciona fent un DFS iterativament canviant el limit de profunditat, això provoca que hagi de visualtizar molts nodes, diversos revisant-los diverses maneres. 

