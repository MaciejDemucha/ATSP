import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    public int compare(Node s1, Node s2) {
        if (s1.getCost() < s2.getCost())
            return -1;
        else if (s1.getCost() > s2.getCost())
            return 1;
        return 0;
    }

}
