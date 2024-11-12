package Genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalesmanGenome implements Comparable {

    List<Integer> citySequence;

    int[][] travelPrices;

    int startingCity;

    public int numberOfCities;

    long fitness;

    public SalesmanGenome(int numberOfCities, int[][] travelPrices, int startingCity, InitialSolution initialSolution) {
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;

        if(initialSolution.equals(InitialSolution.GREEDY))
            this.citySequence = greedySolution();
        else
            this.citySequence = randomSolution();

        this.fitness = this.calculateFitness();
    }

    public SalesmanGenome(List<Integer> permutationOfCities, int numberOfCities, int[][] travelPrices, int startingCity) {
        this.citySequence = permutationOfCities;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;

        this.fitness = this.calculateFitness();
    }

    public List<Integer> getCitySequence() {
        return citySequence;
    }

    public int getStartingCity() {
        return startingCity;
    }

    public long getFitness() {
        return fitness;
    }

    private List<Integer> randomSolution() {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < numberOfCities; i++) {
            if (i != startingCity)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }

    private List<Integer> greedySolution() {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[numberOfCities];
        int currentCity = startingCity;

        visited[currentCity] = true;
        for (int i = 1; i < numberOfCities; i++) {
            int nearestCity = -1;
            int nearestDistance = Integer.MAX_VALUE;

            for (int nextCity = 0; nextCity < numberOfCities; nextCity++) {
                if (!visited[nextCity] && travelPrices[currentCity][nextCity] < nearestDistance) {
                    nearestCity = nextCity;
                    nearestDistance = travelPrices[currentCity][nextCity];
                }
            }

            if (nearestCity != -1) {
                result.add(nearestCity);
                visited[nearestCity] = true;
                currentCity = nearestCity;
            }
        }

        return result;
    }

    public long calculateFitness() {
        int fitness = 0;
        int currentCity = startingCity;

        for (int city : citySequence) {
            fitness += travelPrices[currentCity][city];
            currentCity = city;
        }

        // add going back to the starting city
        // subtract 2 -> no starting city in citySequence and indexing starts at 0
        fitness += travelPrices[citySequence.get(numberOfCities-2)][startingCity];

        return fitness;
    }

    @Override
    public int compareTo(Object o) {
        SalesmanGenome genome = (SalesmanGenome) o;
        if(this.fitness > genome.getFitness())
            return 1;
        else if(this.fitness < genome.getFitness())
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Path: ");
        sb.append(startingCity);
        for ( int gene: citySequence) {
            sb.append(" ");
            sb.append(gene);
        }
        sb.append(" ");
        sb.append(startingCity);
        sb.append("\nLength: ");
        sb.append(this.fitness);
        return sb.toString();
    }
}
