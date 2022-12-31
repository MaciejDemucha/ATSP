import java.util.ArrayList;

public class ResultSA {

    private int cost = 9999;
    private ArrayList<Integer> path;

    public ResultSA(ResultSA currentResult) {
        this.cost = currentResult.cost;
        this.path = new ArrayList<>(currentResult.path);
    }
    public ResultSA(){

    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }
}
