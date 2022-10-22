
public class Main {


    public static void main(String[] args) throws Exception {

        TSP tsp = TSP.readFromFile("test.txt");
        tsp.printDistances();

    }

}
