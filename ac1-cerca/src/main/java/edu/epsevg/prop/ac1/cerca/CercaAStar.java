package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.cerca.heuristica.Heuristica;
import edu.epsevg.prop.ac1.cerca.model.Mapa;
import edu.epsevg.prop.ac1.model.Moviment;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.*;

public class CercaAStar extends Cerca {

    private Heuristica heur;

    public CercaAStar(boolean usarLNT, Heuristica heur) {
        super(usarLNT);
        this.heur = heur;
    }


    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        // PriorityQueue ordenada por f(n) = g(n) + h(n)
        PriorityQueue<Node> oberts = new PriorityQueue<>((o1, o2) -> {
            int f1 = o1.g + heur.h(o1.estat);
            int f2 = o2.g + heur.h(o2.estat);
            return Integer.compare(f1, f2);
        });

        // Guardamos el mapa y el mejor coste 'g' con el que se ha alcanzado
        Map<Mapa, Integer> tancats = usarLNT ? new HashMap<>() : null;

        Node arrel = new Node(inicial, null, null, 0, 0);
        oberts.add(arrel);
        if (usarLNT) {
            tancats.put(inicial, 0);
        }

        while (!oberts.isEmpty()) {
            rc.updateMemoria(oberts.size(), usarLNT ? tancats.size() : 0);

            Node actual = oberts.poll(); // Extraemos el nodo con menor f(n)

            // Si ya habíamos expandido este estado con un coste g estrictamente menor, lo ignoramos
            if (usarLNT && tancats.containsKey(actual.estat) && tancats.get(actual.estat) < actual.g) {
                rc.incNodesTallats();
                continue;
            }

            rc.incNodesExplorats();
            rc.actualitzaProfunditat(actual.depth);

            if (actual.estat.esFinalPartida()) {
                rc.setSolucio(reconstruirCami(actual));
                return;
            }

            for (Moviment m : actual.estat.getAccionsPossibles()) {
                Mapa seguentEstat = (Mapa) actual.estat.mou(m);
                int nouCostG = actual.g + 1;

                if (usarLNT) {
                    // Solo descartamos si ya lo vimos con un coste menor o igual
                    if (tancats.containsKey(seguentEstat) && tancats.get(seguentEstat) <= nouCostG) {
                        rc.incNodesTallats();
                        continue;
                    }
                    tancats.put(seguentEstat, nouCostG);
                } else {
                    if (esCicleEnBranca(actual, seguentEstat)) {
                        rc.incNodesTallats();
                        continue;
                    }
                }

                Node fill = new Node(seguentEstat, actual, m, actual.depth + 1, nouCostG);
                oberts.add(fill);
            }
        }
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


    /**
     * Torna el nom de la cerca.
     * @return el nom de la cerca.
     */
    public String getNomCerca(){
        return "A*("+heur.getNom()+")";
    }
}
