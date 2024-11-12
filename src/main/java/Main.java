import Genetic.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //menu();
        String input = Paths.get("newTSP", "berlin52.txt").toString();
        String outputFolder = "outputInverse";

        CrossoverType ct = CrossoverType.OX;
        MutationType mt = MutationType.INVERSE;
        SelectionType st = SelectionType.TOURNAMENT;
        InitialSolution initialSolution = InitialSolution.RANDOM;

        int durationInSeconds = 30;

        int pop_size = 2000;
        int gen = 1500;
        measurementsQualityGA(input, Paths.get(outputFolder, "outputGA11.txt").toString(), pop_size, gen,durationInSeconds, 0.1F, 0.7F, 2, st, mt, ct, initialSolution);
        st = SelectionType.ROULETTE;
        measurementsQualityGA(input, Paths.get(outputFolder, "outputGA11.txt").toString(), pop_size, gen,durationInSeconds, 0.1F, 0.7F, 2, st, mt, ct, initialSolution);
        //measurementsQualityGA(input, Paths.get(outputFolder, "outputGA12.txt").toString(), pop_size, gen,durationInSeconds, 0.2F, 0.7F, 2, st, mt, ct, initialSolution);
        //measurementsQualityGA(input, Paths.get(outputFolder, "outputGA13.txt").toString(), pop_size, gen,durationInSeconds, 0.3F, 0.7F, 2, st, mt, ct, initialSolution);
        //measurementsQualityGA(input, Paths.get(outputFolder, "outputGA14.txt").toString(), pop_size, gen, durationInSeconds, 0.1F, 0.8F, 2, st, mt, ct, initialSolution);
        //measurementsQualityGA(input, Paths.get(outputFolder, "outputGA15.txt").toString(), pop_size, gen,durationInSeconds, 0.1F, 0.9F, 2, st, mt, ct, initialSolution);

       /* input = Paths.get("newTSP", "berlin52.txt").toString();
        measurementsQualityGA(input, Paths.get(outputFolder, "outputGA11.txt").toString(), pop_size, gen,durationInSeconds, 0.1F, 0.7F, 2, st, mt, ct, initialSolution);
        input = Paths.get("newTSP", "ali535.txt").toString();
        measurementsQualityGA(input, Paths.get(outputFolder, "outputGA11.txt").toString(), pop_size, gen,durationInSeconds, 0.1F, 0.7F, 2, st, mt, ct, initialSolution);
        input = Paths.get("newTSP", "kroA100.txt").toString();
        measurementsQualityGA(input, Paths.get(outputFolder, "outputGA12.txt").toString(), pop_size, gen,durationInSeconds, 0.1F, 0.7F, 2, st, mt, ct, initialSolution);
        input = Paths.get("newTSP", "kroA150.txt").toString();
        measurementsQualityGA(input, Paths.get(outputFolder, "outputGA13.txt").toString(), pop_size, gen,durationInSeconds, 0.1F, 0.7F, 2, st, mt, ct, initialSolution);
        input = Paths.get("newTSP", "kroA200.txt").toString();
        measurementsQualityGA(input, Paths.get(outputFolder, "outputGA14.txt").toString(), pop_size, gen,durationInSeconds, 0.1F, 0.7F, 2, st, mt, ct, initialSolution);
        input = Paths.get("newTSP", "fl417.txt").toString();
        measurementsQualityGA(input, Paths.get(outputFolder, "outputGA15.txt").toString(), pop_size, gen,durationInSeconds, 0.1F, 0.7F, 2, st, mt, ct, initialSolution);*/

        outputFolder = "outputSwap";
        mt = MutationType.SWAP;

        pop_size = 2000;
        gen = 500;
        //measurementsQualityGA(input, Paths.get(outputFolder, "outputGA11.txt").toString(), pop_size, gen,durationInSeconds, 0.01F, 0.7F, 2, st, mt, ct, initialSolution);
        //measurementsQualityGA(input, Paths.get(outputFolder, "outputGA12.txt").toString(), pop_size, gen, durationInSeconds,0.02F, 0.7F, 2, st, mt, ct, initialSolution);
        //measurementsQualityGA(input, Paths.get(outputFolder, "outputGA13.txt").toString(), pop_size, gen,durationInSeconds, 0.05F, 0.7F, 2, st, mt, ct, initialSolution);
        //measurementsQualityGA(input, Paths.get(outputFolder, "outputGA14.txt").toString(), pop_size, gen,durationInSeconds, 0.01F, 0.8F, 2, st, mt, ct, initialSolution);
        //measurementsQualityGA(input, Paths.get(outputFolder, "outputGA15.txt").toString(), pop_size, gen,durationInSeconds, 0.01F, 0.9F, 2, st, mt, ct, initialSolution);


    }

    static void measurementsQualityGreedy(String input, String fileName) throws IOException {
        TSP tsp;
        tsp = TSP.readFromFileScanner(input);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        int startingCity = 0;
        while (true) {
            SalesmanGenome solution = tsp.initialSolutionSA(startingCity, InitialSolution.GREEDY);
            //System.out.println(solution);
            //System.out.println("*********************************************************************************");
            writer.write(solution.getFitness() + "\n");

            startingCity++;
            if(startingCity >= solution.numberOfCities)
                break;
        }

        writer.close();

    }

    static void measurementsQualityRandom(String input, String fileName) throws IOException {
        TSP tsp;
        tsp = TSP.readFromFileScanner(input);
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

    static void measurementsQualityGA(String input, String fileName, int generationSize, int maxIterations, int durationInSeconds,
                                      float mutationRate, float crossoverRate, int tournamentSize,
                                      SelectionType selectionType, MutationType mutationType, CrossoverType crossoverType,
                                      InitialSolution initialSolution) throws IOException {
        TSP tsp;
        tsp = TSP.readFromFileScanner(input);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        int startingCity = 0;
        for (int i = 0; i < 10; i++) {
            SalesmanGenome solution = tsp.geneticAlgoritmSolution(selectionType, startingCity, generationSize,
                    maxIterations, durationInSeconds, mutationRate, crossoverRate, tournamentSize, mutationType, crossoverType, initialSolution);
            //System.out.println(solution);
            //System.out.println("*********************************************************************************");
            writer.write(solution.getFitness() + "\n");
        }

        writer.close();

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
                            choice = Integer.parseInt(scanner.nextLine());
                            if(choice == 1)  mutationType = MutationType.SWAP;
                            else mutationType = MutationType.INVERSE;

                            System.out.println("Wybierz metodę krzyżowania: ");
                            System.out.println("CX: 1");
                            System.out.println("OX: 2");
                            System.out.println("One-point: 3");
                            choice = Integer.parseInt(scanner.nextLine());
                            if(choice == 1)  crossoverType = CrossoverType.CX;
                            else crossoverType = CrossoverType.OX;

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
                                    maxIterations, 20, mutationRate, crossoverRate, tournamentSize, mutationType, crossoverType, initialSolution));
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
