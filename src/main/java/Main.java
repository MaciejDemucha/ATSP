
public class Main {
    public static void main(String[] args) throws Exception {
        TSP tsp = TSP.readFromFileScanner("test3.txt");
        tsp.printDistances();
        tsp.solution(true);

        long start = System.nanoTime();

        for(int i = 0; i < 100; i++){
            tsp.solution(false);
        }

        long finish = System.nanoTime();

        long timeElapsed = finish - start;
        System.out.println("Time: " + (timeElapsed / 1000000.0)/100.0 + " ms");
    }
}
