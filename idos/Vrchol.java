/*
    Konvence je - vsecky komentare, nazvy metod a promennych v CESKEM jazyce.
    Metody i promenne se neboj pojmenovat takto:
    MetodaCoDelaKouzelneHodnoty(int id_hladoveho_zvirete_v_zoo)
    Díky :)
 */
package martin.TGH.idos;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Vrchol {

    private int cisloStanice;
    private int casVeStanici;

    /* po pruchodu Dijkstra algoritmu ze zadaneho vrcholu je v
      seznamu nejkratsiCesta cesta od zadaneho vrcholu k tomuto - ta nejkratsi */
    private LinkedList<Vrchol> nejkratsiCesta = new LinkedList<>();

    //pouzivame objektovy integer kvuli mape sousednichVrch
    private Integer vzdalenost = Integer.MAX_VALUE;
    
    //Vrchol a jak je daleko od instance tohoto vrcholu
    private Map<Vrchol, Integer> sousedniVrcholy = new HashMap<>();

    /** Konstruktor
     * 
     * @param cislo_stanice
     * @param cas_ve_stanici
     */
    public Vrchol(int cislo_stanice, int cas_ve_stanici) {

        boolean chyba = false;
        chyba = (cislo_stanice < 0 || cas_ve_stanici < 0 || cas_ve_stanici >= 1440);
        //1440 pocet minut od zacatku dne - ze zadani neprekroci pulnoc
        if (chyba) {
            throw new IllegalArgumentException("");
        }

        this.casVeStanici = cas_ve_stanici;
        this.cisloStanice = cislo_stanice;

    }
    
    /** 
     * Přidá orientovanou a ohodnocenou hranu od této instance
     * 
     * @param cilovy 
     * @param ohodnoceni ohodnoceni orientovane hrany mezi tímto vrcholem a vrcholem v parametru cilovy
     */
    public void addHrana(Vrchol cilovy, int ohodnoceni) {
        sousedniVrcholy.put(cilovy, ohodnoceni);
    }
    
    @Override
    public int hashCode() {
        return this.casVeStanici * 1717 + this.cisloStanice * 91;
    }

    //get a set metody
    
    public int getCisloStanice() {
        return cisloStanice;
    }
    
    public int getCasVeStanici() {
        return casVeStanici;
    }

    public Map<Vrchol, Integer> getSousedniVrcholy() {
        return sousedniVrcholy;
    }

    public Integer getVzdalenost() {
        return vzdalenost;
    }

    public void setVzdalenost(Integer vzdalenost) {
        this.vzdalenost = vzdalenost;
    }

    public List<Vrchol> getNejkratsiCesta() {
        return nejkratsiCesta;
    }

    public void setNejkratsiCesta(LinkedList<Vrchol> novaNejkratsi) {
        this.nejkratsiCesta = novaNejkratsi;
    }
    
    /** 
     * Vrchol je shodný, pokud má stejne cislo i cas stanice - generovana metoda
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vrchol other = (Vrchol) obj;
        if (this.cisloStanice != other.cisloStanice) {
            return false;
        }
        if (this.casVeStanici != other.casVeStanici) {
            return false;
        }
        return true;
    }

    /** Format pro vypis, ze zadani (X Y)
     * 
     * @return text
     */
    @Override
    public String toString() {
        return "("+ cisloStanice + " " + casVeStanici+")";
    }
    
    
}