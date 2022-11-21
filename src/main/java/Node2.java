import java.util.ArrayList;

public class Node2 {

    public Node2(/*Node2 parent,*/ int number) {
       // this.parent = parent;
        this.number = number;
    }

    public Node2(){
        visited.add(0);
    }

   // public Node2 parent;
    public int number;

    public ArrayList<Integer> visited = new ArrayList<>();

    public void setVisited(ArrayList<Integer> array){
        this.visited = new ArrayList<>(array);
    }

}
