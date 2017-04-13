/*
    Konvence je - vsecky komentare, nazvy metod a promennych v CESKEM jazyce.
    Metody i promenne se neboj pojmenovat takto:
    MetodaCoDelaKouzelneHodnoty(int id_hladoveho_zvirete_v_zoo)
    Díky :)
 */
package TGH.TGH;

/** třída reprezentuje uzel grafu
 *
 * @author King
 */
public class Uzel {
    
    //dvojice identifikujici jednoznacne dany uzel
    protected int cislo_stanice;
    protected int cas;
    
    public Uzel(int cislo_stanice, int cas_stanice)
    {
        boolean chyba = false;
        chyba = (cislo_stanice < 0 || cas_stanice < 0 || cas_stanice >= 1440);
        //1440 pocet minut od zacatku dne - ze zadani
        if(chyba)
            throw new IllegalArgumentException("");
        
        this.cas = cas_stanice;
        this.cislo_stanice = cislo_stanice;
    }

    @Override
    public String toString() {
        return "(" + cislo_stanice + ", cas=" + cas + ')';
    }
    
    
    
}
