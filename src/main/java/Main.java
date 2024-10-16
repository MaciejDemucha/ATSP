import Genetic.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //menu();
        measurementsQualityGA(Paths.get("output", "outputGA.txt").toString());
       // measurementsQualityGreedy(Paths.get("output", "outputGreedy.txt").toString());
       // measurementsQualityRandom(Paths.get("output", "outputRandom.txt").toString());
    }

static void measurementsQualitySA(){
    TSP tsp;
    ResultSA result;
    tsp = TSP.readFromFileScanner("tsp_170.txt");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),1000, 0.001, 0.95, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),1000, 0.001, 0.99, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),1000, 0.001, 0.999, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),2000, 0.001, 0.95, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),2000, 0.001, 0.99, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),2000, 0.001, 0.999, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),5000, 0.001, 0.95, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),5000, 0.001, 0.99, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),5000, 0.001, 0.999, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),10000, 0.001, 0.95, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),10000, 0.001, 0.99, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
    result = tsp.simulatedAnnealing(tsp.getRandomSolution(),10000, 0.001, 0.999, 1);
    System.out.println("Final solution distance: " + result.getCost());
    System.out.println("Tour: " + result.getPath());
    System.out.println("*********************************************************************************");
}

    static void measurementsQualityGreedy(String fileName) throws IOException {
        TSP tsp;
        tsp = TSP.readFromFileScanner(Paths.get("newTSP", "berlin52.txt").toString());
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        int startingCity = 0;
        while (true) {
            SalesmanGenome solution = tsp.initialSolutionSA(startingCity, InitialSolution.GREEDY);
            System.out.println(solution);
            System.out.println("*********************************************************************************");
            writer.write(solution.getFitness() + "\n");

            startingCity++;
            if(startingCity >= solution.numberOfCities)
                break;
        }

        writer.close();

    }

    static void measurementsQualityRandom(String fileName) throws IOException {
        TSP tsp;
        tsp = TSP.readFromFileScanner(Paths.get("oldTSP", "tsp_17.txt").toString());
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        int startingCity = 0;
        for (int i = 0; i < 10000; i++) {
            SalesmanGenome solution = tsp.initialSolutionSA(startingCity, InitialSolution.RANDOM);
            //System.out.println(solution);
            //System.out.println("*********************************************************************************");
            writer.write(solution.getFitness() + "\n");
        }

        writer.close();

    }

    static void measurementsQualityGA(String fileName) throws IOException {
        TSP tsp;
        tsp = TSP.readFromFileScanner(Paths.get("newTSP", "berlin52.txt").toString());
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        SelectionType selectionType = SelectionType.TOURNAMENT;
        int startingCity = 0;
        int generationSize = 1000;
        int maxIterations = 1000;
        float mutationRate = 0.1F;
        float crossoverRate = 0.7F;
        int tournamentSize = 5;
        MutationType mutationType = MutationType.INVERSE;
        CrossoverType crossoverType = CrossoverType.OX;
        InitialSolution initialSolution = InitialSolution.RANDOM;
        for (int i = 0; i < 2; i++) {
            SalesmanGenome solution = tsp.geneticAlgoritmSolution(selectionType, startingCity, generationSize,
                    maxIterations, mutationRate, crossoverRate, tournamentSize, mutationType, crossoverType, initialSolution);
            System.out.println(solution);
            System.out.println("*********************************************************************************");
            writer.write(solution.getFitness() + "\n");
        }

        writer.close();

    }

    static void measurementsTime(int size){
        TSP tsp;
        System.out.println("Size: " + size);
        long start = System.nanoTime();

        for(int i = 0; i < 100; i++){
            tsp = TSP.createRandomMatrix(size);
            tsp.performSAMeasurements(40000, 1, 0.001, 2);
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
            System.out.println("3.Przegląd zupełny ");
            System.out.println("4.Metoda podziału i ograniczeń ");
            System.out.println("5.Symulowane wyżarzanie ");
            System.out.println("6.Algorytm genetyczny \n");

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
                            tsp.performBnB(true);
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
                            System.out.println("Podaj temperaturę końcową: ");
                            double endTemp = Double.parseDouble(scanner.nextLine());
                            System.out.println("Podaj współczynnik redukcji temperatury: ");
                            double reductionRate = Double.parseDouble(scanner.nextLine());
                            tsp.performSA(startTemp, endTemp, reductionRate, method);
                        }

                        else
                            System.out.println("Nie załadowano pliku");
                        break;
                    case 6 :
                        if(tsp != null){
                            SelectionType selectionType;
                            MutationType mutationType;
                            CrossoverType crossoverType;
                            InitialSolution initialSolution;
                            int startingCity = 0;

                            System.out.println("Wybierz metodę selekcji osobników do krzyżowania: ");
                            System.out.println("losowo: 1");
                            System.out.println("Tournament selection: 2");
                            int choice = Integer.parseInt(scanner.nextLine());
                            if(choice == 1)  selectionType = SelectionType.ROULETTE;
                            else selectionType = SelectionType.TOURNAMENT;

                            System.out.println("Wybierz metodę mutacji: ");
                            System.out.println("Zamiana 2 wartości: 1");
                            System.out.println("Inversion mutation: 2");
                            System.out.println("Scramble mutation: 3");
                            choice = Integer.parseInt(scanner.nextLine());
                            if(choice == 1)  mutationType = MutationType.SWAP;
                            else if(choice == 2) mutationType = MutationType.INVERSE;
                            else mutationType = MutationType.SCRAMBLE;

                            System.out.println("Wybierz metodę krzyżowania: ");
                            System.out.println("CX: 1");
                            System.out.println("OX: 2");
                            System.out.println("One-point: 3");
                            choice = Integer.parseInt(scanner.nextLine());
                            if(choice == 1)  crossoverType = CrossoverType.CX;
                            else if(choice == 2)  crossoverType = CrossoverType.OX;
                            else crossoverType = CrossoverType.ONEPOINT;

                            System.out.println("Wybierz metodę wyznaczania pierwszego rozwiązania: ");
                            System.out.println("losowa: 1");
                            System.out.println("zachłanna: 2");
                            choice = Integer.parseInt(scanner.nextLine());
                            if(choice == 1)  initialSolution = InitialSolution.RANDOM;
                            else initialSolution = InitialSolution.GREEDY;

                            System.out.println("Podaj wielkość początkowej generacji: ");
                            int generationSize = Integer.parseInt(scanner.nextLine());
                            System.out.println("Podaj maksymalną liczbę iteracji: ");
                            int maxIterations = Integer.parseInt(scanner.nextLine());
                            System.out.println("Podaj współczynnik mutacji[0.0 - 1.0]: ");
                            float mutationRate = Float.parseFloat(scanner.nextLine());
                            System.out.println("Podaj współczynnik krzyżowania [0.0 - 1.0]: ");
                            float crossoverRate = Float.parseFloat(scanner.nextLine());
                            System.out.println("Podaj rozmiar turnieju: ");
                            int tournamentSize = Integer.parseInt(scanner.nextLine());

                            System.out.println(tsp.geneticAlgoritmSolution(selectionType, startingCity, generationSize,
                                    maxIterations, mutationRate, crossoverRate, tournamentSize, mutationType, crossoverType, initialSolution));
                        }

                        else
                            System.out.println("Nie załadowano pliku");
                        break;
                    case 0 :shouldContinue = false;
                        break;
                    default : System.out.println(" ");
                }

            }
            catch (InputMismatchException | NumberFormatException ignored){

            }
        }
    }
}
