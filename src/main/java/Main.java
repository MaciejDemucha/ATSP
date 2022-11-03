import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        TSP tsp = TSP.readFromFileScanner("test5.txt");
        //tsp.printDistances();
        tsp.bruteForce(true);
        tsp.expandNodes(0);
        System.out.println();
        tsp.expandNodes(3);

        /*long start = System.nanoTime();

        for(int i = 0; i < 100; i++){
            tsp.bruteForce(false);
        }

        long finish = System.nanoTime();

        long timeElapsed = finish - start;
        System.out.println("Time: " + (timeElapsed / 1000000.0)/100.0 + " ms");*/
    }
}
