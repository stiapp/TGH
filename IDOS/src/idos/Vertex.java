package idos;

import java.util.ArrayList;


public class Vertex implements Comparable<Vertex> {

    // Cislo uzlu
    private int identification;

    // Cas odjezdu nebo prijezdu do uzlu
    private int time;

    // Pro startovní uzel true jinak false
    private boolean startingNode = false;

    // List hran vrcholu
    private ArrayList<Edge> edges = new ArrayList<>();

    /**
     * Konstruktor
     *
     * @param identification cislo vrcholu
     * @param time cas odjezdu neo prijezdu
     */
    public Vertex(int identification, int time) {
        this.identification = identification;
        this.time = time;

    }

    /**
     *
     * @return time - cas odjezdu nebo prijezdu
     */
    public int getTime() {
        return time;
    }

    /**
     *
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     *
     * @return cislo vrcholu
     */
    public int getIdentification() {
        return identification;
    }
    
    
    

    /**
     *
     * @param indentification
     */
    public void setIdentification(int indentification) {
        this.identification = indentification;
    }

    /**
     * Pridava hranu do pole hran
     *
     * @param e hrana
     */
    public void addEdge(Edge e) {
        edges.add(e);
    }

    /**
     * Porovnavani vrcholu podle cisla vrcholu a pripadne podle casu vrcholu
     *
     * @param v Vrchol
     * @return
     */
    @Override
    public int compareTo(Vertex v) {

        if (v.identification != this.identification) {
            return Integer.compare(this.identification, v.identification);
        }
        return Integer.compare(this.time, v.time);
    }

    /**
     * Vypis pro testovani
     *
     * @return string
     */
    @Override
    public String toString() {
        return "Vertex{" + "id=" + identification + ", t=" + time + ", edges=" + edges + '}';
    }

    /**
     * Vrcholy jsou stejne, pokud maji stejny identifikator a cas odjezdu
     *
     * @param v vrchol
     * @return boolean true pro stejne vrcholy jinak false
     */
    public boolean equals(Vertex v) {
        if (v.identification == this.identification && v.time == this.time) {
            return true;
        }
        return false;
    }

}
