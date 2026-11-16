# ESTUDI - ACTIVITAT1: MULTI-SNAKE G_A34

## Entorn d'execució i configuració de proves

* **CPU:** *13th Gen Intel(R) Core(TM) i7-13620H*
* **Memòria RAM total:** 15,7 GB
* **Sistema Operatiu:** Windows (64 bits)
* **Entorn Java:** Java 17 amb Maven, memòria màxima -Xmx2g per prova i temps límit de 120 s.
* **Límit de profunditat de DFS:** Fixat a 50 per evitar el pou de perdició.

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


Si observem la columna `Nodes Tallats` de la taula anterior en les execucions amb `usarLNT = NO` de **BFS** , **DFS** i **A\*** dels mapes A, B1 i B2, el valor és sempre 0.
*En el cas IDS amb usarLNT = NO, els 697.814 nodes tallats corresponen a la poda pel límit de profunditat de cada iteració*


### Pregunta `[3]`: Calculeu el factor d’embrancament mitjà (real) del mapa A per BFS usant LNT i sense fer-la servir. Justifiqueu els avantatges d’usar la LNT respecte a no fer-la servir.
### Pregunta `[3]`: Calculeu el factor d’embrancament mitjà (real) del mapa A per BFS usant LNT i sense fer-la servir. Justifiqueu els avantatges d’usar la LNT respecte a no fer-la servir.

Al `mapA`, els dos casos acaben la cerca i troben la solució òptima a profunditat $d = 12$. El factor d'embrancament efectiu $b^*$ es calcula a partir del total de nodes explorats $N$ resolent:

$$N = \sum_{i=0}^{d} (b^*)^i = \frac{(b^*)^{d+1} - 1}{b^* - 1}$$

* **BFS sense LNT (`usaLNT = NO`):** amb $N = 539.471$ nodes explorats i $d = 12$, obtenim **$b^* = 2,90$** (aproximació $539.471^{1/12} = 3,00$).
* **BFS amb LNT (`usaLNT = YES`):** amb $N = 5.052$ nodes explorats i $d = 12$, obtenim **$b^* = 1,91$** (aproximació $5.052^{1/12} = 2,04$).

| Configuració (`mapA`) | Acaba / Òptima | Explorats | Tallats | Pic LNO | Pic LNT | Pic Total | Temps |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| BFS (`usaLNT = NO`) | Sí / Sí (12) | 539.471 | 0 | 452.539 | 0 | 452.539 | 627 ms |
| BFS (`usaLNT = YES`) | Sí / Sí (12) | 5.052 | 5.514 | 1.235 | 6.003 | 6.955 | 49 ms |

Encara que no hi hagi cicles dins d'una mateixa branca, sí que hi ha moltes branques diferents que porten exactament al mateix estat (per exemple, moure primer l'agent A i després el B deixa les mateixes posicions i el mateix rastre de parets que fer-ho a l'inrevés). Sense LNT, BFS expandeix aquests estats repetits una vegada i una altra. En activar la LNT es tallen 5.514 repeticions, cosa que redueix els nodes explorats en més d'un 99%, baixa el temps de 627 ms a 49 ms i evita que la cua d'oberts (LNO) s'infli, reduint la memòria pic total de 452.539 a 6.955 estats.

---

### Pregunta `[4]`: Expliqueu les diferències de resultats entre el mapa B1 i B2.

Els dos mapes tenen exactament les mateixes dimensions, cap paret interior, les mateixes sortides (`a` a (4,0) i `b` a (5,9)) i la mateixa posició inicial per a l'agent `B` ((2,2)). L'única diferència és la posició inicial de l'agent `A`:
* A **`mapB1`**, `A` comença a (3,6).
* A **`mapB2`**, `A` comença dues files més amunt, a (1,6).

Aquest canvi que sembla petit altera completament la interacció entre les trajectòries dels dos agents:

| Mapa / Algorisme (`usaLNT = YES`) | Estat | Longitud | Explorats | Tallats | Pic LNO | Pic LNT | Pic Total | Temps |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **mapB1** — DFS | Trobat (Subòpt.) | 35 | 1.459.356 | 1.597.285 | 112 | 1.459.440 | 1.459.526 | 4.196 ms |
| **mapB2** — DFS | No acaba (OOM) | - | 3.501.832 | 3.732.315 | 121 | 3.501.896 | 3.501.961 | 69.130 ms |
| **mapB1** — A*(H.bas) | Trobat (Òptima) | 17 | 28 | 0 | 66 | 93 | 159 | 2 ms |
| **mapB2** — A*(H.bas) | Trobat (Òptima) | 19 | 4.745 | 3.920 | 9.024 | 13.768 | 22.792 | 85 ms |
| **mapB1** — A*(H.av) | Trobat (Òptima) | 17 | 18 | 0 | 60 | 77 | 137 | 6 ms |
| **mapB2** — A*(H.av) | Trobat (Òptima) | 19 | 230 | 187 | 850 | 1.079 | 1.929 | 19 ms |

* **Canvi en la longitud òptima (de 17 a 19):** Al `mapB1`, `A` és a distància Manhattan 7 de la seva sortida i `B` a distància 10, amb trajectòries separades que no es molesten ($7 + 10 = 17$). Al `mapB2`, en sortir dues files més amunt, `A` necessita 9 passos ($9 + 10 = 19$) i el seu camí cap a baix a l'esquerra es creua al mig del tauler amb el camí de `B`.
* **Bloqueig de DFS al `mapB2`:** Al `mapB1`, com que els agents no es tanquen el pas, DFS acaba trobant una solució subòptima de 35 passos. Al `mapB2`, en canvi, el primer agent que creua el centre deixa un rastre de parets que talla el pas a l'altre, obligant DFS a fer *backtracking* constant dins el límit de profunditat 50 fins a esgotar els 2 GB de memòria (`OOM`). (Pel que fa a BFS i IDS, tots dos fallen per `OOM` als dos mapes per l'amplada de l'espai d'estats).
* **Diferència entre heurístiques a A*:** Al `mapB1` gairebé no hi ha interferències i tant `H.bas` com `H.av` van directes a la meta (28 i 18 nodes explorats). Al `mapB2`, com que `H.bas` només mira la distància Manhattan i ignora els rastres dels agents, ha d'explorar 4.745 nodes amb LNT (i 648.328 nodes en 3.803 ms sense LNT). En canvi, `H.av` detecta mitjançant BFS quan un rastre talla el pas (`INF`) i resol el mapa en només 230 nodes i 19 ms.
---

### Pregunta `[5]`: Expliqueu de forma justificada si les heurístiques proposades són admissibles. Són també consistents? Quina conseqüència té la consistència per a A* amb LNT?

* **Admissibilitat (Sí, totes dues):** Cap de les dues sobreestima mai el cost real mínim ($h(n) \le h^*(n)$). `HeuristicaBasica` suma les distàncies Manhattan individuals (ignorant parets i rastres), mentre que `HeuristicaAvancada` suma les distàncies reals amb BFS sobre la graella actual (retornant `INF` si algun agent queda aïllat) sense tenir en compte els rastres futurs.
* **Consistència (Sí, totes dues):** Al fer un pas de cost $1$, la distància de l'agent que es mou disminueix com a màxim en $1$ unitat, i la nova paret deixada pel seu rastre només pot mantenir igual o augmentar la distància de la resta d'agents ($h(n) \le 1 + h(n')$).
* **Conseqüència per a A\* amb LNT:** Com que $f(n) = g(n) + h(n)$ no és decreixent al llarg de qualsevol camí, el primer cop que A\* extreu un estat de la cua d'oberts ja hi ha arribat amb el cost òptim $g^*(n)$. Això garanteix trobar sempre la solució òptima i permet descartar de forma segura qualsevol successor ja present a la LNT sense necessitat de tornar-lo a obrir.

---

### Pregunta `[6]`: Quina heurística dona millors resultats? Per què?

En general, **`HeuristicaAvancada` (`H.av`) dona molt millors resultats** que `HeuristicaBasica` (`H.bas`), tot i que depèn de si el mapa té parets o bloquejos entre agents:

* **Quan hi ha parets o es creuen els agents (`mapB2` i `mapD`):** `H.av` és força més informada ($h_{\text{bas}}(n) \le h_{\text{av}}(n) \le h^*(n)$) perquè calcula les distàncies reals fent un BFS sobre el tauler actual, tenint en compte tant els murs com els rastres que van deixant els agents. A més, si un agent queda tancat i no pot arribar a la sortida, retorna `INF` i talla la branca al moment. Gràcies a això, al `mapB2` (amb LNT) baixa de 4.745 nodes explorats i 85 ms amb `H.bas` a només 230 nodes i 19 ms amb `H.av`, i al `mapD` aconsegueix resoldre el mapa mentre que `H.bas` es queda sense memòria.
* **Quan el mapa és obert i no hi ha obstacles (`mapA`, `mapB1` i `mapC`):** Aquí `H.bas` surt més a compte en temps. Calcular `H.bas` costa $O(K)$ (on $K$ és el nombre d'agents), mentre que `H.av` ha de llançar $K$ recorreguts BFS per cada node que es genera. Al `mapC` (amb LNT), per exemple, totes dues exploren exactament els mateixos nodes (113 nodes, 0 tallats, memòria pic total de 1.206), però `H.bas` tarda només 11 ms i `H.av` puja a 453 ms.
---

### Pregunta `[7]`: Expliqueu la influència de les heurístiques al mapa C.

El `mapC` és una graella molt gran de $25 \times 78$ **sense cap paret interior**, amb 3 agents que tenen el camí completament lliure fins a les seves sortides a una distància òptima total de **112 passos**:

* **Les cerques cegues fallen totes per memòria (`OOM`):** Tant `BFS` (67.793 nodes explorats, pic total de 434.166 estats i 6.364 ms), com `DFS` (252.272 nodes i 6.616 ms) i `IDS` (486.228 nodes i 10.899 ms) esgoten els 2 GB de RAM abans d'arribar a profunditat 112. Amb 3 agents en un espai tan obert, l'arbre creix massa ràpid sense cap guia.
* **Comportament perfecte d'A\* amb les dues heurístiques:** Tant `A*(H.bas)` com `A*(H.av)` troben la solució òptima (Longitud = 112) explorant únicament 113 nodes. Això passa perquè la distància Manhattan i la distància real via BFS coincideixen exactament amb el cost real que queda, i fa  que A\* vagi en línia recta fins a la meta sense desviar-se.
* **Diferència en el temps d'execució:** L'única diferència entre totes dues és el temps: `A*(H.bas)` tarda només 11 ms, mentre que `A*(H.av)` puja a 453 ms, ja que calcular 3 recorreguts BFS sobre un tauler de $25 \times 78$ a cada node té un cost innecessari quan no hi ha obstacles.

---

### Pregunta `[8]`: Expliqueu la influència de les heurístiques al mapa D.

El `mapD` ($10 \times 16$, 2 agents `A` i `B`, solució òptima en 42 moviments) ja incorpora parets interiors que formen obstacles i passos estrets on els agents han de fer la volta i coordinar-se per no tancar-se el pas. Aquí la diferència entre les dues heurístiques és decisiva:

* **`A*(H.bas)` no acaba i falla per falta de memòria (`OOM`):** Esgota els 2 GB de RAM als 29.978 ms després d'explorar 705.981 nodes (641.353 tallats) i acumular un pic de memòria total de 3.199.706 estats (1.246.863 oberts i 1.952.843 tancats). Com que `H.bas` calcula la distància Manhattan ignorant les parets, es pensa que pot passar recte a través dels murs i queda atrapada explorant milers de combinacions contra la paret abans d'acceptar que cal rodejar l'obstacle; a més, tampoc detecta quan el rastre d'un agent talla el pas a l'altre.
* **`A*(H.av)` troba la solució òptima (Longitud = 42):** Acaba en 2.812 ms, explorant només 50.011 nodes (51.774 tallats) i amb un pic total de memòria de 291.378 estats (120.684 oberts i 170.694 tancats). Al calcular les distàncies reals esquivant parets i rastres amb BFS, `H.av` guia els agents al voltant dels murs des del primer moment i poda de seguida (`INF`) qualsevol branca on un agent deixi tancat al company.

---

### Pregunta `[9]`: Per què els algorismes estàndard no són capaços de donar una solució als mapes E i F? Justifiqueu-ho numèricament.

Els algoritmes estàndard no són capaços de donar una solució en aquests mapes, perquè superen els límits de 120 segons d'execució i els 2 GB de RAM de la JVM, a més de ser graelles molt més grans on cada objecte `Mapa` ocupa molta més memòria:

* **BFS i A\* (amb LNT):** Esgoten els 2 GB de memòria (`OutOfMemoryError`) en emmagatzemar milions d'objectes `Mapa` a la LNO/LNT (als mapes petits cabien més de $3\text{–}4 \times 10^6$ estats, mentre que al `mapE` l'objecte és tan gran que `A*(H.av)` ja omple la RAM amb un pic total de 829.890 estats —370.998 oberts i 458.892 tancats— després d'explorar 87.895 nodes i tallar-ne 76.148 en 112.249 ms).
* **IDS i A\* (sense LNT), i `A*(H.av)` al `mapF`:** Esgoten el temps màxim de 120 s (`Timeout`) re-explorant permutacions repetides dels agents. Al `mapF`, fins i tot `A*(H.av)` amb LNT arriba als 120.000 ms pel cost de calcular les distàncies BFS a cada node en un tauler tan gran.
* **DFS:** Està limitat a profunditat 50, per la qual cosa és estructuralment incapaç d'assolir solucions més llargues.

---

### Pregunta `[10]`: Quin altre enfocament podríem prendre per solucionar E i F? Si heu implementat el repte, discutiu si el vostre algorisme és complet i si garanteix la solució òptima.

Potser un enfocament diferent que podríem haver utilitzat és mirar de fusionar diferents algoritmes per agafar els seus avantatges i intentar d'eliminar els seus defectes. En canvi, nosaltres hem pensat de fer l'algoritme **IDA\***, però ho hem fet de forma iterativa; creiem que aquest podria ser el motiu pel qual no hem arribat a aconseguir cap solució pels dos mapes. En teoria havíem pensat en aquesta solució ja que ens permet no consumir tanta memòria a canvi de fer que l'algoritme tardi més, i sense perdre la capacitat d'obtenir la solució òptima.

Pel que fa al repte, el nostre **IDA\* sí que és complet i garanteix la solució òptima** (sempre que no s'esgoti el temps límit). És complet perquè el tauler és finit i cada moviment deixa una paret fixa, així que l'espai d'estats és finit i sense cicles; i garanteix l'òptim perquè fem servir una heurística admissible i consistent i anem pujant el llindar de cost $f(n) = g(n) + h(n)$ al mínim que el supera a cada iteració. Per evitar que es quedi en `Timeout`, hauríem d'evitar re-explorar intercalacions repetides dels agents (per exemple, agrupant moviments d'un mateix agent) o precalcular les distàncies BFS de les parets estàtiques un sol cop a l'inici.

---

### Pregunta `[11]`: Demostreu que, en aquest problema, l’ordre en què es mouen els agents no afecta la validesa ni la longitud d’una solució (pista: els camins de dos agents diferents mai comparteixen cap casella). Si els agents fan N moviments cadascun, quantes seqüències de moviments diferents representen la mateixa solució? Com podríeu aprofitar-ho per reduir l’espai de cerca?

### Pregunta `[11]`: Demostreu que, en aquest problema, l’ordre en què es mouen els agents no afecta la validesa ni la longitud d’una solució (pista: els camins de dos agents diferents mai comparteixen cap casella). Si els agents fan N moviments cadascun, quantes seqüències de moviments diferents representen la mateixa solució? Com podríeu aprofitar-ho per reduir l’espai de cerca?

Sigui una solució vàlida $S$ formada pels camins individuals $P_1, P_2, \dots, P_k$ dels $k$ agents des de les seves posicions inicials fins a les seves sortides respectives. Com que cada casella que deixa un agent es converteix en una paret (`#`) i dos agents no poden trepitjar mai la mateixa casella ni la sortida d'un company, perquè $S$ sigui vàlida els conjunts de caselles dels camins $P_1, \dots, P_k$ han de ser disjunts ($V(P_i) \cap V(P_j) = \emptyset$ per a tot $i \ne j$). Si compartissin alguna casella, el primer agent que hi passés la deixaria bloquejada i impediria acabar al segon.

Com que els camins no es creuen enlloc, intercalar els moviments dels agents en qualsevol ordre no provoca cap col·lisió (manté la validesa) i la longitud total continua sent exactament $\sum_{i=1}^{k} |P_i|$ moviments.

Si tenim $k$ agents i cadascun fa $N$ moviments (en total $k \cdot N$ passos), com que els $N$ moviments d'un mateix agent sempre van en ordre, el nombre de seqüències diferents que representen la mateixa solució és:

$$\frac{(k \cdot N)!}{(N!)^k}$$

Per aprofitar-ho i reduir l'espai de cerca podem fer servir la **LNT** (ja que totes aquestes seqüències porten al mateix estat final de posicions i parets i es poden tallar) o **fixar un ordre de prioritat** entre agents perquè no s'intercalin a cada nivell quan no es molesten entre ells.

---

### Pregunta `[12]`: Quins dels algorismes implementats (BFS, DFS, IDS, A* amb cada heurística) garanteixen trobar la solució òptima en aquest problema? Especifica la resposta quan s’usa o no la LNT.

Si tenim en compte que en aquest exercici totes les accions tenen cost unitari $c = 1$ i els camins són finits, **BFS**, **A\*** amb totes dues heurístiques i **IDS** donen una solució òptima, sent **DFS** l'únic que no et proporciona la solució òptima:

* **A\* (`H.bas` i `H.av`):** Aquest algoritme gràcies a l'heurística i els costos dona la solució òptima **tant sense LNT com amb LNT**. Sense LNT ho garanteix perquè totes dues heurístiques són admissibles ($h(n) \le h^*(n)$), i amb LNT també perquè a més són consistents (el primer cop que un node surt de la LNO ja té el cost òptim $g^*(n)$, així que descartar repetits a la LNT no fa perdre l'òptim).
* **IDS i BFS:** Només donen la solució òptima perquè com tots els moviments tenen un cost unitari de $c = 1$, llavors la solució òptima és la més propera a l'arrel, justament la que proporcionen aquests algoritmes en explorar per nivells ($d = 0, 1, 2, \dots$). Són òptims **tant amb LNT com sense**, ja que amb cost unitari la primera vegada que es visita un estat sempre és pel camí més curt.
* **DFS:** **No garanteix l'òptim** ni amb LNT ni sense, ja que baixa per una branca fins al límit abans de provar alternatives i la primera solució que troba depèn de l'ordre d'expansió dels fills (al `mapA` dona longitud 16 en lloc de 12, i al `mapB1` dona 35 en lloc de 17).

---

### Pregunta `[13]`: Compareu la memòria i el temps de BFS i IDS en els mapes on tots dos acaben. S’ajusten els resultats al que prediu la teoria?

**BFS** té menys temps d'execució però gasta més memòria en comparació amb **IDS**. Això s'ajusta amb la teoria ja que IDS serveix per aconseguir el mateix resultat òptim que BFS però amb un cost menor de memòria ($O(b \cdot d)$ a la LNO enfront de $O(b^d)$); en canvi, com funciona fent un DFS iterativament canviant el límit de profunditat, això provoca que hagi de visitar molts nodes repetidament a cada iteració.

L'únic mapa on tots dos acaben sense quedar-se sense memòria (`OOM`) és el **Mapa A** (tots dos troben la solució òptima de longitud 12):

* **Comparació de dades Mapa A (sense LNT):**
  * **BFS (`usaLNT = NO`):** Temps: 627 ms | Nodes explorats: 539.471 | Tallats: 0 | Memòria pic: 452.539 oberts / 0 tancats / 452.539 total.
  * **IDS (`usaLNT = NO`):** Temps: 651 ms | Nodes explorats: 1.348.840 | Tallats: 697.814 (pel límit de profunditat) | Memòria pic: 31 oberts / 0 tancats / 31 total.
* **Comparació de dades Mapa A (amb LNT):**
  * **BFS (`usaLNT = YES`):** Temps: 49 ms | Nodes explorats: 5.052 | Tallats: 5.514 | Memòria pic: 1.235 oberts / 6.003 tancats / 6.955 total.
  * **IDS (`usaLNT = YES`):** Temps: 100 ms | Nodes explorats: 20.968 | Tallats: 24.067 | Memòria pic: 31 oberts / 5.223 tancats / 5.237 total.

