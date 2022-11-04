import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class TSP {

    public TSP() {
        hamiltonCycle = Integer.MAX_VALUE;
    }

    boolean[] visitCity;
    int cities;
    int[][] distance;

    int hamiltonCycle;

    int sumReduction = 0;
    int costOfStartNode = 0;
    Node[] bounds;

    int[] final_path;
    int finalPathIndex = 0;
    int finalPathDistance = 0;

    public int getCities() {
        return cities;
    }

    public void setCities(int cities) {
        this.cities = cities;
    }

    public int[][] getMatrix() {
        return distance;
    }

    public int getDistance(int x, int y){
        return distance[x][y];
    }

    public void setMatrix(int[][] distance) {
        this.distance = distance;
    }

    public void setMatrix(int x, int y) {
        this.distance = new int[x][y];
    }

    public void setDistance(int x, int y, int value) {
        this.distance[x][y] = value;
    }

    public void printDistances(){
        System.out.println(this.getCities());
        for( int i = 0; i < this.getCities(); i++){
            for( int j = 0; j < this.getCities(); j++)
                System.out.print(this.getDistance(i, j) + " ");
            System.out.print("\n");
        }
    }

    int getNumOfUnvisitedCities(){
        int count = 0;
        for (boolean visited: visitCity) {
            if(!visited) count++;
        }
        return count;
    }

    public static TSP readFromFileScanner(String filePath) throws FileNotFoundException {
        TSP tsp = new TSP();
        try{
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()){
                if (scanner.hasNextInt()) {
                    tsp.setCities(scanner.nextInt());
                    tsp.setMatrix(tsp.getCities(), tsp.getCities());
                    break;
                }
                else
                    scanner.next();
            }

            while (scanner.hasNext()) {
                for( int i = 0; i < tsp.getCities(); i++){
                    scanner.nextLine();
                    for( int j = 0; j < tsp.getCities(); j++){
                        if (scanner.hasNextInt())
                            tsp.setDistance(i, j, scanner.nextInt());
                        else
                            scanner.next();
                    }
                }
            }
            // create an array of type boolean to check if a node has been visited or not
            tsp.visitCity = new boolean[tsp.getCities()];

            // by default, we make the first city visited
            tsp.visitCity[0] = true;

            tsp.final_path = new int[tsp.getCities() + 1];
            tsp.final_path[0] = 0;
            tsp.finalPathIndex = 0;
        }
        catch (FileNotFoundException e){
            System.out.println("Nie odnaleziono pliku " + filePath);
            return null;
        }
        return tsp;
    }

    public void bruteForce(boolean print){
        hamiltonCycle = findHamiltonianCycle(distance, visitCity, 0, cities, 1, 0, hamiltonCycle);
        if(print)
        System.out.println("Distance: " + hamiltonCycle);

        Arrays.fill(visitCity, false);
        visitCity[0] = true;
    }

    // create findHamiltonianCycle() method to get minimum weighted cycle
    static int findHamiltonianCycle(int[][] distance, boolean[] visitCity, int currPos, int cities, int count, int cost, int hamiltonianCycle) {

        if (count == cities && distance[currPos][0] > 0) {
            hamiltonianCycle = Math.min(hamiltonianCycle, cost + distance[currPos][0]);
            return hamiltonianCycle;
        }

        // BACKTRACKING STEP
        for (int i = 0; i < cities; i++) {
            if (visitCity[i] == false && distance[currPos][i] > 0) {

                // Mark as visited
                visitCity[i] = true;

                hamiltonianCycle = findHamiltonianCycle(distance, visitCity, i, cities, count + 1, cost + distance[currPos][i], hamiltonianCycle);

                // Mark ith node as unvisited
                visitCity[i] = false;
            }
        }
        return hamiltonianCycle;
    }

    //Branch and Bound

    // Function to find the minimum edge cost
    // having an end at the vertex i
    int firstMinRow(int from, int i, int[][] arr)
    {
        int count = 0;
        int min = 9999;
        for (int k = from; k < getCities(); k++){
            if(arr[i][k] > 9000)
                count++;
            if (arr[i][k] < min && i != k)
                min = arr[i][k];
        }
        if(count == getCities())
            min = 0;
        return min;
    }

    // Function to find the minimum edge cost
    // having an end at the vertex i
    int firstMinCol(int from, int i, int[][] arr)
    {
        int count = 0;
        int min = 9999;
        for (int k = from; k < getCities(); k++){
            if(arr[k][i] > 9000)
                count++;
            if (arr[k][i] < min && i != k)
                min = arr[k][i];
        }
        if(count == getCities())
            min = 0;
        return min;
    }

    Node nodeWithMinCost(Node[] arr){
        int index = arr[0].getNumber();
        int min = arr[0].getCost();
        for(int i = 0; i < arr.length; i++){
            if(arr[i].getCost() < min){
                min = arr[i].getCost();
                index = arr[i].getNumber();
            }
        }
        Node node = new Node(index, min);
        return node;
    }

    void reduceMatrix(int[][] arr){
        sumReduction = 0;
        for(int i = 0; i < getCities(); i++){
            int localMin = firstMinRow(0, i, arr);
            sumReduction += localMin;
            for(int j= 0; j < getCities(); j++) {
                arr[i][j] -= localMin;
            }
        }

        for(int i = 0; i < getCities(); i++){
            int localMin = firstMinCol(0, i, arr);
            sumReduction += localMin;
            for(int j= 0; j < getCities(); j++) {
                arr[j][i] -= localMin;
            }
        }
    }

    void expandNodes(int from, boolean print){
        bounds = new Node[getNumOfUnvisitedCities()];
        int boundNumber = 0;

        int[][] tempArr = new int[getCities()][getCities()];

        for( int i = 0; i < this.getCities(); i++)
            for( int j = 0; j < this.getCities(); j++)
                tempArr[i][j] = getDistance(i,j);

        reduceMatrix(tempArr);
        if(costOfStartNode == 0)
            costOfStartNode = sumReduction;

        int[][] arrAfterFirstReduction = new int[getCities()][getCities()];

        for( int i = 0; i < this.getCities(); i++)
            for( int j = 0; j < this.getCities(); j++)
                arrAfterFirstReduction[i][j] = tempArr[i][j];

        for (int k = 0; k < getCities(); k++){
            if(visitCity[k]) continue;
            boundNumber++;

            for( int i = 0; i < this.getCities(); i++)
                for( int j = 0; j < this.getCities(); j++)
                    tempArr[i][j] = arrAfterFirstReduction[i][j];

            int edge = tempArr[from][k];

            for (int j= 0; j< getCities(); j++)
                tempArr[from][j] = 9999;

            for (int j= 0; j< getCities(); j++)
                tempArr[j][k] = 9999;

            tempArr[k][from] = 9999;

            reduceMatrix(tempArr);

            Node node  = new Node(k+1, (sumReduction + edge + costOfStartNode));
            bounds[boundNumber-1] = node;
            if(print){
                System.out.println("from: " + (from));
                System.out.println("bound " + (k+1) + ": " + sumReduction + " + " + edge + " + " + costOfStartNode + " = " + bounds[boundNumber-1].getCost());
            }
        }

        Node nodeWithMinCost = nodeWithMinCost(bounds);
        final_path[++finalPathIndex] = nodeWithMinCost.getNumber() - 1;
        costOfStartNode = nodeWithMinCost.getCost();
        visitCity[final_path[finalPathIndex]] = true;
        finalPathDistance += getDistance(from, final_path[finalPathIndex]);
        if(getNumOfUnvisitedCities() == 0) finalPathDistance += getDistance(final_path[finalPathIndex], 0);
        if(print)
        for (int node:final_path) {
            System.out.print(node + " ");
        }
    }

    public void bnBSolution(boolean print){
        while(getNumOfUnvisitedCities() > 0){
            expandNodes(final_path[finalPathIndex], print);
        }
        if(print) System.out.println("Distance: " + this.finalPathDistance);

        Arrays.fill(visitCity, false);
        visitCity[0] = true;
        Arrays.fill(final_path, 0);
        finalPathIndex = 0;
        finalPathDistance = 0;
        costOfStartNode = 0;
        sumReduction = 0;
    }

    int boundFun(int from, int i) throws Exception {
        if(from >= i) throw new Exception("Wrong parameters for bound");
        return (firstMinRow(from, i, distance) + firstMinCol(from, i, distance))/2;
    }
    
    int bound2(int from, int i){
        int bound = 0;
        for(int j = 0; j <= i; j++)
        bound += firstMinRow(from, j, distance);

        return bound;
    }

}
