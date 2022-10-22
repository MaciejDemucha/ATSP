import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TSP {
     int cities;
     int[][] distance;

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

}
