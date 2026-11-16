package edu.epsevg.prop.ac1.cerca.heuristica;

import edu.epsevg.prop.ac1.cerca.model.Mapa;
import edu.epsevg.prop.ac1.model.Posicio;

import java.util.List;

/**
 * Com {@link HeuristicaBasica} suma per cada agent actiu  la distància fins a la seva
 * pròpia sortida, però usant la distància real sobre la graella actual calculada amb BFS
 * tenint en compte les parets/rastres ja existents en lloc de la distància Manhattan.
 */
public class HeuristicaAvancada implements Heuristica {
    
  
    public HeuristicaAvancada() {
        //@TODO: implementar       
    }

    
    @Override
    public int h(Mapa estat) {
        int suma = 0;
        for (int i = 0; i < estat.getAgents().size(); ++i) {
            Posicio posAgent = estat.getAgents().get(i); 
            if (posAgent == null) continue; //si l'agent ja ha arribat a la seva sortida, el cost es 0
            Posicio posSortida = estat.getSortida(i);
            int[][] dist = estat.distancesFrom(posAgent); //calculem la distancia real per BFS evitant parets, els rastres i altres agents
            int distSortida = dist[posSortida.x][posSortida.y];
            if (distSortida >= Mapa.INF) return Mapa.INF;
            suma += distSortida;
        }
        return suma;
    }
    
    @Override
    public String getNom() {
        return "H.av";
    }

        // Podeu implementar aquí d'altres métodes que considereu necessaris
    // ...
}
