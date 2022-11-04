import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        menu();
    }

    static void measurements(){
        /*long start = System.nanoTime();

        for(int i = 0; i < 100; i++){
            tsp.bruteForce(false);
        }

        long finish = System.nanoTime();

        long timeElapsed = finish - start;
        System.out.println("Time: " + (timeElapsed / 1000000.0)/100.0 + " ms");*/
    }

    static void menu() throws FileNotFoundException {
        TSP tsp = null;
        Scanner scanner = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);
        boolean shouldContinue = true;
        int userChoice;
        String fileName = "";
        while(shouldContinue){
            System.out.println("Załadowany plik: " + fileName + "\n");
            System.out.println("Wybierz opcję: ");
            System.out.println("1.Wczytaj plik ");
            System.out.println("2.Wyświetl macierz odległości ");
            System.out.println("3.Brute Force ");
            System.out.println("4.Branch and Bound ");
            System.out.println("5.Wyjdź ");

            userChoice = scanner.nextInt();

            switch (userChoice){
                case 1 -> {
                    System.out.println("Podaj nazwę pliku z rozszerzeniem: ");
                    fileName = scanner2.nextLine();
                    tsp = TSP.readFromFileScanner(fileName);
                }
                case 2 -> {
                    if(tsp != null)
                        tsp.printDistances();
                    else
                        System.out.println("Nie załadowano pliku");
                }
                case 3 -> {
                    if(tsp != null){
                        tsp.bruteForce(true);
                    }
                    else
                        System.out.println("Nie załadowano pliku");
                }
                case 4 -> {
                    if(tsp != null)
                        tsp.bnBSolution(true);
                    else
                        System.out.println("Nie załadowano pliku");
                }
                case 5 -> shouldContinue = false;
            }
        }
    }
}
