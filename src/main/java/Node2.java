import java.util.ArrayList;

public class Node2 {
    private int lowerBound;
    private int number;
    private ArrayList<Integer> children;

    public Node2(int lowerBound, int number, ArrayList<Integer> children) {
        this.lowerBound = lowerBound;
        this.number = number;
        this.children = children;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<Integer> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Integer> children) {
        this.children = children;
    }
}
