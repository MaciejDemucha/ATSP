import java.io.*;
import java.util.*;
import java.nio.file.*;

public class TSPToTxtConverter {

    public static void main(String[] args) {
        generateTxt("berlin52.tsp", "berlin52.txt");
    }

    // Function to calculate Euclidean distance
    private static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static void generateTxt(String tspFile, String outputTxtFile) {
        try {
            // Read all lines from the .tsp file
            List<String> lines = Files.readAllLines(Paths.get(tspFile));

            // Variables to store city data
            List<double[]> cityCoordinates = new ArrayList<>();
            boolean startReading = false;

            // Parse the coordinates from the .tsp file
            for (String line : lines) {
                if (line.trim().equals("NODE_COORD_SECTION")) {
                    startReading = true;
                    continue;
                }

                if (startReading) {
                    if (line.trim().equals("EOF")) {
                        break;
                    }
                    // Split the line by spaces to extract city coordinates
                    String[] parts = line.trim().split("\\s+");
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    cityCoordinates.add(new double[]{x, y});
                }
            }

            // Number of cities
            int numCities = cityCoordinates.size();

            // Create a 2D array to store the distances
            double[][] distances = new double[numCities][numCities];

            // Calculate the distance between each pair of cities
            for (int i = 0; i < numCities; i++) {
                for (int j = 0; j < numCities; j++) {
                    distances[i][j] = calculateDistance(cityCoordinates.get(i)[0], cityCoordinates.get(i)[1], cityCoordinates.get(j)[0], cityCoordinates.get(j)[1]);
                }
            }

            // Write the output to the txt file
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputTxtFile));

            // Write number of cities
            writer.write(numCities + "\n");

            // Write the distance matrix
            for (int i = 0; i < numCities; i++) {
                for (int j = 0; j < numCities; j++) {
                    writer.write(String.format("%.0f ", distances[i][j]));  // Format distances to integers
                }
                writer.write("\n");
            }

            // Close the writer
            writer.close();

            System.out.println("Conversion complete! File saved as " + outputTxtFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
