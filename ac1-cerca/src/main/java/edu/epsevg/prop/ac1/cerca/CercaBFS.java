package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.cerca.model.Mapa;
import edu.epsevg.prop.ac1.model.Moviment;
import edu.epsevg.prop.ac1.model.Posicio;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.*;

public class CercaBFS extends Cerca {

    public CercaBFS(boolean usarLNT) {
        super(usarLNT);
    }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        Deque<Node> oberts = new ArrayDeque<>(); //pila per guardar els nodes pendents

        Set<Mapa> tancats = usarLNT ? new HashSet<>() : null; //si usarLNT creem LNT, si no null

        Node arrel = new Node(inicial, null, null, 0, 0); //node inicial amb prof 0, cost 0 sense pare

        oberts.push(arrel); //apilem el node arrel i si el fem servir el guardem a la LNT
        if (usarLNT){
            tancats.add(inicial);
        }

        while(!oberts.isEmpty()){
            rc.updateMemoria(oberts.size(), usarLNT ? tancats.size() : 0); //registrem la mida per mesurar el pic de memoria

            Node actual = oberts.pop(); //extreiem el node + antic

            rc.incNodesExplorats(); //incrementem contador de nodes
            rc.actualitzaProfunditat(actual.depth); // registrem la profunditat
        
            if (actual.estat.esFinalPartida()){ //si tots els agents han sortit, hem acabat
                rc.setSolucio(reconstruirCami(actual));
                return;
            }

            List<Moviment> accions = actual.estat.getAccionsPossibles();

            for (int i = accions.size() - 1; i >= 0; i--) { //recorrem les accions en sentir invers per mantenir l'ordre LIFO
                Moviment m = accions.get(i);
                Mapa seguentEstat = (Mapa) actual.estat.mou(m);

                //controlem els cicles
                if (usarLNT) { //control global amb LNT
                    if (tancats.contains(seguentEstat)) {
                        rc.incNodesTallats();   //node repetit en algun moment de la cerca
                        continue;
                    }
                    tancats.add(seguentEstat);
                } 
                
                else { //control només dins la rama actual pujant pels pares
                    if (esCicleEnBranca(actual, seguentEstat)) {
                        rc.incNodesTallats();   //node repetit en el camí actual
                        continue;
                    }
                }

                //si es un estat vàlid i que no es repeteix, creem el node fill i l'apilem
                Node fill = new Node(seguentEstat, actual, m, actual.depth + 1, actual.g + 1);
                oberts.addLast(fill);
            }
        }
    }

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
        return "BFS";
    }    
}
