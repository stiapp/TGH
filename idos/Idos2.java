package martin.TGH.idos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class Idos2 {

    private static Scanner sc = new Scanner(System.in);

    private static int[][] poleDotazu;

    private static int pocetStanic;

    private static int pocetSpoju;

    private static int[][] vstupniPole;

    private static ArrayList<Vrchol> vrcholy = new ArrayList<>();

    private static Set<Vrchol> stanice = new HashSet<Vrchol>();
    

    public static void main(String[] args) {

        pocetStanic = sc.nextInt();  //prvni dve cisla ze vstupu
        pocetSpoju = sc.nextInt();

        //zpracovani zadani do grafu - pristich mnoho radku
        //vstupni zadani vsech spoju
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

        /*//Pro ladeni a krokovani mam vsecky vrcholy na jednom miste
        //instance grafu 
        Graf graf = new Graf();
        //naplneni grafu uzly
        for (Vrchol node : vrcholy) {
            graf.addVrchol(node);
        }
         */
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

            // Vtvoreni vrcholu s nazvem a casem a pridani hrany ze zadani
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

    private static void vyhodnocovaniDotazu() {
        //vyhodnoceni dotazu na spoje
        Vrchol zdrojovyVrchol = null;
        
        //zpracovani vsech dotazu na spoje
        for (int i = 0; i < poleDotazu.length; i++) {
            //dotaz - zpracovani vstupu
            int odkudJedu = poleDotazu[i][0];
            int kamJedu = poleDotazu[i][1];
            int odkdyHledat = poleDotazu[i][2];

            /* nalezeni uzlu s nejblizsim casem - 
            existuji pouze uzly - stanice v case pritomnem a budoucim
            node.getCas_ve_stanici() >= kolik_je_cas
             */
            //nalezeni zdrojoveho vrcholu - aby nebyl cas v minulosti

            zdrojovyVrchol = null;
            for (Vrchol node : vrcholy) {
                if (node.getCisloStanice() == odkudJedu && node.getCasVeStanici() >= odkdyHledat) {
                    zdrojovyVrchol = node;
                    break; //potreba zastavit cyklus, jinak bude zdrojovym uzlem vzdy ten nejposlednejsi cas
                }
            }

            if (zdrojovyVrchol != null) {
                //nastaveni vsech vzdalenosti na nekonecno - pri vice pruchodech by mohly vadit minule hodnoty vzdalenosti
                for (Vrchol node : vrcholy) {
                    //velmi dulezite
                    node.setVzdalenost(Integer.MAX_VALUE);
                    node.setNejkratsiCesta(new LinkedList<>());
                }
                //spusteni dijkstra
                DijkstruvAlg.najdiNejkratsiCestuZeZdrojovehoVrcholu(zdrojovyVrchol);
            } else {
                //zadny spoj nejede z teto stanice v tuto dobu
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
                //nove minimum
                minimum_ze_vzdalenosti = node.getVzdalenost();
                cilovy_uzel = node;
             }
        }

        //kazdy cilovy uzel ma pole uzlu k nemu vedouci ze zdrojoveho uzlu
        if (zdrojovyVrchol == null || cilovy_uzel == null || cilovy_uzel.getVzdalenost() == Integer.MAX_VALUE) {
            //Vypíše se prázdná zavorka pokud neexistuje spojeni
            System.out.println("()");
        } else {

            //zpracovani cesty v cilovem uzlu
            //STale upravy pro vystup
            //hledam nejvyssi cas odjezdu ze stanice
            /* v poli cesty jsou zaneseny i cesty do stejneho uzlu v jinem case
            a ja je chci z vysledne cesty vypustit
             */
                ArrayList<Vrchol> vysledna_cesta = new ArrayList<Vrchol>();
                if(cilovy_uzel.getNejkratsiCesta().size() > 0){
                    vysledna_cesta.add(cilovy_uzel.getNejkratsiCesta().get(0));
                }
                for(int x = 1; x < cilovy_uzel.getNejkratsiCesta().size(); x++) //pres vsecky cisla stanic vyselektuje jen ty s nejvyssim casem pro vypis
                {
                    if(vysledna_cesta.get(vysledna_cesta.size()-1).getCisloStanice() == cilovy_uzel.getNejkratsiCesta().get(x).getCisloStanice())
                    {
                        vysledna_cesta.remove(vysledna_cesta.size()-1);
                        vysledna_cesta.add(cilovy_uzel.getNejkratsiCesta().get(x));
                    }
                    else
                    {
                        vysledna_cesta.add(cilovy_uzel.getNejkratsiCesta().get(x));
                    }
                }
                //ze zadani chceme vypsat i cas v cilovem vrcholu
                vysledna_cesta.add(cilovy_uzel);

            //finalni vypis
            for (Vrchol node : vysledna_cesta) {
                System.out.print(node + " ");
            }
            System.out.println("");
        }
    }

    
}
