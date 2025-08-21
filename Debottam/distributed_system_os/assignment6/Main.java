import java.util.*;

public class Main {

    public static HashSet<Integer> s = new HashSet<>();
    public static ArrayList<Integer> arr = new ArrayList<>();
    public static ArrayList<Node> nodesList = new ArrayList<>();
    public static int[][] graph;
    public static int n;

    public static void main(String[] args) {
        System.out.println("Mitchell Merritt DDD algorithm using Adjacency Matrix");
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter total number of nodes: ");
        n = sc.nextInt();
        graph = new int[n][n]; // adjacency matrix

        Random rand = new Random();
        int i = 0;
        while (i < n) {
            int j = rand.nextInt(2 * n) + 1;
            if (!s.contains(j)) {
                s.add(j);
                arr.add(j);
                System.out.println("Process " + i + " created with ID: " + i + ", Public: " + j + " Private: " + j);
                nodesList.add(new Node(i, j));
                i++;
            }
        }

        System.out.println("All nodes created successfully.");
        System.out.println("Enter total number of edges: ");
        int e = sc.nextInt();

        for (int index = 0; index < e; index++) {
            System.out.println("Enter blocked process ID and blocking process ID (0 to " + (n - 1) + "):");
            int x = sc.nextInt(); // blocked
            int y = sc.nextInt(); // blocking

            if (x < 0 || x >= n || y < 0 || y >= n) {
                System.out.println("Invalid process IDs.");
                index--;
                continue;
            }

            if (graph[x][y] == 1) {
                System.out.println("Edge already exists.");
                continue;
            }

            graph[x][y] = 1;
            addEdge(nodesList.get(x), nodesList.get(y));
            deadlockDetection(x, y);

        }

        sc.close();
    }

    public static void addEdge(Node blocked, Node blocking) {
        System.out.println("Blocked Process " + blocked.pid + " - Public: " + blocked.u + " Private: " + blocked.v);
        System.out.println("Blocking Process " + blocking.pid + " - Public: " + blocking.u + " Private: " + blocking.v);

        blocked.u = blocked.v = Math.max(blocked.u, blocking.u) + 1;
        System.out.println(
                "Updated Blocked Process " + blocked.pid + " => Public: " + blocked.u + " Private: " + blocked.v);

        transmit(blocked);
    }

    public static void transmit(Node node) {
        for (int i = 0; i < n; i++) {
            if (graph[i][node.pid] == 1) { // if process i is waiting on node
                Node prev = nodesList.get(i);
                if (prev.u < node.u) {
                    System.out.println("Transmitting from Process " + node.pid + " to waiting Process " + prev.pid);
                    prev.u = node.u;
                    System.out.println("After transmission - Process " + prev.pid + " => Public: " + prev.u
                            + " Private: " + prev.v);
                    transmit(prev); // propagate further
                }
            }
        }
    }

    public static void deadlockDetection(int x, int y) {
        if (graph[x][y] == 1) {

            Node blocked = nodesList.get(x);
            Node blocking = nodesList.get(y);

            if (blocked.u == blocked.v &&
                    blocked.u == blocking.u) {
                System.out.println("Deadlock Detected:");
                System.out.println(
                        "Blocked Process: " + blocked.pid + " => Public: " + blocked.u + " Private: " + blocked.v);
                System.out.println(
                        "Blocking Process: " + blocking.pid + " => Public: " + blocking.u + " Private: " + blocking.v);
                System.exit(1);
            }
        }
        System.out.println("No Deadlock Detected.");
    }
}
