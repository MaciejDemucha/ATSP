import java.util.*;

public class Main {
    public static void main(String[] args) {
        menu();
       // measurements(1800);
    }


    static void measurements(int size){
        TSP tsp;
        System.out.println("Size: " + size);
        long start = System.nanoTime();

        for(int i = 0; i < 100; i++){
            tsp = TSP.createRandomMatrix(size);
            tsp.performSAMeasurements(40000, 0.999, 1);
        }

        long finish = System.nanoTime();

        long timeElapsed = finish - start;
        System.out.println("Time: " + (timeElapsed / 1000000.0)/100.0 + " ms");
    }

    static void menu() {
        TSP tsp;
        Scanner scanner = new Scanner(System.in);
        boolean shouldContinue = true;
        int userChoice = 0;
        String fileName = "tsp_10a.txt";
        tsp = TSP.readFromFileScanner(fileName);
        while(shouldContinue){
            System.out.println("\nZaładowany plik: " + fileName + "\n");
            System.out.println("Wybierz opcję: ");
            System.out.println("0.Wyjdź ");
            System.out.println("1.Wczytaj plik ");
            System.out.println("2.Wyświetl macierz odległości ");
            System.out.println("3.Brute Force ");
            System.out.println("4.Branch and Bound ");
            System.out.println("5.SA \n");

            try{
                userChoice = Integer.parseInt(scanner.nextLine());

                switch (userChoice){
                    case 1:
                        System.out.println("Podaj nazwę pliku z rozszerzeniem: ");
                        fileName = scanner.nextLine();
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
                            tsp.doBFS(true);
                        else
                            System.out.println("Nie załadowano pliku");
                        break;
                    case 5 :
                        if(tsp != null){
                            System.out.println("Wybierz metodę redukcji temperatury: ");
                            System.out.println("alpha(T) = a * T (a < 1): 1");
                            System.out.println("alpha(T) = T/(1+b*T) (b << 1): 2");
                            int method = Integer.parseInt(scanner.nextLine());
                            if(method != 1 && method != 2)
                                break;
                            System.out.println("Podaj temperaturę początkową: ");
                            double startTemp = Double.parseDouble(scanner.nextLine());
                            System.out.println("Podaj współczynnik redukcji temperatury: ");
                            double reductionRate = Double.parseDouble(scanner.nextLine());
                            tsp.performSA(startTemp, reductionRate, method);
                        }

                        else
                            System.out.println("Nie załadowano pliku");
                        break;
                    case 0 :shouldContinue = false;
                        break;
                    default : System.out.println(" ");
                }

            }
            catch (InputMismatchException | NumberFormatException e){
                //System.out.println("Naciśnij ENTER ");
                //scanner.nextLine();
            }
        }
    }
}
