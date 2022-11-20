import java.util.ArrayList;
import java.util.Comparator;

public class Node {
    private int number;
    private int cost;

    private int[][] matrix;
    private ArrayList<Integer> path;

    public boolean expanded = false;

    int level;

    public Node(int number, int cost, int[][] matrix, ArrayList<Integer> path, int level) {
        this.number = number;
        this.cost = cost;
        this.matrix = matrix;
        this.path = new ArrayList<>(path);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public ArrayList<Integer> getPath() {
        return path;
    }

    public void setPath(ArrayList<Integer> path) {
        this.path = new ArrayList<>(path);
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}

