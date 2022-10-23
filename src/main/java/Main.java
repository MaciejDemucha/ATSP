
public class Main {
    public static void main(String[] args) throws Exception {
        TSP tsp = TSP.readFromFile("test.txt");
        tsp.printDistances();

        long start = System.nanoTime();

        for(int i = 0; i < 100; i++){
            tsp = TSP.readFromFile("test.txt");
            tsp.solution(false);
        }

        long finish = System.nanoTime();

        long timeElapsed = finish - start;
        System.out.println("Time: " + (timeElapsed / 1000000.0)/100.0 + " ms");
    }
}
