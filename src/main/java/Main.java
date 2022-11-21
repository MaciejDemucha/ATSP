import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        //menu();
        test();
        //measurements();
    }

    static void test(){

        String fileName = "tsp_10b.txt";
       TSP tsp = TSP.readFromFileScanner(fileName);
       //tsp.bruteForce(true);
        assert tsp != null;
        tsp.doBFS();

    }

    static void measurements(){
        TSP tsp;
        long start = System.nanoTime();

        for(int i = 0; i < 100; i++){
            tsp = TSP.createRandomMatrix(10);
            tsp.bnBSolution(false);
        }

        long finish = System.nanoTime();

        long timeElapsed = finish - start;
        System.out.println("Time: " + (timeElapsed / 1000000.0)/100.0 + " ms");
    }

    static void menu() {
        TSP tsp = null;
        Scanner scannerChoice = new Scanner(System.in);
        Scanner scannerFile = new Scanner(System.in);
        boolean shouldContinue = true;
        int userChoice = 0;
        String fileName = "test5.txt";
        tsp = TSP.readFromFileScanner(fileName);
        while(shouldContinue){
            System.out.println("\nZaładowany plik: " + fileName + "\n");
            System.out.println("Wybierz opcję: ");
            System.out.println("1.Wczytaj plik ");
            System.out.println("2.Wyświetl macierz odległości ");
            System.out.println("3.Brute Force ");
            System.out.println("4.Branch and Bound ");
            System.out.println("5.Wyjdź \n");

            try{
                userChoice = scannerChoice.nextInt();

                switch (userChoice){
                    case 1:
                        System.out.println("Podaj nazwę pliku z rozszerzeniem: ");
                        fileName = scannerFile.nextLine();
                        tsp = TSP.readFromFileScanner(fileName);
                    break;
                    case 2:
                        if(tsp != null)
                            tsp.printDistances();
                        else
                            System.out.println("Nie załadowano pliku");
                        break;
                    case 3:
                        if(tsp != null)
                            tsp.bruteForce(true);
                        else
                            System.out.println("Nie załadowano pliku");
                        break;
                    case 4 :
                        if(tsp != null)
                            tsp.bnBSolution(true);
                        else
                            System.out.println("Nie załadowano pliku");
                        break;
                    case 5 :shouldContinue = false;
                        break;
                    default : System.out.println(" ");
                }

            }
            catch (InputMismatchException e){
                System.out.println(" ");
                scannerChoice.nextLine();
            }
        }
    }
}
