
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class idos {

    private static Scanner sc = new Scanner(System.in);

    private static int[][] poleDotazu;

    private static int pocetStanic;

    private static int pocetSpoju;

    private static int[][] vstupniPole;

    private static ArrayList<Vrchol> vrcholy = new ArrayList<>();

    private static Set<Vrchol> stanice = new HashSet<Vrchol>();

    public static void main(String[] args) {

        pocetStanic = sc.nextInt();
        pocetSpoju = sc.nextInt();

        // Vstupni zadani vsech spoju
        vstupniPole = new int[pocetSpoju][4]; //4 cisla na radek pri zadavani spoju

        for (int i = 0; i < pocetSpoju; i++) //i-ty radek
        {
            for (int j = 0; j < 4; j++) //j-ty sloupec
            {
                vstupniPole[i][j] = sc.nextInt();
            }
        }

        dotazyNaSpoje();

        // Mnozina vsech vrcholu grafu
        // Mnozina proto, aby nevznikaly duplicitni vrcholy - stanice ve stejnem case
        vytvoreniVrcholu();

        setrideniVrcholu();

        pridaniHran();

        vyhodnocovaniDotazu();

    }

    /**
     * Zpracovava dotazy na spoje
     */
    private static void dotazyNaSpoje() {
        // Vstupni zadani vsech dotazu na spoje
        int pocetDotazu = sc.nextInt();
        poleDotazu = new int[pocetDotazu][3]; //odkud, kam, odkdy

        // Naplneni pole dotazu
        for (int i = 0; i < pocetDotazu; i++) {
            for (int j = 0; j < 3; j++) {
                poleDotazu[i][j] = sc.nextInt();
            }
        }

    }

    /**
     * Vytvari vrcholy ze ziskanych dat ze vstupu
     */
    private static void vytvoreniVrcholu() {
        // Vzniknou stanice v case odjezdu z dane stanice
        for (int i = 0; i < pocetSpoju; i++) {

            // Vytvoreni vrcholu s nazvem a casem a pridani hrany ze zadani
            Vrchol vrchol = new Vrchol(vstupniPole[i][0], vstupniPole[i][2]);
            stanice.add(vrchol);
        }

        // Vrcholy ze zadani - vzniknou stanice v case prijezdu do dane stanice
        for (int i = 0; i < pocetSpoju; i++) {

            // Prijezd do stanice vznikne z nazvu stanice a jeji cas - odjezd z predchozi + delka cesty
            stanice.add(new Vrchol(vstupniPole[i][1], vstupniPole[i][2] + vstupniPole[i][3]));
        }

        // Setrideni stanic podle nazvu i casu - unikatnost zaridila mnozina 'stanice'
        // např. 0,10  0,70  0,130  1,40  1,50  1,60
        Object[] vrcholy_pole = stanice.toArray();
        int pocet_vrcholu = vrcholy_pole.length;
        //ArrayList<Vrchol> vrcholy = new ArrayList<Vrchol>();

        // Vlozeni do listu pro dalsi pouziti
        for (int i = 0; i < pocet_vrcholu; i++) {
            vrcholy.add((Vrchol) vrcholy_pole[i]);
        }
    }

    /**
     * Setrideni vrcholu podle cisla stanice a casu odjezdu nebo prijezdu
     */
    private static void setrideniVrcholu() {

        // Vlastni komparator - anonymni trida
        Collections.sort(vrcholy, new Comparator<Vrchol>() {

            public int compare(Vrchol o1, Vrchol o2) {
                // Pokud je sejne cislo stanice - tridime podle casu stanice
                if (o1.getCisloStanice() == o2.getCisloStanice()) {
                    return Integer.compare(o1.getCasVeStanici(), o2.getCasVeStanici());
                }

                return Integer.compare(o1.getCisloStanice(), o2.getCisloStanice());
            }
        }
        );
    }

    /**
     * Pridava k vrcholum jejich hrany
     */
    private static void pridaniHran() {

        // K vrcholum pridavame hrany - tvori se graf
        for (int i = 0; i < pocetSpoju; i++) {
            int staniceStart = vstupniPole[i][0];
            int staniceStartCas = vstupniPole[i][2];
            int staniceCilova = vstupniPole[i][1];
            int staniceCilovaCas = vstupniPole[i][2] + vstupniPole[i][3];

            // Hledaji se dva vrcholy, ktere splnuji nasledujici podminky 
            Vrchol startovni = null;
            Vrchol cilovy = null;
            for (Vrchol node : vrcholy) {
                if (node.getCisloStanice() == staniceStart && node.getCasVeStanici() == staniceStartCas) {
                    startovni = node;
                    break;
                }
            }
            for (Vrchol node : vrcholy) {
                if (node.getCisloStanice() == staniceCilova && node.getCasVeStanici() == staniceCilovaCas) {
                    cilovy = node;
                    break;
                }
            }
            if (startovni == null || cilovy == null) {
                throw new NullPointerException("došlo k chybě ve vyhledávání vrcholu pro vytvoreni hrany");
            }
            // Najite vrcholy se spoji hranou
            startovni.addHrana(cilovy, vstupniPole[i][3]);
        }

        // Vytvoreni hran cekani, to jsou hrany mezi vrcholy se stejnym cislem stanice
        for (int i = 0; i < pocetStanic; i++) {
            for (int index = 0; index < vrcholy.size() - 1; index++) {
                if (vrcholy.get(index).getCisloStanice() == i && vrcholy.get(index + 1).getCisloStanice() == i) {
                    int doba_cekani = vrcholy.get(index + 1).getCasVeStanici() - vrcholy.get(index).getCasVeStanici();
                    vrcholy.get(index).addHrana(vrcholy.get(index + 1), doba_cekani);
                }
            }
        }

    }

    /**
     * Vyhodoceni zadanych dotazu
     */
    private static void vyhodnocovaniDotazu() {
        // Vyhodnoceni dotazu na spoje
        Vrchol zdrojovyVrchol = null;

        // Zpracovani vsech dotazu na spoje
        for (int i = 0; i < poleDotazu.length; i++) {
            // Dotaz - zpracovani vstupu
            int odkudJedu = poleDotazu[i][0];
            int kamJedu = poleDotazu[i][1];
            int odkdyHledat = poleDotazu[i][2];

            
            // Nalezeni zdrojoveho vrcholu - aby nebyl cas v minulosti
            zdrojovyVrchol = null;
            for (Vrchol node : vrcholy) {
                if (node.getCisloStanice() == odkudJedu && node.getCasVeStanici() >= odkdyHledat) {
                    zdrojovyVrchol = node;
                    break; // Potreba zastavit cyklus, jinak bude zdrojovym uzlem vzdy ten nejposlednejsi cas
                }
            }

            if (zdrojovyVrchol != null) {
                // Nastaveni vsech vzdalenosti na nekonecno - pri vice pruchodech by mohly vadit minule hodnoty vzdalenosti
                for (Vrchol node : vrcholy) {
                    // Znovu inicializovani hodnot
                    node.setVzdalenost(Integer.MAX_VALUE);
                    node.setNejkratsiCesta(new LinkedList<>());
                }
                // Dpusteni dijkstrova algoritmu
                DijkstruvAlg.najdiNejkratsiCestuZeZdrojovehoVrcholu(zdrojovyVrchol);
            } else {
                // Zadny spoj nenalezen
                zdrojovyVrchol = null;
            }

            vypisSpojeni(kamJedu, zdrojovyVrchol);
        }
    }

    private static void vypisSpojeni(int kamJedu, Vrchol zdrojovyVrchol) {
        // Upravy pro vystup 
        // Nalezeni cilove stanice s nejnizsi vzdalenosti od zdrojoveho vrcholu
        int minimum_ze_vzdalenosti = Integer.MAX_VALUE;
        Vrchol cilovy_uzel = null;
        for (Vrchol node : vrcholy) {
            if (node.getCisloStanice() == kamJedu && node.getVzdalenost() < minimum_ze_vzdalenosti) {
                // Nove minimum
                minimum_ze_vzdalenosti = node.getVzdalenost();
                cilovy_uzel = node;
            }
        }

        // Kazdy cilovy uzel ma pole uzlu k nemu vedouci ze zdrojoveho uzlu
        if (zdrojovyVrchol == null || cilovy_uzel == null || cilovy_uzel.getVzdalenost() == Integer.MAX_VALUE) {
            // Vypíše se prázdná zavorka pokud neexistuje spojeni
            System.out.println("()");
        } else {

            // Zpracovani cesty v cilovem uzlu
            // Hleda se nejvyssi cas odjezdu ze stanice
            // V poli cesty jsou zaneseny i cesty do stejneho uzlu v jinem case
            // ty se z vysledne cesty vypusti
            ArrayList<Vrchol> vysledna_cesta = new ArrayList<Vrchol>();
            if (cilovy_uzel.getNejkratsiCesta().size() > 0) {
                vysledna_cesta.add(cilovy_uzel.getNejkratsiCesta().get(0));
            }
            // Pres vsechny cisla stanic vyselektuje jen ty s nejvyssim casem pro vypis
            for (int x = 1; x < cilovy_uzel.getNejkratsiCesta().size(); x++) 
            {
                if (vysledna_cesta.get(vysledna_cesta.size() - 1).getCisloStanice() == cilovy_uzel.getNejkratsiCesta().get(x).getCisloStanice()) {
                    vysledna_cesta.remove(vysledna_cesta.size() - 1);
                    vysledna_cesta.add(cilovy_uzel.getNejkratsiCesta().get(x));
                } else {
                    vysledna_cesta.add(cilovy_uzel.getNejkratsiCesta().get(x));
                }
            }
            // Ze zadani chceme vypsat i cas v cilovem vrcholu
            vysledna_cesta.add(cilovy_uzel);

            // Finalni vypis
            for (Vrchol node : vysledna_cesta) {
                System.out.print(node + " ");
            }
            System.out.println("");
        }
    }

}

class Vrchol {

    private int cisloStanice;
    private int casVeStanici;

    // Po pruchodu Dijkstra algoritmu ze zadaneho vrcholu je v
    // Seznamu nejkratsiCesta nejkratsi cesta od zadaneho vrcholu k tomuto
    private LinkedList<Vrchol> nejkratsiCesta = new LinkedList<>();

    // Pouzivame objektovy integer kvuli mape sousednichVrch
    private Integer vzdalenost = Integer.MAX_VALUE;

    // Vrchol a jak je daleko od instance tohoto vrcholu
    private Map<Vrchol, Integer> sousedniVrcholy = new HashMap<>();

    /**
     * Konstruktor
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
     * @param ohodnoceni ohodnoceni orientovane hrany mezi tímto vrcholem a
     * vrcholem v parametru cilovy
     */
    public void addHrana(Vrchol cilovy, int ohodnoceni) {
        sousedniVrcholy.put(cilovy, ohodnoceni);
    }

    @Override
    public int hashCode() {
        return this.casVeStanici * 1717 + this.cisloStanice * 91;
    }

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

    /**
     * Format pro vypis, ze zadani (X Y)
     *
     * @return text
     */
    @Override
    public String toString() {
        return "(" + cisloStanice + " " + casVeStanici + ")";
    }

}


class DijkstruvAlg {

    public static void najdiNejkratsiCestuZeZdrojovehoVrcholu(Vrchol zdrojovyVrchol) {

        // Nultý krok dijkstrova algoritmu
        zdrojovyVrchol.setVzdalenost(0);

        Set<Vrchol> vyhodnoceneVrcholy = new HashSet<>();
        Set<Vrchol> nevyhodnoceneVrcholy = new HashSet<>();

        // Vlozi zdrojovy vrchol do vehodnocenych vrcholu 
        nevyhodnoceneVrcholy.add(zdrojovyVrchol);

        // Dokud nebyly vhodnoceny vsechny dosazitelne vrcholy
        // Dokud je kolekce nevyhodnocenych vrcholu neprazdna
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
     * @param vyhodnocovanyVrchol Vrchol sousedici s vrcholem, jemuz nyni
     * dijkstra algoritmus vyhodnocuje vzdalenosti k jeho sousedum
     * @param hodnota_hrany ohodnoceni hrany vedoucí k vrcholu sousednimu
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

    /**
     * Vrati vrchol s nejnizsi vzdalenosti ze vstupni mnoziny vrcholu - s
     * nejnizim atributem vzdalenost
     *
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
