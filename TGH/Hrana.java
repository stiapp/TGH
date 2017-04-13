
package TGH.TGH;

/** Hrana - dva uzly které propojuje
 *  je ORIENTOVANA - vede od prvniho k druhemu
 *  je ohodnocena
 * 
 * @author King
 */
public class Hrana {
   
    protected Uzel prvni_uzel;
    protected Uzel druhy_uzel;
    protected int  ohodnoceni;  //jednotka - minuty [0:1440]

    public Hrana(Uzel prvni_uzel, Uzel druhy_uzel, int ohodnoceni) {
        this.prvni_uzel = prvni_uzel;
        this.druhy_uzel = druhy_uzel;
        this.ohodnoceni = ohodnoceni;
    }

    public Uzel getPrvni_uzel() {
        return prvni_uzel;
    }

    public Uzel getDruhy_uzel() {
        return druhy_uzel;
    }

    public int getOhodnoceni() {
        return ohodnoceni;
    }
    
}

