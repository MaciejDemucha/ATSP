
public class Main {


    public static void main(String[] args) throws Exception {

        TSP tsp = TSP.readFromFile("test.txt");
        tsp.printDistances();

        long start = System.nanoTime();
        tsp.printSolution();
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("Time: " + timeElapsed / 1000000.0 + " ms");


    }

}
