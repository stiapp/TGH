/*
    Konvence je - vsecky komentare, nazvy metod a promennych v CESKEM jazyce.
    Metody i promenne se neboj pojmenovat takto:
    MetodaCoDelaKouzelneHodnoty(int id_hladoveho_zvirete_v_zoo)
    Díky :)
 */
package idos2;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

public class DijkstruvAlg {

    /* Smysluplny a vyčerpavajici zdroj pro pochopeni dijkstrova algoritmu - jeden z mála
       http://www.baeldung.com/java-dijkstra
    */
    
    public static void najdiNejkratsiCestuZeZdrojovehoVrcholu(Vrchol zdrojovyVrchol) {

        //nultý krok dijkstra algo
        zdrojovyVrchol.setVzdalenost(0);

        Set<Vrchol> vyhodnoceneVrcholy = new HashSet<>();
        Set<Vrchol> nevyhodnoceneVrcholy = new HashSet<>();
        
        /*vlozi do nevyhodnocenych jako prvni a ke kterym vede cesta, 
        zmeni vzdalenost z nekonecna - Integer.Max - na delku hran
        */
        nevyhodnoceneVrcholy.add(zdrojovyVrchol);

        //dokud nebyly vhodnoceny vsechny dosazitelne vrcholy
        //dokud je kolekce nevyhodnocenych neprazdna
        while (!nevyhodnoceneVrcholy.isEmpty()) {
            
            // Vezme z nevyhodnocenych ten, co ma nejnizsi vzdalenost
            Vrchol aktualniVrchol = getVrcholSNejmensiVzdalenosti(nevyhodnoceneVrcholy);
            
            // Musi se zaroven odebrat z nevyhodnocenych vrcholu
            nevyhodnoceneVrcholy.remove(aktualniVrchol);
            
            // Pruchod pres sousedy aktualne vyhodnocovaneho vrcholu aktualniVrchol
            for (Entry<Vrchol, Integer> s : aktualniVrchol.getSousedniVrcholy().entrySet()) {
                
                Vrchol sousedni_vrchol = s.getKey();
                Integer ohodnoceni_hrany = s.getValue();

                if (!vyhodnoceneVrcholy.contains(sousedni_vrchol)) {
                    // Vypocti vzdalenost k sousedovi
                    vypoctiNejmensiVzdalenost(sousedni_vrchol, ohodnoceni_hrany, aktualniVrchol);
                    
                    // Pro dalsi postup algoritmu se doposud nenavstiveny vrchol prida do kolekce pro vyhodnoceni
                    nevyhodnoceneVrcholy.add(sousedni_vrchol);
                }
            }
            vyhodnoceneVrcholy.add(aktualniVrchol);
        }
    }

    /**
     * 
     * @param vyhodnocovanyVrchol Vrchol sousedici s vrcholem, 
     * jemuz nyni dijkstra algoritmus vyhodnocuje vzdalenosti k jeho sousedum
     * @param hodnota_hrany  ohodnoceni hrany vedoucí k vrcholu sousednimu
     * @param zdrojovyVrchol vrchol ve kterem nyní jsem v dijkstra algoritmu
     */
    private static void vypoctiNejmensiVzdalenost(Vrchol vyhodnocovanyVrchol, Integer hodnota_hrany, Vrchol zdrojovyVrchol) {
                
        // Pokud existuje kratsi vzdalenost do vyhodnocovaneho vrcholu, nez ma nyni 
        if (zdrojovyVrchol.getVzdalenost() + hodnota_hrany < vyhodnocovanyVrchol.getVzdalenost()) {
            
            // Nastaveni vzdalenosti
            vyhodnocovanyVrchol.setVzdalenost(zdrojovyVrchol.getVzdalenost() + hodnota_hrany);
            
            // Zmeni se mu tim i dosavadni nejkratsi cesta k nemu
            LinkedList<Vrchol> nejkratsiCesta = new LinkedList<>(zdrojovyVrchol.getNejkratsiCesta());
            
            nejkratsiCesta.add(zdrojovyVrchol);
            vyhodnocovanyVrchol.setNejkratsiCesta(nejkratsiCesta);
        }
    }

    /** Vrati vrchol s nejnizsi vzdalenosti ze vstupni mnoziny vrcholu
     *   - s nejnizim atributem vzdalenost
     * @param vrcholy
     * @return 
     */
    private static Vrchol getVrcholSNejmensiVzdalenosti(Set<Vrchol> vrcholy) {
        Vrchol nejnizsiVzdalenost = null;
        int min = Integer.MAX_VALUE;
        for (Vrchol node : vrcholy) {

            if (node.getVzdalenost() < min) {
                min = node.getVzdalenost();
                nejnizsiVzdalenost = node;
            }
        }
        return nejnizsiVzdalenost;
    }
}