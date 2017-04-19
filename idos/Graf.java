
package martin.TGH.idos;

import java.util.HashSet;
import java.util.Set;

/** Množina vrcholů tvořící graf
 *  pomocný kontejner pro ladění a krokování
 * @author Jan Š.
 */
public class Graf {

    private Set<Vrchol> vrcholy = new HashSet<>();

    public void addVrchol(Vrchol v) {
        vrcholy.add(v);
    }

    public Set<Vrchol> getNodes() {
        return vrcholy;
    }
}
