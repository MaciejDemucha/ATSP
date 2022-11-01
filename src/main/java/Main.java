import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        TSP tsp = TSP.readFromFileScanner("test5.txt");
        //tsp.printDistances();
        tsp.expandNodes();
        //System.out.println(Arrays.toString(tsp.bounds));
        tsp.bruteForce(true);

        long start = System.nanoTime();

        for(int i = 0; i < 100; i++){
            tsp.bruteForce(false);
        }

        long finish = System.nanoTime();

        long timeElapsed = finish - start;
        System.out.println("Time: " + (timeElapsed / 1000000.0)/100.0 + " ms");
    }
}
