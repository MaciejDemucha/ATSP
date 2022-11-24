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

    int[] finalPath;           //tablica zawierająca numery kolejnych miast w najkrótszej ścieżce


    //Zmienne pomocnicze do otrzymania ścieżki przy wywoływaniu permutacji w Brute Force
    int[] tempPath;

    Node[] nodes;


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


    /**
     * Tworzy losową macierz odległości i ustawia ją.
     * @param size - rozmiar macierzy -- liczba miast
     */
    public static TSP createRandomMatrix(int size){
        TSP tsp = new TSP();
        Random random = new Random();
        int[][] matrix = new int[size][size];
        int upperbound = 50;
        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
                matrix[i][j] = random.nextInt(upperbound) + 1; //odległość z zakresu 1-50

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

        tsp.tempPath = new int[tsp.getCities() + 1];

        tsp.nodes = new Node[tsp.getCities()];
        tsp.addNodes();

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
                            if(/*tsp.getDistance(i, j) == 0*/ i==j) tsp.setDistance(i, j, 9999);
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

            tsp.nodes = new Node[tsp.getCities()];
            tsp.addNodes();

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
            System.out.println("Path: " + Arrays.toString(finalPath));
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

    int greedy(){
        int size = getCities() - 1;
        int count = 0;
        int cityNumber = 0;
        int distance = 0;
        int[] min = new int[2];
        nodes[0].explored = true;
        while(count < size){
            min = firstMinRow(cityNumber, getMatrix(), true);
            distance += min[1];
            cityNumber = min[0];
            count++;
        }

        distance += getDistance(min[0], 0);
        allNotExplored();
        return distance;
    }

    int calcLowerBound(Node node){
        int lowerBound = node.distanceSoFar;
        int size = nodes.length;
        for(int i = 0; i < size; i++){
            if (!nodes[i].explored){
                lowerBound += firstMinRow(i, getMatrix(), false)[1];
            }
        }

        return lowerBound;
    }

    /**
     * Funkcja zwracająca najmniejszą liczbę w wierszu macierzy. Zakres liczb 9001 - 9999 traktujemy jako nieskończoność, czyli brak przejścia między miastami.
     * @param i   - indeks wiersza
     * @param arr - macierz
     */
    int[] firstMinRow(int i, int[][] arr, boolean usedInGreedy)
    {
        int count = 0; //Zmienna pomocnicza sprawdzająca czy cały wiersz składa się z wartości nieskończoność
        int[] minimum = new int[2];
        minimum[0] = 0;
        minimum[1] = 9999;

        for (int k = 0; k < getCities(); k++){
            if(arr[i][k] > 9000)
                count++;
            if (arr[i][k] < minimum[1] && i != k && (!nodes[k].explored || k == 0)){
                minimum[0] = k;
                minimum[1] = arr[i][k];
            }
        }
        if(usedInGreedy)
        nodes[minimum[0]].explored = true;
        //Jeśli cały wiersz składa się z wartości nieskończoność to zwracamy 0.
        if(count == getCities())
            minimum[1] = 0;

        return minimum;
    }

    //BFS

    Result bfs(Node root, boolean print){
        LinkedList<Integer> path = new LinkedList<>();
        LinkedList<Node> queue = new LinkedList<>();
        int permutationSize = nodes.length;
        Result result = new Result();
        result.path = new int[permutationSize];
        int distance;
        int greedySolution = greedy();
        if (print)
        System.out.println("Greedy: " + greedySolution);
        queue.add(root);

        while (!queue.isEmpty()){
            Node v = queue.poll();

            if(v.level == permutationSize-1){

                path = createPath(v, path);
                distance = v.distanceSoFar + getDistance(v.number, 0);
                if(distance < result.cost){
                    result.cost = distance;
                    copyPathToResult(result, path);
                }
            }

            nodes[v.number].explored = true;
            setExploredNodes(v);

            for (int i = 0; i < permutationSize; i++) {
                if(!nodes[i].explored){
                    Node w = copyNode(nodes[i]);
                    w.level = v.level + 1;
                    w.parent = v;
                    w.distanceSoFar = v.distanceSoFar + getDistance(v.number, w.number);
                    w.lowerBound = calcLowerBound(w);

                    if(w.lowerBound <= greedySolution)
                        queue.add(w);
                }
            }
            allNotExplored();
        }
        return result;
    }

    void doBFS(boolean print){
        Node root = nodes[0];
        Result result = bfs(root, print);
        if(print){
            System.out.println("Path: " + Arrays.toString(result.path));
            System.out.println("Cost: " + result.cost);
        }
    }

    LinkedList<Integer> createPath(Node endNode, LinkedList<Integer> path){
        path.clear();
        path = addParents(endNode, path);
        path.addFirst(0);
        return path;
    }

    LinkedList<Integer> addParents(Node endNode, LinkedList<Integer> path){
        if(endNode.parent != null){
            path.add(0, endNode.number);
                addParents(endNode.parent, path);
        }
        return path;
    }

    void copyPathToResult(Result result, LinkedList<Integer> path){
        int size = path.size();
        for(int i = 0; i < size; i++){
            result.path[i] = path.get(i);
        }
    }

    void setExploredNodes(Node node){
        if(node.parent != null){
            nodes[node.parent.number].explored = true;
            setExploredNodes(node.parent);
        }
    }

    void allNotExplored(){
        int size = nodes.length;
        for(int i = 0; i < size; i++)
            nodes[i].explored = false;
    }

    void addNodes(){
        int size = nodes.length;
        for (int i = 0; i < size; i++){
            Node node = new Node();
            node.number = i;
            node.level = 0;
            nodes[i] = node;
        }
    }

    public static Node copyNode(Node other ) {
        Node newNode = new Node();
        newNode.number = other.number;
        newNode.parent = other.parent;
        newNode.level = other.level;
        newNode.distanceSoFar = other.distanceSoFar;
        newNode.explored = other.explored;
        newNode.lowerBound = other.lowerBound;
        return newNode;
    }

}
