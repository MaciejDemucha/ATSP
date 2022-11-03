import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        TSP tsp = TSP.readFromFileScanner("test5.txt");
        //tsp.printDistances();
        tsp.bruteForce(true);
        tsp.expandNodes(0);
        System.out.println();
        tsp.expandNodes(3);
        System.out.println();
        tsp.expandNodes(4);
        System.out.println();
        tsp.expandNodes(2);
        System.out.println("\n" + tsp.getDistance(0, 3) + " " + tsp.getDistance(3, 4) + " "+tsp.getDistance(4, 2) + " "+tsp.getDistance(2, 1) + " "+ tsp.getDistance(1,0));
        System.out.println("Distance: " + tsp.finalPathDistance);

        /*long start = System.nanoTime();

        for(int i = 0; i < 100; i++){
            tsp.bruteForce(false);
        }

        long finish = System.nanoTime();

        long timeElapsed = finish - start;
        System.out.println("Time: " + (timeElapsed / 1000000.0)/100.0 + " ms");*/
    }
}
