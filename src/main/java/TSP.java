import java.io.*;
import java.util.Scanner;

public class TSP {

    public TSP() {
        hamiltonCycle = Integer.MAX_VALUE;
    }

    boolean[] visitCity;
    int cities;
    int[][] distance;

    int hamiltonCycle;

    int sumReduction;
    int[] bounds;

    int[] final_path;
    // Stores the final minimum weight of shortest tour.
    static int final_res = Integer.MAX_VALUE;

    public int getCities() {
        return cities;
    }

    public void setCities(int cities) {
        this.cities = cities;
    }

    public int[][] getDistance() {
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

    public static TSP readFromFile(BufferedReader reader) throws IOException {
        TSP tsp = new TSP();

        String line = reader.readLine();
        String[] txt = line.split(" ");
        tsp.setCities(Integer.parseInt(txt[0]));

        tsp.setMatrix(tsp.getCities(), tsp.getCities());
        for( int i = 0; i < tsp.getCities(); i++){
            line = reader.readLine();
            txt = line.split(" ");
            for( int j = 0; j < tsp.getCities(); j++){
                //int distance = Integer.parseInt(txt[j]);
                tsp.setDistance(i, j, Integer.parseInt(txt[j]));
                //if(tsp.getDistance(i, j) == 0)
                    //tsp.setDistance(i, j, Integer.MAX_VALUE);
            }
        }

        // create an array of type boolean to check if a node has been visited or not
        tsp.visitCity = new boolean[tsp.getCities()];

        // by default, we make the first city visited
        tsp.visitCity[0] = true;

        return tsp;
    }

    public static TSP readFromFile(String file_name) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_name))) {
            return TSP.readFromFile(reader);
        } catch (FileNotFoundException e){
            throw new Exception("Nie odnaleziono pliku " + file_name);
        } catch(IOException e){
            throw new Exception("Wystąpił błąd podczas odczytu danych z pliku.");
        }
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
        }
        catch (FileNotFoundException e){
            throw new FileNotFoundException("Nie odnaleziono pliku " + filePath);
        }
        return tsp;
    }

    public void bruteForce(boolean print){
        hamiltonCycle = findHamiltonianCycle(distance, visitCity, 0, cities, 1, 0, hamiltonCycle);
        if(print)
        System.out.println(hamiltonCycle);
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
                //System.out.print(currPos + 1 + ", ");


                hamiltonianCycle = findHamiltonianCycle(distance, visitCity, i, cities, count + 1, cost + distance[currPos][i], hamiltonianCycle);

                // Mark ith node as unvisited
                visitCity[i] = false;
            }
        }
        //System.out.println(hamiltonianCycle);
        return hamiltonianCycle;
    }

    //Branch and Bound

    // Function to copy temporary solution to
    // the final solution
    void copyToFinal(int curr_path[])
    {
        for (int i = 0; i < this.getCities(); i++)
            final_path[i] = curr_path[i];
        final_path[this.getCities()] = curr_path[0];
    }

    // Function to find the minimum edge cost
    // having an end at the vertex i
    int firstMin(int from, int i)
    {
        int min = Integer.MAX_VALUE;
        for (int k = from; k < getCities(); k++)
            if (getDistance(i, k) < min && i != k)
                min = getDistance(i, k);
        return min;
    }

    // Function to find the minimum edge cost
    // having an end at the vertex i
    int firstMin2(int from, int i)
    {
        int min = Integer.MAX_VALUE;
        for (int k = from; k < getCities(); k++)
            if (getDistance(k, i) < min && i != k)
                min = getDistance(k, i);
        return min;
    }

    // function to find the second minimum edge cost
    // having an end at the vertex i
    int secondMin(int i)
    {
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for (int j=0; j<getCities(); j++)
        {
            if (i == j)
                continue;

            if (getDistance(i, j) <= first)
            {
                second = first;
                first = getDistance(i, j);
            }
            else if (getDistance(i, j) <= second &&
                    getDistance(i, j) != first)
                second = getDistance(i, j);
        }
        return second;
    }

    void reduceMatrix(int[][] arr){
        sumReduction = 0;
        for(int i = 0; i < getCities(); i++){
            int localMin = firstMin(0, i);
            sumReduction += localMin;
            for(int j= 0; j < getCities(); j++) {
                arr[i][j] -= localMin;
            }
        }

        for(int i = 0; i < getCities(); i++){
            int localMin = firstMin2(0, i);
            sumReduction += localMin;
            for(int j= 0; j < getCities(); j++) {
                arr[j][i] -= localMin;
            }
        }
    }

    void expandNode(int i){
        int firstSum = 0;
        int[][] tempArr = distance.clone();
        int[][] arrAfterFirstIt = new int[getCities()][getCities()];
        for (int k = 0; k < getCities(); k++){

            if(k > 0) tempArr = arrAfterFirstIt;

        for (int j= 0; j< getCities(); j++)
        tempArr[0][j] = Integer.MAX_VALUE;

        for (int j= 0; j< getCities(); j++)
            tempArr[j][k] = Integer.MAX_VALUE;

            tempArr[k+1][0] = Integer.MAX_VALUE;

            reduceMatrix(tempArr);

            if(k == 0){
                firstSum = sumReduction;
                arrAfterFirstIt = tempArr;
            }

        bounds[k] = sumReduction + getDistance(0, k) + firstSum;

        }
    }

    void branch(){

    }


    int boundFun(int from, int i) throws Exception {
        if(from >= i) throw new Exception("Wrong parameters for bound");
        return (firstMin(from, i) + firstMin2(from, i))/2;
    }
    
    int bound2(int from, int i){
        int bound = 0;
        for(int j = 0; j <= i; j++)
        bound += firstMin(from, j);

        return bound;
    }

}
