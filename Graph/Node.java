public class Node {
    private Node[] neighbors;
    private boolean visited;
    private int value;

    public Node(Node[] neighbors, boolean visited, int value){
        this.neighbors = neighbors;
        this.visited = visited;
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public boolean isVisited(){
        return this.visited;
    }

    public void setVisited(){
        this.visited = true;
    }

    public void unsetVisited(){
        this.visited = false;
    }

    public Node[] getNeighbors(){
        return this.neighbors;
    }

    public void setNeighbors(Node[] neighbors){
        this.neighbors = neighbors;
    }
}
