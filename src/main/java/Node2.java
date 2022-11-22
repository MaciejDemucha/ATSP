import java.util.LinkedList;

public class Node2 {
    public Node2 parent;
    public int number;
    public int level;
    //public LinkedList<Integer> visitedSoFar = new LinkedList<>();
    public int lowerBound;
    int distanceSoFar;
    boolean explored = false;

    public Node2(){
       // visitedSoFar.add(0);
    }
   // public void setVisitedSoFar(LinkedList<Integer> array){
       // this.visitedSoFar = new LinkedList<>(array);
        //this.visitedSoFar = array;
    //}

}
