import java.io.*;
import java.util.*;
import java.nio.file.*;

public class TSPToTxtConverter {

    public static void main(String[] args) {
        generateTxt("gr666.tsp", "gr666.txt");
    }

    private static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static void generateTxt(String tspFile, String outputTxtFile) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(tspFile));

            List<double[]> cityCoordinates = new ArrayList<>();
            boolean startReading = false;

            for (String line : lines) {
                if (line.trim().equals("NODE_COORD_SECTION")) {
                    startReading = true;
                    continue;
                }

                if (startReading) {
                    if (line.trim().equals("EOF")) {
                        break;
                    }
                    String[] parts = line.trim().split("\\s+");
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    cityCoordinates.add(new double[]{x, y});
                }
            }

            int numCities = cityCoordinates.size();

            double[][] distances = new double[numCities][numCities];

            for (int i = 0; i < numCities; i++) {
                for (int j = 0; j < numCities; j++) {
                    distances[i][j] = calculateDistance(cityCoordinates.get(i)[0], cityCoordinates.get(i)[1], cityCoordinates.get(j)[0], cityCoordinates.get(j)[1]);
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputTxtFile));

            writer.write(numCities + "\n");

            for (int i = 0; i < numCities; i++) {
                for (int j = 0; j < numCities; j++) {
                    writer.write(String.format("%.0f ", distances[i][j]));
                }
                writer.write("\n");
            }

            writer.close();

            System.out.println("Conversion complete! File saved as " + outputTxtFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
