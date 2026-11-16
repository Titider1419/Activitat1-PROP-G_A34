package edu.epsevg.prop.ac1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import edu.epsevg.prop.ac1.cerca.*;
import edu.epsevg.prop.ac1.cerca.heuristica.HeuristicaAvancada;
import edu.epsevg.prop.ac1.cerca.heuristica.HeuristicaBasica;
import edu.epsevg.prop.ac1.cerca.model.Mapa;
import edu.epsevg.prop.ac1.model.MapaBase;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;
import edu.epsevg.prop.ac1.utils.CsvWriter;

public class Main {
    static final String RESULTS_FILE = "results.csv";

    /** Límit màxim de temps per cerca establert a l'enunciat (120 segons). */
    private static final long TIMEOUT_SEGONS = 120L;

    /** Memòria màxima de la JVM per a cada experiment aïllat. */
    private static final String JVM_XMX = "-Xmx2g";

    /**
     * Si és {@code true}, executa només les proves que falten respecte al vostre
     * {@code ac1-app/results.csv} actual i les desa a {@code results_pendents.csv}.
     * Poseu-ho a {@code false} si voleu reexecutar tota la bateria completa des de zero
     * a {@code results.csv}.
     */
    private static final boolean NOMES_PENDENTS = true;

    /**
     * Directori on hi ha els fitxers {@code .map}. Per defecte és relatiu al directori
     * de treball actual (com quan s'executa {@code mvn exec:java} des d'{@code ac1-app}),
     * però es pot sobreescriure amb la propietat de sistema {@code mapes.dir}.
     */
    public static final String DIR_MAPES = System.getProperty("mapes.dir", "./src/main/resources");

    /** Capçalera comuna de les files de resultats (reutilitzada per l'eina d'avaluació). */
    public static final String[] CSV_HEADER = {"Algoritme", "usaLNT", "Mapa", "Trobat", "Valida", "Longitud",
            "NodesExplorats", "NodesTallats", "MemoriaPicOberts", "MemoriaPicTancats",
            "MemoriaPicTotal", "TempsMs"};

    private record Experiment(String codiAlgoritme, String nomMostrar, boolean usaLNT, String nomMapa) {}

    public static void main(String[] args) throws Exception {
        // Mode intern: executa un únic experiment en un procés aïllat amb heap net (-Xmx2g)
        if (args.length == 5 && "--single".equals(args[0])) {
            executarFillAillat(args[1], Boolean.parseBoolean(args[2]), args[3], args[4]);
            return;
        }

        List<Experiment> experiments = construirBateriaExperiments(NOMES_PENDENTS);
        String fitxerSortida = NOMES_PENDENTS ? "results_pendents.csv" : RESULTS_FILE;

        List<String[]> csv = new ArrayList<>();
        csv.add(CSV_HEADER);
        CsvWriter cw = new CsvWriter(fitxerSortida);

        printHeader("EXECUCIÓ DE PROVES (" + experiments.size() + " experiments, timeout=" + TIMEOUT_SEGONS + "s)");

        int idx = 1;
        for (Experiment exp : experiments) {
            System.out.println("[" + idx + "/" + experiments.size() + "] "
                    + exp.nomMostrar() + " (LNT=" + (exp.usaLNT() ? "YES" : "NO") + ") sobre " + exp.nomMapa());
            String[] fila = executarAmbTimeoutAillat(exp);
            csv.add(fila);
            cw.write(csv); // Desa incrementalment després de cada prova
            idx++;
        }

        printHeader("FINALITZAT -> Resultats desats a " + fitxerSortida);
    }

    private static List<Experiment> construirBateriaExperiments(boolean nomesPendents) {
        List<Experiment> llista = new ArrayList<>();

        // 1) mapA: bateria completa amb i sense LNT (molt ràpida, < 1 s en total)
        String[] algsTots = {"BFS", "DFS", "IDS", "ASTAR_BAS", "ASTAR_AV"};
        String[] nomsTots = {"BFS", "DFS", "IDS", "A*(H.bas)", "A*(H.av)"};
        for (int i = 0; i < algsTots.length; i++) {
            llista.add(new Experiment(algsTots[i], nomsTots[i], false, "mapA"));
            llista.add(new Experiment(algsTots[i], nomsTots[i], true, "mapA"));
        }

        if (nomesPendents) {
            // 2) mapB1 i mapB2: només falta BFS(LNT) (DFS, IDS i els 4 A* ja són a ac1-app/results.csv)
            llista.add(new Experiment("BFS", "BFS", true, "mapB1"));
            llista.add(new Experiment("BFS", "BFS", true, "mapB2"));

            // 3) mapC: cerques no informades amb LNT (A* bàsica i avançada ja hi són)
            llista.add(new Experiment("BFS", "BFS", true, "mapC"));
            llista.add(new Experiment("DFS", "DFS", true, "mapC"));
            llista.add(new Experiment("IDS", "IDS", true, "mapC"));

            // 4) mapD: A*(H.bas) amb LNT per comparar amb A*(H.av) (que ja teniu a 2.812 ms)
            llista.add(new Experiment("ASTAR_BAS", "A*(H.bas)", true, "mapD"));

            // 5) mapF: A*(H.av) amb LNT per completar les dades d'escalabilitat (mapE ja hi és)
            llista.add(new Experiment("ASTAR_AV", "A*(H.av)", true, "mapF"));
        } else {
            // Bateria completa per a mapB1 i mapB2
            for (String mapa : new String[] {"mapB1", "mapB2"}) {
                llista.add(new Experiment("BFS", "BFS", true, mapa));
                llista.add(new Experiment("DFS", "DFS", true, mapa));
                llista.add(new Experiment("IDS", "IDS", true, mapa));
                llista.add(new Experiment("ASTAR_BAS", "A*(H.bas)", false, mapa));
                llista.add(new Experiment("ASTAR_BAS", "A*(H.bas)", true, mapa));
                llista.add(new Experiment("ASTAR_AV", "A*(H.av)", false, mapa));
                llista.add(new Experiment("ASTAR_AV", "A*(H.av)", true, mapa));
            }
            // mapC
            llista.add(new Experiment("BFS", "BFS", true, "mapC"));
            llista.add(new Experiment("DFS", "DFS", true, "mapC"));
            llista.add(new Experiment("IDS", "IDS", true, "mapC"));
            llista.add(new Experiment("ASTAR_BAS", "A*(H.bas)", true, "mapC"));
            llista.add(new Experiment("ASTAR_AV", "A*(H.av)", true, "mapC"));
            // mapD
            llista.add(new Experiment("ASTAR_BAS", "A*(H.bas)", true, "mapD"));
            llista.add(new Experiment("ASTAR_AV", "A*(H.av)", true, "mapD"));
            // mapE i mapF
            llista.add(new Experiment("ASTAR_AV", "A*(H.av)", true, "mapE"));
            llista.add(new Experiment("ASTAR_AV", "A*(H.av)", true, "mapF"));
        }

        return llista;
    }

    private static Cerca instanciarCerca(String codiAlgoritme, boolean usaLNT) {
        return switch (codiAlgoritme) {
            case "BFS" -> new CercaBFS(usaLNT);
            case "DFS" -> new CercaDFS(usaLNT);
            case "IDS" -> new CercaIDS(usaLNT);
            case "ASTAR_BAS" -> new CercaAStar(usaLNT, new HeuristicaBasica());
            case "ASTAR_AV" -> new CercaAStar(usaLNT, new HeuristicaAvancada());
            default -> throw new IllegalArgumentException("Algoritme desconegut: " + codiAlgoritme);
        };
    }

    private static void executarFillAillat(String codiAlg, boolean usaLNT, String nomMapa, String fitxerTemp)
            throws Exception {
        Mapa mapa = new Mapa(Path.of(DIR_MAPES, nomMapa + MapaBase.MAP_FILE_EXTENSION));
        Cerca c = instanciarCerca(codiAlg, usaLNT);
        List<String[]> files = new ArrayList<>();
        executarIRecopilarResultats(c, mapa, files);
        new CsvWriter(fitxerTemp).write(files);
    }

    private static String[] executarAmbTimeoutAillat(Experiment exp) {
        Path tempCsv = null;
        try {
            tempCsv = Files.createTempFile("ac1_prova_", ".csv");
            String javaBin = Path.of(System.getProperty("java.home"), "bin", "java").toString();
            String classpath = construirClasspathComplet();
            String dirMapesAbsolut = new File(DIR_MAPES).getCanonicalPath();

            ProcessBuilder pb = new ProcessBuilder(
                    javaBin,
                    JVM_XMX,
                    "-Dmapes.dir=" + dirMapesAbsolut,
                    "-cp", classpath,
                    Main.class.getName(),
                    "--single",
                    exp.codiAlgoritme(),
                    String.valueOf(exp.usaLNT()),
                    exp.nomMapa(),
                    tempCsv.toAbsolutePath().toString()
            );
            pb.inheritIO();

            long inici = System.currentTimeMillis();
            Process proc = pb.start();
            boolean acabatATemps = proc.waitFor(TIMEOUT_SEGONS, TimeUnit.SECONDS);

            if (!acabatATemps) {
                proc.destroyForcibly();
                proc.waitFor(5, TimeUnit.SECONDS);
                System.out.println();
                System.out.println("> TIMEOUT (" + TIMEOUT_SEGONS + "s) assolits per "
                        + exp.nomMostrar() + " sobre " + exp.nomMapa());
                System.out.println();
                return new String[] {
                    exp.nomMostrar(),
                    exp.usaLNT() ? "YES" : "NO",
                    exp.nomMapa(),
                    "NO(TIMEOUT " + TIMEOUT_SEGONS + "s)",
                    "-", "-", "-", "-", "-", "-", "-",
                    String.valueOf(TIMEOUT_SEGONS * 1000L)
                };
            }

            // Llegeix la fila generada pel procés fill
            if (Files.exists(tempCsv) && Files.size(tempCsv) > 0) {
                try (BufferedReader br = new BufferedReader(new FileReader(tempCsv.toFile()))) {
                    String linia = br.readLine();
                    if (linia != null && !linia.isBlank()) {
                        return linia.split(String.valueOf(CsvWriter.CSV_SEPARATOR), -1);
                    }
                }
            }

            long durada = System.currentTimeMillis() - inici;
            return new String[] {
                exp.nomMostrar(),
                exp.usaLNT() ? "YES" : "NO",
                exp.nomMapa(),
                "NO(OUT OF MEMORY ERROR)",
                "-", "-", "-", "-", "-", "-", "-",
                String.valueOf(durada)
            };
        } catch (Exception e) {
            e.printStackTrace();
            return new String[] {
                exp.nomMostrar(),
                exp.usaLNT() ? "YES" : "NO",
                exp.nomMapa(),
                "NO(" + perCsv(e.toString()) + ")",
                "-", "-", "-", "-", "-", "-", "-", "0"
            };
        } finally {
            if (tempCsv != null) {
                try {
                    Files.deleteIfExists(tempCsv);
                } catch (IOException ignored) {}
            }
        }
    }

    /** Construeix el classpath real dels 3 mòduls (compatible tant amb run.bat/run.sh com amb NetBeans/mvn exec:java). */
    private static String construirClasspathComplet() {
        Set<String> rutes = new LinkedHashSet<>();
        Class<?>[] classesModul = {Main.class, Cerca.class, MapaBase.class};
        for (Class<?> cl : classesModul) {
            try {
                if (cl.getProtectionDomain() != null && cl.getProtectionDomain().getCodeSource() != null) {
                    rutes.add(new File(cl.getProtectionDomain().getCodeSource().getLocation().toURI()).getCanonicalPath());
                }
            } catch (Exception ignored) {}
        }
        String cpSistema = System.getProperty("java.class.path");
        if (cpSistema != null && !cpSistema.isBlank()) {
            Collections.addAll(rutes, cpSistema.split(File.pathSeparator));
        }
        return String.join(File.pathSeparator, rutes);
    }

    /**
     * Retorna la llista de mapes carregats a partir de cada fitxer {@code .map} del
     * directori {@code directory}, ordenats pel seu nom.
     */
    public static List<Mapa> loadMaps(final File directory) throws IOException {
        System.out.println("Carpeta actual:" + directory.getAbsolutePath());
        List<Mapa> maps = new ArrayList<>();
        if (!directory.exists()) {
            throw new RuntimeException("Directori de mapes no trobat");
        }
        File[] fitxers = directory.listFiles();
        if (fitxers == null) return maps;

        for (final File entry : fitxers) {
            if (entry.isFile() && entry.getName().endsWith(MapaBase.MAP_FILE_EXTENSION)) {
                try {
                    maps.add(new Mapa(Path.of(entry.getCanonicalPath())));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Collections.sort(maps);

        return maps;
    }

    static final int LONG_BAR_LENGTH = 66;
    static final int SHORT_BAR_LENGTH = 2;
    static final String BAR_CHARACTER = "=";
    static final String LONG_BAR = BAR_CHARACTER.repeat(LONG_BAR_LENGTH);
    static final String SHORT_BAR = BAR_CHARACTER.repeat(SHORT_BAR_LENGTH);

    /**
     * Pretty-prints the provided `text` centered in a box.
     */
    static void printHeader(final String text) {
        final String padding = " ".repeat((LONG_BAR_LENGTH - text.length() - (SHORT_BAR.length() * 2)) / 2);
        final String extraRightPadding = " ".repeat(text.length() % 2);

        System.out.println(LONG_BAR);
        System.out.println(SHORT_BAR + padding + text + padding + extraRightPadding + SHORT_BAR);
        System.out.println(LONG_BAR);
    }

    public static void executarIRecopilarResultats(Cerca c, Mapa map, List<String[]> rows) {
        String className = c.getClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);

        System.out.println("Executant " + className + " sobre " + map.name());

        ResultatCerca r = new ResultatCerca();
        String error = "";

        r.startTime(); // Engegar el temporitzador

        MonitorProgres monitor = new MonitorProgres(c, map, r);
        try {
            try {
                c.ferCerca(map, r);
            } finally {
                monitor.atura();
            }
            System.out.println("> SOLUCIO: " + r.getSolucio());
        } catch (OutOfMemoryError ooem) {
            error = "OUT OF MEMORY ERROR";
            System.out.println(error);
        } catch (Exception ex) {
            ex.printStackTrace();
            error = ex.toString();
        }

        r.stopTime(); // Aturar el temporitzador

        // Comprova que el camí retornat sigui realment una solució (fora del temps mesurat).
        String valida = ValidadorSolucio.valida(map, r.getSolucio());
        if (r.getSolucio() != null && !valida.equals(ValidadorSolucio.VALIDA)) {
            System.out.println("> SOLUCIO NO VALIDA: " + valida);
        }

        rows.add(new String[] {
            c.getNomCerca(),
            c.usesLNT() ? "YES" : "NO",
            map.name(),
            (r.getSolucio() != null) ? "YES" : "NO" + (error.isEmpty() ? "" : "(" + perCsv(error) + ")"),
            perCsv(valida),
            (r.getSolucio() != null) ? String.valueOf(r.getSolucio().size()) : "-",
            String.valueOf(r.getNodesExplorats()),
            String.valueOf(r.getNodesTallats()),
            String.valueOf(r.getMemoriaPicOberts()),
            String.valueOf(r.getMemoriaPicTancats()),
            String.valueOf(r.getMemoriaPicTotal()),
            String.valueOf(r.getTempsMs())
        });

        System.out.println("    " + className + " -> " + r);
        System.out.println();
    }

    /** Treu del text els separadors de camp i els salts de línia, que trencarien el CSV. */
    private static String perCsv(String text) {
        return text.replaceAll("[" + CsvWriter.CSV_SEPARATOR + "\\r\\n]+", " ");
    }

    private static final class MonitorProgres {
        private final ScheduledExecutorService executor;
        private final AtomicInteger longitudLinia = new AtomicInteger(0);

        MonitorProgres(Cerca c, Mapa map, ResultatCerca r) {
            ThreadFactory dimoni = runnable -> {
                Thread t = new Thread(runnable, "monitor-progres");
                t.setDaemon(true);
                return t;
            };
            executor = Executors.newSingleThreadScheduledExecutor(dimoni);
            executor.scheduleAtFixedRate(() -> imprimeix(c.getNomCerca(), map.name(), r),
                    1, 1, TimeUnit.SECONDS);
        }

        void atura() {
            executor.shutdown();
            try {
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            if (longitudLinia.get() > 0) System.out.println();
        }

        private void imprimeix(String nomCerca, String nomMapa, ResultatCerca r) {
            StringBuilder sb = new StringBuilder();
            sb.append('[').append(nomCerca).append('/').append(nomMapa).append("] ")
              .append(r.getTempsTranscorregutMs() / 1000).append("s: explorats=").append(r.getNodesExplorats())
              .append(" tallats=").append(r.getNodesTallats())
              .append(" profunditatMax=").append(r.getProfunditatMaxima())
              .append(" memòria(oberts/tancats/total)=").append(r.getMemoriaPicOberts()).append('/')
              .append(r.getMemoriaPicTancats()).append('/').append(r.getMemoriaPicTotal());
            if (r.teHeuristicaDisponible()) {
                sb.append(" heurMin=").append(r.getHeuristicaMinima());
            }

            int longitudActual = sb.length();
            int espaiSobrant = longitudLinia.getAndSet(longitudActual) - longitudActual;
            if (espaiSobrant > 0) sb.append(" ".repeat(espaiSobrant));

            System.out.print("\r" + sb);
            System.out.flush();
        }
    }
}