package ring;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Ring {
    public static void main(String[] args) {
        ArrayList<Node> nodeList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int totalNodes = scanner.nextInt();
        int tokenHolder = new Random().nextInt(totalNodes);

        for (int i = 0; i < totalNodes; i++) {
            if (i == tokenHolder) {
                nodeList.add(new Node(i, true, "locked"));
            } else {
                nodeList.add(new Node(i, false, "unlocked"));
            }
        }

        for (int i = 0; i < totalNodes; i++) {
            nodeList.get(i).neighbor = nodeList.get((i + 1) % totalNodes);
            Node node = nodeList.get(i);
            System.out.println("Process ID: " + node.processId + " | Has Token: " + node.hasToken
                    + " | Neighbor: " + node.neighbor.processId);
        }

        scanner.close();

        for (Node node : nodeList) {
            new Thread(node).start();
        }
    }
}
