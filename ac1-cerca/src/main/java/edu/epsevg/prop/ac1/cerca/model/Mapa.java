package edu.epsevg.prop.ac1.cerca.model;

import edu.epsevg.prop.ac1.model.Direccio;
import edu.epsevg.prop.ac1.model.MapaBase;
import edu.epsevg.prop.ac1.model.Posicio;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Implementació concreta de {@link MapaBase}. Aquí és on cal decidir quan dos estats
 * són "el mateix" a efectes de detecció de cicles/repeticions (LNT): com que cada
 * moviment converteix la casella d'origen en paret de manera permanent, dos estats amb
 * els mateixos agents però un rastre de parets diferent <b>no</b> són el mateix estat
 * (podrien tenir moviments futurs possibles diferents). Per tant {@link #equals(Object)}
 * i {@link #hashCode()} han de tenir en compte tant la posició de cada agent com el
 * contingut complet de la graella.
 */
public class Mapa extends MapaBase {


    public Mapa(Path fitxer) throws IOException { super(fitxer); }

    public Mapa(Mapa other) { super(other); }

    @Override
    protected MapaBase clona() { return new Mapa(this); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (o instanceof Mapa other) {
            Mapa otherMapa = (Mapa) o;
            if(!this.agents.equals(otherMapa.agents)) return false;
            if(this.n == otherMapa.n && this.m == otherMapa.m) {
                for (int i = 0; i < n; i++) {
                    if (!Arrays.equals(this.grid[i], otherMapa.grid[i])) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + agents.hashCode();
        for(int i = 0; i < n; i++) {
            result = 31 * result + Arrays.deepHashCode(grid);
        }
        return result;
    }

    /**
     * Distàncies mínimes (en nombre de passos,  tenint en compte de no passar per les parets
     * actuals ni trepitjar els agents) des de l'agent a la posició {@code src} a cada casella de la graella,
     * calculades amb BFS.
     * Útil per construir heurístiques informades..
     * @return matriu de distàncies indexat per {@code x, y}; Retorn {@code Integer.MAX_VALUE/4}
     *         si la casella és inabastable
     */
    @Override
    public int[][] distancesFrom(Posicio src) {
        int[][] dist = new int[getN()][getM()];
        for(int[] fila:dist) Arrays.fill(fila, -1);
        dist[src.x][src.y] = 0;
        Deque<Posicio> oberts = new ArrayDeque<>();
        oberts.add(src);
        while(!oberts.isEmpty()){
            Posicio actual = oberts.poll();
            int dActual = dist[actual.x][actual.y];
            for(Direccio d : Direccio.values()){
                Posicio dest = actual.translate(d);
                if (getCella(dest) != PARET && dist[dest.x][dest.y] == -1){
                    boolean trobat = false;
                    int i = 0;
                    while(i < agents.size() && !trobat){
                        if(agents.get(i) != null && dest.x == agents.get(i).x && dest.y == agents.get(i).y) trobat = true;
                        else ++i;
                    }
                    if(!trobat){
                        dist[dest.x][dest.y] = dActual+1;
                        oberts.add(dest);
                    }
                }
                
            }
        }
        return dist;
    }
}
