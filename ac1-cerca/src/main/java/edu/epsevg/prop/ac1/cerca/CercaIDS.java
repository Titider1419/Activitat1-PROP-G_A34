package edu.epsevg.prop.ac1.cerca;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.epsevg.prop.ac1.cerca.model.Mapa;
import edu.epsevg.prop.ac1.model.Moviment;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

public class CercaIDS extends Cerca {

    public CercaIDS(boolean usarLNT) {
        super(usarLNT);
    }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        boolean tallat = true;
        int i = 0;
        
        while (rc.getSolucio() == null && tallat) {
            Mapa map = new Mapa(inicial);
            tallat = ferCercaDFS(map, rc, i);
            if (tallat) ++i;
        }
    }

    private boolean ferCercaDFS(Mapa inicial, ResultatCerca rc, int limit) {
        boolean tallat = false;
        Deque<Node> oberts = new ArrayDeque<>();
        Map<Mapa, Integer> tancats = usarLNT ? new HashMap<>() : null;

        Node arrel = new Node(inicial, null, null, 0, 0);
        oberts.push(arrel);
        if (usarLNT) {
            tancats.put(inicial, 0);
        }

        while (!oberts.isEmpty()) {
            rc.updateMemoria(oberts.size(), usarLNT ? tancats.size() : 0);

            Node actual = oberts.pop();
            rc.incNodesExplorats();
            rc.actualitzaProfunditat(actual.depth);

            if (actual.estat.esFinalPartida()) {
                rc.setSolucio(reconstruirCami(actual));
                return tallat;
            }

            if (actual.depth >= limit) {
                rc.incNodesTallats();
                tallat = true; // ÚNICO sitio donde se pone a true
                continue;
            }

            List<Moviment> accions = actual.estat.getAccionsPossibles();

            for (int i = accions.size() - 1; i >= 0; i--) {
                Moviment m = accions.get(i);
                Mapa seguentEstat = (Mapa) actual.estat.mou(m);
                int novaProfunditat = actual.depth + 1;

                if (usarLNT) {
                    if (tancats.containsKey(seguentEstat) && tancats.get(seguentEstat) <= novaProfunditat) {
                        rc.incNodesTallats();
                        continue; // Sin tallat = true
                    }
                    tancats.put(seguentEstat, novaProfunditat);
                } else {
                    if (esCicleEnBranca(actual, seguentEstat)) {
                        rc.incNodesTallats();
                        continue; // Sin tallat = true
                    }
                }

                Node fill = new Node(seguentEstat, actual, m, novaProfunditat, actual.g + 1);
                oberts.push(fill);
            }
        }
        return tallat;
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
        return "IDS";
    }
}
