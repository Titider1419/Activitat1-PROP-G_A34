package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.cerca.heuristica.Heuristica;
import edu.epsevg.prop.ac1.cerca.model.Mapa;
import edu.epsevg.prop.ac1.model.Moviment;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.*;

public class CercaIDAStar extends Cerca {
    private Heuristica heur;
    private record Auxiliar(Node node, int prioritat){}

    public CercaIDAStar(boolean usarLNT, Heuristica heur) {
        super(usarLNT);
        this.heur = heur;
    }

    @Override 
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        int limit = heur.h(inicial);
        if (limit == -1) return; // Mapa inicial sin solución

        while (rc.getSolucio() == null && limit != Integer.MAX_VALUE) {
            limit = ferCercaLCD(inicial, rc, limit);
        }
    }

    private int ferCercaLCD(Mapa inicial, ResultatCerca rc, int llindar) {
        Deque<Auxiliar> oberts = new ArrayDeque<>(); // Pila LIFO (consumo lineal de memoria)
        Map<Mapa, Integer> tancats = usarLNT ? new HashMap<>() : null;

        int hInicial = heur.h(inicial);
        if (hInicial == -1) return Integer.MAX_VALUE;

        Node arrel = new Node(inicial, null, null, 0, 0);
        oberts.push(new Auxiliar(arrel, hInicial));
        if (usarLNT) {
            tancats.put(inicial, 0);
        }

        int minSuperat = Integer.MAX_VALUE; // <-- Inicializado a infinito

        while (!oberts.isEmpty()) {
            rc.updateMemoria(oberts.size(), usarLNT ? tancats.size() : 0);
            Auxiliar actual = oberts.pop();

            // Si supera el umbral actual, cortamos esta rama y actualizamos el mínimo superado
            if (actual.prioritat() > llindar) {
                rc.incNodesTallats();
                if (actual.prioritat() < minSuperat) {
                    minSuperat = actual.prioritat();
                }
                continue;
            }

            if (usarLNT && tancats.containsKey(actual.node().estat) && tancats.get(actual.node().estat) < actual.node().g) {
                rc.incNodesTallats();
                continue;
            }

            rc.incNodesExplorats();
            rc.actualitzaProfunditat(actual.node().depth);

            if (actual.node().estat.esFinalPartida()) {
                rc.setSolucio(reconstruirCami(actual.node()));
                return actual.prioritat();
            }

            List<Moviment> accions = actual.node().estat.getAccionsPossibles();
            List<Auxiliar> fills = new ArrayList<>(accions.size());

            for (Moviment m : accions) {
                Mapa seguentEstat = (Mapa) actual.node().estat.mou(m);
                int nouHeur = heur.h(seguentEstat);
                if (nouHeur == -1) {
                    rc.incNodesTallats();
                    continue;
                }

                int nouCostG = actual.node().g + 1;
                int nouPrioritat = nouCostG + nouHeur;

                if (usarLNT) {
                    if (tancats.containsKey(seguentEstat) && tancats.get(seguentEstat) <= nouCostG) {
                        rc.incNodesTallats();
                        continue;
                    }
                    tancats.put(seguentEstat, nouCostG);
                } else {
                    if (esCicleEnBranca(actual.node(), seguentEstat)) {
                        rc.incNodesTallats();
                        continue;
                    }
                }

                Node fill = new Node(seguentEstat, actual.node(), m, actual.node().depth + 1, nouCostG);
                fills.add(new Auxiliar(fill, nouPrioritat));
            }

            // Ordenamos los hijos de mayor a menor prioridad antes de apilarlos (LIFO),
            // así el de menor f(n) queda arriba de la pila y se explora primero
            fills.sort((a, b) -> Integer.compare(b.prioritat(), a.prioritat()));
            for (Auxiliar f : fills) {
                oberts.push(f);
            }
        }

        return minSuperat;
    }

    //funció auxiliar per trobar nodes repetits en la mateixa rama pujant pels pares
    private boolean esCicleEnBranca(Node nodoActual, Mapa mapaNuevo) {
        Node curr = nodoActual;
        while (curr != null) {
            if (curr.estat.equals(mapaNuevo)) {
                return true; //trobat el node en el camí actual
            }
            curr = curr.pare;  //pujem un nivell cap al pare
        }
        return false; //vol dir que no hi ha cicle en aquesta rama 
    }

    //per cosntruir el camí solució, començem pel node arrel i anem cap a dalt mitjançant el pare fins arribar a l'arrel
    private List<Moviment> reconstruirCami(Node meta) {
        LinkedList<Moviment> cami = new LinkedList<>();
        Node curr = meta;
        while (curr.pare != null) {
            cami.addFirst(curr.accio); 
            curr = curr.pare;
        }
        return cami;
    }

    @Override
    public String getNomCerca() {   
        return "IDA*(" + heur.getNom() + ")";
    }
}
