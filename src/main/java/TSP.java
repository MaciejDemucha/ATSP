import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TSP {

    public TSP() {
        hamiltonCycle = Integer.MAX_VALUE;
    }

    boolean[] visitCity;    //tablica zawierająca informację czy dane miasto zostało odwiedzone
    int cities;             //liczba miast
    int[][] distance;       //macierz odległości między miastami

    int hamiltonCycle;      //długość cyklu Hamiltona znalezionego w metodzie Brute Force

    //Branch and Bound
    int sumReduction = 0;   //Suma liczb, które odejmowalismy od poszczególnych wierszy i kolumn w metodzie podziałui ograniczeń
    int costOfStartNode = 0;    //koszt węzła startowego w poszczególnych iteracjach metody podziału i ograniczeń
    Node[] bounds;              //Tablica szacowanych kosztów stanowiących kryterium wyboru następnego węzła

    HashMap<Integer, int[][]> arrays = new HashMap<>();

    int[] finalPath;           //tablica zawierająca numery kolejnych miast w najkrótszej ścieżce
    int finalPathIndex = 0;     //zmienna pomocnicza używana przy wpisywaniu numerów miast do najkrótszej ścieżki
    int finalPathDistance = 0;  //waga znalezionej ścieżki

    //Zmienne pomocnicze do otrzymania ścieżki przy wywoływaniu permutacji w Brute Force
    //ArrayList<Integer> tempPath = new ArrayList();
    int[] tempPath;


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

    /** Wypisanie macieży odległości */
    public void printDistances(){
        System.out.println(this.getCities());
        for( int i = 0; i < this.getCities(); i++){
            for( int j = 0; j < this.getCities(); j++)
                System.out.print(this.getDistance(i, j) + ", ");
            System.out.print("\n");
        }
    }

    public static void printArray(int [][] arr){
        System.out.println(arr.length);
        for( int i = 0; i < arr.length; i++){
            for( int j = 0; j < arr.length; j++)
                System.out.print(arr[i][j] + ", ");
            System.out.print("\n");
        }
    }

    /** Funkcja zwracająca liczbę nieodwiedzonych miast */
    int getNumOfUnvisitedCities(){
        int count = 0;
        for (boolean visited: visitCity) {
            if(!visited) count++;
        }
        return count;
    }

    /**
     * Tworzy losową macierz odległości i ustawia ją.
     * @param size - rozmiar macierzy -- liczba miast
     */
    public static TSP createRandomMatrix(int size){
        TSP tsp = new TSP();
        Random random = new Random();
        int[][] matrix = new int[size][size];
        int upperbound = 300;
        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
                matrix[i][j] = random.nextInt(upperbound) + 1; //odległość z zakresu 1-300

        tsp.setMatrix(matrix);
        tsp.setCities(size);
        //Tablica visitCity ma długość równą liczbie miast
        tsp.visitCity = new boolean[tsp.getCities()];

        //Domyślnie zaczynamy od miasta pierwszego
        tsp.visitCity[0] = true;

        //Tablica ścieżki ma długość liczba miast + 1 (ostatnie miejsce dla miasta początkowego do którego wracamy po odwiedzeniu reszty)
        tsp.finalPath = new int[tsp.getCities() + 1];
        //Pierwszy element ścieżki przyjmuje wartość numeru miasta początkowego
        tsp.finalPath[0] = 0;
        //Inicjalizacja zmiennej pomocniczej
        tsp.finalPathIndex = 0;

        tsp.tempPath = new int[tsp.getCities() + 1];

        return tsp;
    }

    /** Wczytanie danych z pliku */
    public static TSP readFromFileScanner(String filePath) {
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
                        if (scanner.hasNextInt()){
                            tsp.setDistance(i, j, scanner.nextInt());
                            if(tsp.getDistance(i, j) == 0) tsp.setDistance(i, j, 9999);
                        }

                        else
                            scanner.next();
                    }
                }
            }
            //Tablica visitCity ma długość równą liczbie miast
            tsp.visitCity = new boolean[tsp.getCities()];

            //Domyślnie zaczynamy od miasta pierwszego
            tsp.visitCity[0] = true;

            //Tablica ścieżki ma długość liczba miast + 1 (ostatnie miejsce dla miasta początkowego do którego wracamy po odwiedzeniu reszty)
            tsp.finalPath = new int[tsp.getCities() + 1];
            tsp.tempPath = new int[tsp.getCities() + 1];
            //Pierwszy element ścieżki przyjmuje wartość numeru miasta początkowego
            tsp.finalPath[0] = 0;
            //Inicjalizacja zmiennej pomocniczej
            tsp.finalPathIndex = 0;
        }
        catch (FileNotFoundException e){
            System.out.println("Nie odnaleziono pliku " + filePath);
            return null;
        }
        return tsp;
    }


    /** Funkcja realizująca przegląd zupełny
     * @param print - czy wyświetlić wyniki
     */
    public void bruteForce(boolean print){
        hamiltonCycle = findHamiltonianCycle(distance, visitCity, 0, cities, 1, 0, hamiltonCycle);
        if(print){
            System.out.println("Distance: " + hamiltonCycle);
            System.out.print("Path: ");
            for (Integer i: finalPath) {
                System.out.print(i + " ");
            }
            System.out.println();
        }

        //Reset zmiennych po zakończeniu algorytmu
        Arrays.fill(visitCity, false);
        Arrays.fill(finalPath, 0);
        visitCity[0] = true;
    }

    /**
     * Funkcja znajdująca cykl Hamiltona o najmniejszej wadze. Realizowane metodą DFS
     * @param distance - macierz odległości
     * @param visitCity - tablica informująca dane miasto zostało odwiedzone
     * @param currPos - numer miasta, które aktualnie odwiedziliśmy
     * @param cities - liczba miast
     * @param count - liczba przejść w głąb drzewa węzłów
     * @param cost - koszt aktualnie przebytej ścieżki
     * @param hamiltonianCycle - waga znalezionego cyklu Hamiltona
     */
     int findHamiltonianCycle(int[][] distance, boolean[] visitCity, int currPos, int cities, int count, int cost, int hamiltonianCycle) {
         //Sprawdzenie czy liczba przejść równa się liczbie miast.
        //Jeśli tak, to sprawdzamy co ma mniejsza wagę: poprzednio znaleziony cykl czy ten obecny składający się z
        //przebytej ścieżki i powrotu do początkowego węzła.
        if (count == cities && distance[currPos][0] < 9999) {
            tempPath[tempPath.length - 2] = currPos;

            if(hamiltonianCycle > cost + distance[currPos][0]){
                hamiltonianCycle = cost + distance[currPos][0];

                for(int j = 0; j < getCities(); j++){
                    finalPath[j] = tempPath[j];
                }
            }

            return hamiltonianCycle;
        }

        //Przeszukiwanie w głąb każdego miasta
        for (int i = 0; i < cities; i++) {
            if (!visitCity[i] && distance[currPos][i] < 9999) {
                    tempPath[count -1] = currPos;

                //Oznaczamy aktualne miasto jako odwiedzone
                visitCity[i] = true;

                //rekurencyjnie sprawdzamy sąsiadów odwiedzonego miasta
                hamiltonianCycle = findHamiltonianCycle(distance, visitCity, i, cities, count + 1, cost + distance[currPos][i], hamiltonianCycle);

                //po zakończeniu przeszukiwania w głąb danego przypadku oznaczamy miasto jako nieodwiedzone, ponieważ procedura będzie powtarzana dla każdego miasta
                visitCity[i] = false;
            }
        }
        return hamiltonianCycle;
    }

    //Branch and Bound

    /**
     * Funkcja zwracająca najmniejszą liczbę w wierszu macierzy. Zakres liczb 9001 - 9999 traktujemy jako nieskończoność, czyli brak przejścia między miastami.
     * @param i   - indeks wiersza
     * @param arr - macierz
     */
    int firstMinRow(int i, int[][] arr)
    {
        int count = 0; //Zmienna pomocnicza sprawdzająca czy cały wiersz składa się z wartości nieskończoność
        int min = 9999;
        for (int k = 0; k < getCities(); k++){
            if(arr[i][k] > 9000)
                count++;
            if (arr[i][k] < min && i != k)
                min = arr[i][k];
        }
        //Jeśli cały wiersz składa się z wartości nieskończoność to zwracamy 0.
        if(count == getCities())
            min = 0;
        return min;
    }

    /**
     * Funkcja zwracająca najmniejszą liczbę w kolumnie macierzy. Zakres liczb 9001 - 9999 traktujemy jako nieskończoność, czyli brak przejścia między miastami.
     * @param i   - indeks kolumny
     * @param arr - macierz
     */
    int firstMinCol(int i, int[][] arr)
    {
        int count = 0; //Zmienna pomocnicza sprawdzająca czy cały kolumna składa się z wartości nieskończoność
        int min = 9999;
        for (int k = 0; k < getCities(); k++){
            if(arr[k][i] > 9000)
                count++;
            if (arr[k][i] < min && i != k)
                min = arr[k][i];
        }
        //Jeśli cała kolumna składa się z wartości nieskończoność to zwracamy 0.
        if(count == getCities())
            min = 0;
        return min;
    }


    /**
     * Funkcja redukująca macierz odległości: w każdym wierszu znajdujemy element minimalny i odejmujemy go od każdego elementu wiersza. Tak samo czynimy w kolumnach.
     * Przechowujemy sumę tych redukcji, ponieważ będzie ona potrzebna do oszacowywania najkrótszej ścieżki
     * @param arr - macierz odległości
     */
    void reduceMatrix(int[][] arr){
        sumReduction = 0;
        for(int i = 0; i < getCities(); i++){
            int localMin = firstMinRow(i, arr);
            sumReduction += localMin;
            for(int j= 0; j < getCities(); j++) {
                arr[i][j] -= localMin;
            }
        }

        for(int i = 0; i < getCities(); i++){
            int localMin = firstMinCol(i, arr);
            sumReduction += localMin;
            for(int j= 0; j < getCities(); j++) {
                arr[j][i] -= localMin;
            }
        }
    }

    /**
     * Funkcja zwracająca węzeł z najmniejszym oszacowanym kosztem.
     * @param arr - tablica składająca się z obiektów typu Node zawierających numer miasta i oszacowany koszt
     */
    Node nodeWithMinCost(Node[] arr){
        int index = arr[0].getNumber();
        int min = arr[0].getCost();
        for (Node node : arr) {
            if (node.getCost() < min) {
                min = node.getCost();
                index = node.getNumber();
            }
        }
        return new Node(index, min);
    }

    void saveArrAfterFirstRection(){
        int[][] array = new int[getCities()][getCities()];    //zmienna tymczasowa przechowująca tablicę odległości

        //kopiowanie tablicy odległości
        for( int i = 0; i < this.getCities(); i++)
            for( int j = 0; j < this.getCities(); j++)
                array[i][j] = getDistance(i,j);

        //pierwsza redukcja macierzy
        //macierz posiada conajmniej raz 0 w każdym wierszu i kolumnie
        reduceMatrix(array);
        arrays.put(0, array);
    }

    /**
     * Realizacja funkcji ograniczającej.
     * @param from - Węzeł (miasto) od którego rozpoczynamy szacowanie
     * @param print - czy wyświetlić wynik
     */
    void expandNodes(int from, boolean print){
        bounds = new Node[getNumOfUnvisitedCities()];   //tablica w której przechowujemy węzły z ich oszacowanymi kosztami
        int boundNumber = 0;

        int[][] tempArr = new int[getCities()][getCities()];    //zmienna tymczasowa przechowująca tablicę odległości
        int[][] arrAfterFirstReduction = new int[getCities()][getCities()];
        int[][] arrOfLastNodeInPath = new int[getCities()][getCities()];

        //zapisujemy tablice po pierwszej redukcji
         arrAfterFirstReduction = arrays.get(0);

        //przy pierwszej redukcji koszt startowego węzła jest równy sumie redukcji
        if(costOfStartNode == 0)
            costOfStartNode = sumReduction;

        //Szacowanie
        for (int k = 0; k < getCities(); k++){
            if(visitCity[k]) continue;  //pomijamy miasto, jeśli było juz odwiedzone
            boundNumber++;

            int[][] arrayToLoad = arrays.get(from);

            for( int i = 0; i < this.getCities(); i++)
                for( int j = 0; j < this.getCities(); j++)
                    arrOfLastNodeInPath[i][j] = arrayToLoad[i][j];

            for( int i = 0; i < this.getCities(); i++)
                for( int j = 0; j < this.getCities(); j++)
                    tempArr[i][j] = arrOfLastNodeInPath[i][j];

            int edge = arrAfterFirstReduction[from][k];    //zapisujemy koszt przejścia z węzła początkowego do rozpatrywanego

            //Ustawiamy wiersz o indeksie węzła początkowego na nieskończoność
            for (int j= 0; j< getCities(); j++)
                tempArr[from][j] = 9999;

            //Ustawiamy kolumnę o indeksie węzła rozpatrywanego na nieskończoność
            for (int j= 0; j< getCities(); j++)
                tempArr[j][k] = 9999;

            //Ustawiamy odległość z węzła rozpatrywanego do początkowego na nieskończoność
            tempArr[k][0] = 9999;

            //redukujemy macierz, aby w każdym wierszu i kolumnie było conajmniej raz 0
            reduceMatrix(tempArr);

            int[][] arrayToRemember = new int[getCities()][getCities()];

            for( int i = 0; i < this.getCities(); i++)
                for( int j = 0; j < this.getCities(); j++)
                    arrayToRemember[i][j] = tempArr[i][j];

            arrays.put(k, arrayToRemember);

            //Koszt każdego węzła stanowi suma redukcji macierzy, odległość z miasta początkowego do rozpatrywanego i koszt węzła początkowego
            Node node  = new Node(k, (sumReduction + edge + costOfStartNode));
            bounds[boundNumber-1] = node;
            if(print){
                System.out.println("from: " + (from));
                System.out.println("bound " + (k) + ": " + sumReduction + " + " + edge + " + " + costOfStartNode + " = " + bounds[boundNumber-1].getCost());
            }
        }

        //Szukamy węzła z minimalnym kosztem, wstawiamy jego numer do ścieżki, oznaczamy miasto jako odwiedzone i dodajemy wagę do ostatecznego kosztu ścieżki.
        Node nodeWithMinCost = nodeWithMinCost(bounds);
        finalPath[++finalPathIndex] = nodeWithMinCost.getNumber();
        costOfStartNode = nodeWithMinCost.getCost();
        visitCity[finalPath[finalPathIndex]] = true;
        finalPathDistance += getDistance(from, finalPath[finalPathIndex]);
        if(getNumOfUnvisitedCities() == 0) finalPathDistance += getDistance(finalPath[finalPathIndex], 0);
        if(print){
            for (int node: finalPath)
                System.out.print(node + " ");
            System.out.println("Current distance: " + finalPathDistance);
        }
    }

    /**
     * Realizacja metody Branch and Bound (podziału i ograniczeń)
     * @param print - czy wyświetlić wynik
     */
    public void bnBSolution(boolean print){
        saveArrAfterFirstRection();
        while(getNumOfUnvisitedCities() > 0){
            expandNodes(finalPath[finalPathIndex], true);
        }

        if(print) {
            System.out.println("Distance: " + this.finalPathDistance);
            System.out.print("Path: ");
            for (int node: finalPath)
                System.out.print(node + " ");
            System.out.println();
        }

        //Reset zmiennych po zakończeniu algorytmu
        Arrays.fill(visitCity, false);
        visitCity[0] = true;
        Arrays.fill(finalPath, 0);
        finalPathIndex = 0;
        finalPathDistance = 0;
        costOfStartNode = 0;
        sumReduction = 0;
    }
}
