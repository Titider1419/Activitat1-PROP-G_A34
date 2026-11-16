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



