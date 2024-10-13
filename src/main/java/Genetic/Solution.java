package Genetic;

import java.util.*;

public class Solution {
    private int generationSize;
    private int genomeSize;
    private int numberOfCities;
    private int maxIterations;
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
                    int generationSize, int maxIterations, float mutationRate,
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
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.tournamentSize = tournamentSize;
        this.initialSolution = initialSolution;
    }

    public List<SalesmanGenome> initialPopulation(){
        List<SalesmanGenome> population = new ArrayList<>();
        for(int i=0; i < generationSize; i++){
            population.add(new SalesmanGenome(numberOfCities, travelPrices, startingCity, initialSolution));
        }
        return population;
    }

    public List<SalesmanGenome> selection(List<SalesmanGenome> population) {
        List<SalesmanGenome> selected = new ArrayList<>();
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
        int totalFitness = population.stream().map(SalesmanGenome::getFitness).mapToInt(Integer::intValue).sum();

        // We pick a random value - a point on our roulette wheel
        Random random = new Random();
        int selectedValue = random.nextInt(totalFitness);

        // Because we're doing minimization, we need to use reciprocal
        // value so the probability of selecting a genome would be
        // inversely proportional to its fitness - the smaller the fitness
        // the higher the probability
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

    // A helper function to pick n random elements from the population
// so we could enter them into a tournament
    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random r = new Random();
        int length = list.size();

        if (length < n) return null;

        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    // A simple implementation of the deterministic tournament - the best genome
// always wins
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

        List<SalesmanGenome> childList = new ArrayList<>();
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

        List<SalesmanGenome> childList = new ArrayList<>();
        childList.add(new SalesmanGenome(child, numberOfCities, travelPrices, startingCity));

        return childList;
    }

    public List<SalesmanGenome> crossover(List<SalesmanGenome> parents) {
        // Housekeeping
        Random random = new Random();

        int breakpoint = random.nextInt(genomeSize);
        List<SalesmanGenome> children = new ArrayList<>();

        // Copy parental genomes - we copy so we wouldn't modify in case they were
        // chosen to participate in crossover multiple times
        List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getCitySequence());
        List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getCitySequence());

        // Creating child 1
        for (int i = 0; i < breakpoint; i++) {
            int newVal;
            newVal = parent2Genome.get(i);
            Collections.swap(parent1Genome, parent1Genome.indexOf(newVal), i);
        }
        children.add(new SalesmanGenome(parent1Genome, numberOfCities, travelPrices, startingCity));
        parent1Genome = parents.get(0).getCitySequence(); // Reseting the edited parent

        // Creating child 2
        for (int i = breakpoint; i < genomeSize; i++) {
            int newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
        }
        children.add(new SalesmanGenome(parent2Genome, numberOfCities, travelPrices, startingCity));

        return children;
    }

    public SalesmanGenome mutate(SalesmanGenome salesman) {
        Random random = new Random();
        float mutate = random.nextFloat();
        if (mutate < mutationRate) {
            List<Integer> genome = salesman.getCitySequence();
            Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
            return new SalesmanGenome(genome, numberOfCities, travelPrices, startingCity);
        }
        return salesman;
    }

    public static int randInt(int min, int max) {
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }

    public static List<Integer> reverseArray(List<Integer> startArray, int start, int n) {
        List destArray = new ArrayList();

        for (int i = start; i <= n; i++) {
            destArray.add(startArray.get(i));
        }
        Collections.reverse(destArray);

        return destArray;
    }

    public static List<Integer> shuffleArray(List<Integer> startArray, int start, int n) {
        List destArray = new ArrayList();

        for (int i = start; i <= n; i++) {
            destArray.add(startArray.get(i));
        }
        Collections.shuffle(destArray);

        return destArray;
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

    public SalesmanGenome scrambleMutation(SalesmanGenome salesman) {
        Random random = new Random();
        float mutate = random.nextFloat();
        if (mutate < mutationRate) {
            List<Integer> genome = salesman.getCitySequence();

            int start = randInt(0, genome.size()-2);
            int end = randInt(start+1, genome.size()-2);


            List reversedPart = shuffleArray(genome, start, end);
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
        List<SalesmanGenome> generation = new ArrayList<>();
        int currentGenerationSize = 0;
        Random random = new Random();
        while (currentGenerationSize < generationSize) {
            float crossover = random.nextFloat();

            List<SalesmanGenome> parents = pickNRandomElements(population, 2);
            List<SalesmanGenome> children = new ArrayList<>(parents);

            if(crossover < crossoverRate) {
                if(crossoverType == CrossoverType.ONEPOINT)
                    children = crossover(parents);
                else if(crossoverType == CrossoverType.OX)
                    children = crossoverOX(parents);
                else if(crossoverType == CrossoverType.CX)
                    children = crossoverCX(parents);
            }

            for (SalesmanGenome child: children) {
                int index = children.indexOf(child);
                if (mutationType == MutationType.SWAP){
                    children.set(index, mutate(children.get(index)));
                }
                else if (mutationType == MutationType.INVERSE) {
                    children.set(index, inversionMutation(children.get(index)));
                }
                else{
                    children.set(index, scrambleMutation(children.get(index)));
                }
            }

            generation.addAll(children);
            currentGenerationSize += children.size();
        }
        return generation;
    }

    public void printGeneration(List<SalesmanGenome> generation ){
        for( SalesmanGenome genome : generation){
            System.out.println(genome);
        }
    }

    public SalesmanGenome optimize() {
        List<SalesmanGenome> population = initialPopulation();
        SalesmanGenome globalBestGenome = population.get(0);
        for (int i = 0; i < maxIterations; i++) {
            List<SalesmanGenome> selected = selection(population);
            population = createGeneration(selected);
            globalBestGenome = Collections.min(population);
        }
        return globalBestGenome;
    }
}
