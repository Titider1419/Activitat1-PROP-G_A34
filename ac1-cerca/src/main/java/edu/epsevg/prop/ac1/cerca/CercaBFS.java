package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.cerca.model.Mapa;
import edu.epsevg.prop.ac1.model.Moviment;
import edu.epsevg.prop.ac1.model.Posicio;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.*;

public class CercaBFS extends Cerca {

    private ArrayList<Node> LNT = new ArrayList<>();

    public CercaBFS(boolean usarLNT) {
        super(usarLNT);
    }
    
    public ArrayList<Node> getLNT() {
        return LNT;
    }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        ArrayDeque<Node> LNO = new ArrayDeque<>();
        LNO.add(new Node(inicial, null, null, 0, 0));
        while (!LNO.isEmpty()) {
            Node actual = LNO.poll();
            rc.incNodesExplorats();
            if(!actual.estat.esFinalPartida()){
                if(usesLNT()){
                    LNT.add(actual);
                }
                List<Moviment> accions = actual.estat.getAccionsPossibles();
                for (Moviment accio : accions) {
                    Node seguent = new Node((Mapa)(actual.estat.mou(accio)), actual, accio, actual.depth+1, actual.g+1);
                    boolean trobat = false;
                    if(usesLNT()){
                        int i = 0;
                        while(i < LNT.size() && !trobat){
                            if(LNT.get(i).estat.equals(seguent.estat)) trobat = true;
                            else ++i;
                        }
                    }
                    else{
                        Node aux = actual;
                        while(aux != null && !trobat){
                            if(aux.estat.equals(seguent.estat)) trobat = true;
                            else aux = aux.pare;
                        }
                    }
                    if(!trobat) LNO.addLast(seguent);
                }
            }
            else{
                List<Moviment> accions = new ArrayList<>();
                while(actual != null){
                    accions.addFirst(actual.accio);
                    actual = actual.pare;
                }
                rc.setSolucio(accions);
            }
        }
    }
    
    /**
     * Torna el nom de la cerca.
     * @return el nom de la cerca.
     */
    public String getNomCerca(){
        return "BFS";
    }    
}
