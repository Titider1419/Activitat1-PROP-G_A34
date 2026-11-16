package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.cerca.heuristica.Heuristica;
import edu.epsevg.prop.ac1.cerca.model.Mapa;
import edu.epsevg.prop.ac1.model.Moviment;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.*;

public class CercaAStar extends Cerca {

    private final Heuristica heur;

    //creem una clase per guardar h(n) i f(n) = g(n) + h(n)
    //i així evitem que la PriorityQueue recalculi heur.h()
    private static final class NodeAStar {
        final Node node;
        final int h;
        final int f;

        NodeAStar(Node node, int h) {
            this.node = node;
            this.h = h;
            this.f = node.g + h;
        }
    }

    public CercaAStar(boolean usarLNT, Heuristica heur) {
        super(usarLNT);
        this.heur = heur;
    }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        //Ordenem la cola de prioritatper f(n) + petita
        //Si hi ha empat, desampatem per h(n) + petita per prioritzar nodes propers al objectiu
        PriorityQueue<NodeAStar> oberts = new PriorityQueue<>((o1, o2) -> {
            int cmp = Integer.compare(o1.f, o2.f);
            return (cmp != 0) ? cmp : Integer.compare(o1.h, o2.h);
        });

        //Guardem a LNT cada estat amb el millor g que hem aconseguit 
        Map<Mapa, Integer> tancats = usarLNT ? new HashMap<>() : null;

        Node arrel = new Node(inicial, null, null, 0, 0);
        //Calculem h incial un sol cop
        oberts.add(new NodeAStar(arrel, heur.h(inicial)));
        if (usarLNT) {
            tancats.put(inicial, 0);
        }

        while (!oberts.isEmpty()) {
            rc.updateMemoria(oberts.size(), usarLNT ? tancats.size() : 0);

            NodeAStar actualAS = oberts.poll();
            Node actual = actualAS.node;

            //Si aquest estat entra diferents cops en oberts i ja el vam expandir amb un g + petit, el descartem directament
            if (usarLNT && tancats.containsKey(actual.estat) && tancats.get(actual.estat) < actual.g) {
                rc.incNodesTallats();
                continue;
            }

            rc.incNodesExplorats();
            rc.actualitzaProfunditat(actual.depth);
            //Registrem la heuritsica minima explorada 
            rc.actualitzaHeuristica(actualAS.h);

            if (actual.estat.esFinalPartida()) {
                rc.setSolucio(reconstruirCami(actual));
                return;
            }

            for (Moviment m : actual.estat.getAccionsPossibles()) {
                Mapa seguentEstat = (Mapa) actual.estat.mou(m);
                int nouCostG = actual.g + 1;
                
                //descartem l'estat si alguna sortida ja es innaccessible
                int hFill = heur.h(seguentEstat);
                if (hFill >= Mapa.INF) {
                    rc.incNodesTallats();
                    continue;
                }

                if (usarLNT) {
                    //Només descartem si ja tenim aquest estat amb un cost menor o igual
                    if (tancats.containsKey(seguentEstat) && tancats.get(seguentEstat) <= nouCostG) {
                        rc.incNodesTallats();
                        continue;
                    }
                    tancats.put(seguentEstat, nouCostG);
                } else {
                    //Sense LNT, evitem repetir estats dins de la mateixa rama
                    if (esCicleEnBranca(actual, seguentEstat)) {
                        rc.incNodesTallats();
                        continue;
                    }
                }

                Node fill = new Node(seguentEstat, actual, m, actual.depth + 1, nouCostG);
                //Calculem h del següent un unic cop al insertar-ho
                oberts.add(new NodeAStar(fill, heur.h(seguentEstat)));
            }
        }
    }

    private boolean esCicleEnBranca(Node nodoActual, Mapa mapaNuevo) {
        Node curr = nodoActual;
        while (curr != null) {
            if (curr.estat.equals(mapaNuevo)) {
                return true;
            }
            curr = curr.pare;
        }
        return false;
    }

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
        return "A*(" + heur.getNom() + ")";
    }
}