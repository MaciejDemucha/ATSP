import java.util.ArrayList;

public class ResultSA {

    public int cost = 9999;
    public ArrayList<Integer> path;

    public ResultSA(ResultSA currentResult) {
        this.cost = currentResult.cost;
        this.path = new ArrayList<>(currentResult.path);
    }
    public ResultSA(){

    }
}
