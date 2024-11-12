package Genetic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Solution {
    private int generationSize;
    private int genomeSize;
    private int numberOfCities;
    private int maxIterations;

    private int durationInSeconds;

    private int calcFitnessCount;
    private float mutationRate;
    private float crossoverRate;
    private int[][] travelPrices;
    private int startingCity;
    private int tournamentSize;
    private SelectionType selectionType;
    private MutationType mutationType;

    private CrossoverType crossoverType;

    private InitialSolution initialSolution;

    public Solution(int numberOfCities, SelectionType selectionType, int[][] travelPrices, int startingCity,
                    int generationSize, int maxIterations, int durationInSeconds, float mutationRate,
                    float crossoverRate, int tournamentSize, MutationType mutationType, CrossoverType crossoverType, InitialSolution initialSolution){
        this.numberOfCities = numberOfCities;
        this.genomeSize = numberOfCities-1;
        this.selectionType = selectionType;
        this.mutationType = mutationType;
        this.crossoverType = crossoverType;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;

        this.generationSize = generationSize;
        this.maxIterations = maxIterations;
        this.durationInSeconds = durationInSeconds;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.tournamentSize = tournamentSize;
        this.initialSolution = initialSolution;
    }

    public List<SalesmanGenome> initialPopulation(){
        List<SalesmanGenome> population = new LinkedList<>();
        for(int i=0; i < generationSize; i++){
            population.add(new SalesmanGenome(numberOfCities, travelPrices, startingCity, initialSolution));
        }
        return population;
    }

    public List<SalesmanGenome> selection(List<SalesmanGenome> population) {
        List<SalesmanGenome> selected = new LinkedList<>();
        for (int i=0; i < population.size(); i++) {
            if (selectionType == SelectionType.ROULETTE) {
                selected.add(rouletteSelection(population));
            }
            else if (selectionType == SelectionType.TOURNAMENT) {
                selected.add(tournamentSelection(population));
            }
        }

        return selected;
    }

    public SalesmanGenome rouletteSelection(List<SalesmanGenome> population) {
        long totalFitness = population.stream().map(SalesmanGenome::getFitness).mapToLong(Long::longValue).sum();

        Random random = new Random();
        //long selectedValue = random.nextLong(0L, totalFitness);
        long selectedValue = (long)(random.nextDouble()*totalFitness);

        float recValue = (float) 1/selectedValue;

        // We add up values until we reach out recValue, and we pick the
        // genome that crossed the threshold
        float currentSum = 0;
        for (SalesmanGenome genome : population) {
            currentSum += (float) 1/genome.getFitness();
            if (currentSum >= recValue) {
                return genome;
            }
        }

        // In case the return didn't happen in the loop above, we just
        // select at random
        int selectRandom = random.nextInt(generationSize);
        return population.get(selectRandom);
    }

    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random r = new Random();
        int length = list.size();

        if (length < n) return null;

        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    public SalesmanGenome tournamentSelection(List<SalesmanGenome> population) {
        List<SalesmanGenome> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }

    public List<SalesmanGenome> crossoverOX(List<SalesmanGenome> parents) {

        int start = randInt(0, genomeSize-2);
        int end = randInt(start+1, genomeSize-2);

        List<Integer> parent1Genome = new LinkedList<>(parents.get(0).getCitySequence());
        List<Integer> parent2Genome = new LinkedList<>(parents.get(1).getCitySequence());

        List<Integer> sublist = parent1Genome.subList(start, end + 1);
        List<Integer> child = new LinkedList<>(parent2Genome);
        for (Integer city: sublist) {
            child.remove(city);
        }

        for (int i = start; i <= end; i++) {
            child.add(i, sublist.remove(0));
        }

        List<SalesmanGenome> childList = new LinkedList<>();
        childList.add(new SalesmanGenome(child, numberOfCities, travelPrices, startingCity));

        return childList;
    }

    public List<SalesmanGenome> crossoverCX(List<SalesmanGenome> parents) {

        List<Integer> parent1Genome = new LinkedList<>(parents.get(0).getCitySequence());
        List<Integer> parent2Genome = new LinkedList<>(parents.get(1).getCitySequence());
        List<Integer> cycle = new LinkedList<>();
        Integer start = parent1Genome.get(0);
        int indexOfCorresponding = 0;
        cycle.add(start);
        while(true) {
            Integer correspoding = parent2Genome.get(indexOfCorresponding);
            indexOfCorresponding = parent1Genome.indexOf(correspoding);
            if(correspoding.equals(start))
                break;
            cycle.add(correspoding);
        }

        List<Integer> child = new LinkedList<>(parent1Genome);
        List<Integer> orderedCycle = new LinkedList<>(parent2Genome);
        orderedCycle.removeIf(city -> !cycle.contains(city));

        for (Integer city: orderedCycle) {
            child.set(child.indexOf(city), -1);
        }

        for (Integer city: orderedCycle) {
            child.set(child.indexOf(-1), city);
        }

        List<SalesmanGenome> childList = new LinkedList<>();
        childList.add(new SalesmanGenome(child, numberOfCities, travelPrices, startingCity));

        return childList;
    }


    public SalesmanGenome swapMutation(SalesmanGenome salesman) {
        Random random = new Random();
        float mutate = random.nextFloat();
        if (mutate < mutationRate) {
            List<Integer> genome = salesman.getCitySequence();
            Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
            return new SalesmanGenome(genome, numberOfCities, travelPrices, startingCity);
        }
        return salesman;
    }

    public SalesmanGenome inversionMutation(SalesmanGenome salesman) {
        Random random = new Random();
        float mutate = random.nextFloat();
        if (mutate < mutationRate) {
            List<Integer> genome = salesman.getCitySequence();

            int start = randInt(0, genome.size()-2);
            int end = randInt(start+1, genome.size()-2);


            List reversedPart = reverseArray(genome, start, end);
            int indexOfReversedPart = 0;

            for(int i = start; i <= end; i++){
                genome.set(i, (Integer) reversedPart.get(indexOfReversedPart));
                indexOfReversedPart++;
            }

            return new SalesmanGenome(genome, numberOfCities, travelPrices, startingCity);
        }
        return salesman;
    }

    public List<SalesmanGenome> createGeneration(List<SalesmanGenome> population) {
        List<SalesmanGenome> generation = new LinkedList<>();
        int currentGenerationSize = 0;
        Random random = new Random();
        while (currentGenerationSize < generationSize) {
            float crossover = random.nextFloat();

            List<SalesmanGenome> parents = pickNRandomElements(population, 2);
            assert parents != null;
            List<SalesmanGenome> children = null;

            if(crossover < crossoverRate) {
                if(crossoverType == CrossoverType.OX)
                    children = crossoverOX(parents);
                else if(crossoverType == CrossoverType.CX)
                    children = crossoverCX(parents);
            }
            else {
                children = new LinkedList<>(){
                    {
                        add(parents.get(0));
                    }
                };
            }

            assert children != null;
            for (SalesmanGenome child: children) {
                int index = children.indexOf(child);
                if (mutationType == MutationType.SWAP){
                    children.set(index, swapMutation(children.get(index)));
                }
                else if (mutationType == MutationType.INVERSE) {
                    children.set(index, inversionMutation(children.get(index)));
                }
            }

            generation.addAll(children);
            currentGenerationSize += children.size();
        }
        return generation;
    }

    public SalesmanGenome getInitialSolution(InitialSolution initialSolution) {

        return new SalesmanGenome(numberOfCities, travelPrices, startingCity, initialSolution);
    }

    public SalesmanGenome optimize() {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + this.durationInSeconds * 1000L;

        String filePath = generateUniqueFilePath();
        List<SalesmanGenome> population = initialPopulation();
        SalesmanGenome globalBestGenome = population.get(0);
        SalesmanGenome worstGenome;
        int sum = 0;
        for (int i = 0; System.currentTimeMillis() < endTime; i++) {
            List<SalesmanGenome> selected = selection(population);
            population = createGeneration(selected);

            if(globalBestGenome.getFitness() > Collections.min(population).getFitness())
                globalBestGenome = Collections.min(population);

            worstGenome = Collections.max(population);
            for (SalesmanGenome genome: population) {
                sum += genome.fitness;
            }
            long avgPopFitness = sum / population.size();
            sum = 0;
            logger(filePath, i, globalBestGenome.fitness, worstGenome.fitness, avgPopFitness);
        }
        return globalBestGenome;
    }

    private void logger(String filePath, int numOfIteration, long best, long worst, long avg) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath, true));

            writer.write(numOfIteration + ";" + best + ";" + avg + ";" + worst);
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateUniqueFilePath() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        return "output/logs/logs_" + formatter.format(date) + ".csv";
    }

    public static int randInt(int min, int max) {
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }

    public static List<Integer> reverseArray(List<Integer> startArray, int start, int n) {
        List destArray = new LinkedList();

        for (int i = start; i <= n; i++) {
            destArray.add(startArray.get(i));
        }
        Collections.reverse(destArray);

        return destArray;
    }
}
