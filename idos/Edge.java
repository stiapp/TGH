package idos;


public class Edge {
    
     // Vrchol do ktereho smeruje hrana
    private Vertex to;
    
    // Casova vzdalenost do vrcholu 'to'
    private int distance;

    public Edge(Vertex to, int distance) {
        this.to = to;
        this.distance = distance;
    }

    public Vertex getTo() {
        return to;
    }

    public void setTo(Vertex to) {
        this.to = to;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Edge{" + "to=" + to.getIdentification() + ", distance=" + distance + '}';
    }
    
    
    
   
}
