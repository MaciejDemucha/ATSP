import Genetic.MutationType;
import Genetic.SalesmanGenome;
import Genetic.SelectionType;
import Genetic.Solution;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        menu();
        //measurements(40);
        //measurementsQualitySA();
       // inversionMutation();
    }



    /*public static void inversionMutation() {
        Random random = new Random();

            List<Integer> genome = new ArrayList<>();
            genome.add(1);
            genome.add(2);
            genome.add(3);
            genome.add(4);
            genome.add(5);
            genome.add(6);
            genome.add(7);
            genome.add(8);
            genome.add(9);


            int start = Solution.randInt(0, genome.size()-2);
            int end = Solution.randInt(start+1, genome.size()-2);
        System.out.println(start + " " + end);

            List reversedPart = Solution.shuffleArray(genome, start, end);

            int indexOfReversedPart = 0;

            for(int i = start; i <= end; i++){
                genome.set(i, (Integer) reversedPart.get(indexOfReversedPart));
                indexOfReversedPart++;
            }
        System.out.println(genome);
    }*/

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
    static void measurements(int size){
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
            System.out.println("\nZa??adowany plik: " + fileName + "\n");
            System.out.println("Wybierz opcj??: ");
            System.out.println("0.Wyjd?? ");
            System.out.println("1.Wczytaj plik ");
            System.out.println("2.Wy??wietl macierz odleg??o??ci ");
            System.out.println("3.Przegl??d zupe??ny ");
            System.out.println("4.Metoda podzia??u i ogranicze?? ");
            System.out.println("5.Symulowane wy??arzanie ");
            System.out.println("6.Algorytm genetyczny \n");

            try{
                userChoice = Integer.parseInt(scanner.nextLine());

                switch (userChoice){
                    case 1:
                        System.out.println("Podaj nazw?? pliku z rozszerzeniem: ");
                        fileName = scanner.nextLine();
                        tsp = TSP.readFromFileScanner(fileName);
                    break;
                    case 2:
                        if(tsp != null)
                            tsp.printDistances();
                        else
                            System.out.println("Nie za??adowano pliku");
                        break;
                    case 3:
                        if(tsp != null)
                            tsp.bruteForce(true);
                        else
                            System.out.println("Nie za??adowano pliku");
                        break;
                    case 4 :
                        if(tsp != null)
                            tsp.performBnB(true);
                        else
                            System.out.println("Nie za??adowano pliku");
                        break;
                    case 5 :
                        if(tsp != null){
                            System.out.println("Wybierz metod?? redukcji temperatury: ");
                            System.out.println("alpha(T) = a * T (a < 1): 1");
                            System.out.println("alpha(T) = T/(1+b*T) (b << 1): 2");
                            int method = Integer.parseInt(scanner.nextLine());
                            if(method != 1 && method != 2)
                                break;
                            System.out.println("Podaj temperatur?? pocz??tkow??: ");
                            double startTemp = Double.parseDouble(scanner.nextLine());
                            System.out.println("Podaj temperatur?? ko??cow??: ");
                            double endTemp = Double.parseDouble(scanner.nextLine());
                            System.out.println("Podaj wsp????czynnik redukcji temperatury: ");
                            double reductionRate = Double.parseDouble(scanner.nextLine());
                            tsp.performSA(startTemp, endTemp, reductionRate, method);
                        }

                        else
                            System.out.println("Nie za??adowano pliku");
                        break;
                    case 6 :
                        if(tsp != null){
                            SelectionType selectionType;
                            MutationType mutationType;
                            int startingCity = 0;

                            System.out.println("Wybierz metod?? selekcji osobnik??w do krzy??owania: ");
                            System.out.println("losowo: 1");
                            System.out.println("Tournament selection: 2");
                            int choice = Integer.parseInt(scanner.nextLine());
                            if(choice == 1)  selectionType = SelectionType.ROULETTE;
                            else selectionType = SelectionType.TOURNAMENT;

                            System.out.println("Wybierz metod?? mutacji: ");
                            System.out.println("Zamiana 2 warto??ci: 1");
                            System.out.println("Inversion mutation: 2");
                            System.out.println("Scramble mutation: 3");
                            choice = Integer.parseInt(scanner.nextLine());
                            if(choice == 1)  mutationType = MutationType.SWAP;
                            else if(choice == 2) mutationType = MutationType.INVERSE;
                            else mutationType = MutationType.SCRAMBLE;

                            System.out.println("Podaj wielko???? pocz??tkowej generacji: ");
                            int generationSize = Integer.parseInt(scanner.nextLine());
                            System.out.println("Podaj ilo???? reprodukowanych genom??w: ");
                            int reproductionSize = Integer.parseInt(scanner.nextLine());
                            System.out.println("Podaj maksymaln?? liczb?? iteracji: ");
                            int maxIterations = Integer.parseInt(scanner.nextLine());
                            System.out.println("Podaj wsp????czynnik mutacji[0.0 - 1.0]: ");
                            float mutationRate = Float.parseFloat(scanner.nextLine());
                            System.out.println("Podaj wsp????czynnik krzy??owania [0.0 - 1.0]: ");
                            float crossoverRate = Float.parseFloat(scanner.nextLine());
                            System.out.println("Podaj rozmiar turnieju: ");
                            int tournamentSize = Integer.parseInt(scanner.nextLine());
                            System.out.println("Podaj docelowy koszt: ");
                            int targetFitness = Integer.parseInt(scanner.nextLine());

                            System.out.println(tsp.geneticAlgoritmSolution(selectionType, startingCity, generationSize, reproductionSize,  maxIterations, mutationRate, crossoverRate, tournamentSize, targetFitness, mutationType));
                        }

                        else
                            System.out.println("Nie za??adowano pliku");
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
