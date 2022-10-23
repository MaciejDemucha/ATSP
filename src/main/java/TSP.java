import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TSP {

    public TSP() {
        hamiltonCycle = Integer.MAX_VALUE;
    }

    boolean[] visitCity;
    int cities;
    int[][] distance;

    int hamiltonCycle;

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

    public void setDistance(int[][] distance) {
        this.distance = distance;
    }

    public void setDistance(int x, int y) {
        this.distance = new int[x][y];
    }

    public void setDistance(int x, int y, int value) {
        this.distance[x][y] = value;
    }

    public void printDistances(){
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

        tsp.setDistance(tsp.getCities(), tsp.getCities());
        for( int i = 0; i < tsp.getCities(); i++){
            line = reader.readLine();
            txt = line.split(" ");
            for( int j = 0; j < tsp.getCities(); j++){
                tsp.setDistance(i, j, Integer.parseInt(txt[j]));
                if(tsp.getDistance(i, j) == 0)
                    tsp.setDistance(i, j, Integer.MAX_VALUE);
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

    public void solution(boolean print){
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
                hamiltonianCycle = findHamiltonianCycle(distance, visitCity, i, cities, count + 1, cost + distance[currPos][i], hamiltonianCycle);

                // Mark ith node as unvisited
                visitCity[i] = false;
            }
        }
        return hamiltonianCycle;
    }

}
