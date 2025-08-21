package distributed_system_os.assignment5;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static Map<Integer, Node> nodeMap = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the No of nodes: ");
        int n = sc.nextInt();
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph[i][j] = 0;
            }
        }
        System.out.print("Enter the total no of channels: ");
        int e = sc.nextInt();

        // connected unidirectional graph where vertices are nodes/processes and
        // edges are channels
        for (int i = 0; i < e; i++) {
            System.out.println("Enter the end nodes of channel " + (i + 1));
            int v = sc.nextInt();
            int u = sc.nextInt();
            graph[v][u] = 1;
        }
        System.out.println("Adjacency Matrix of the Graph:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(graph[i][j] + " ");
            }
            System.out.println();
        }
        sc.close();

        // Creating the Nodes/prcesses and starting them one by one and also stroring
        // them in a hash map
        for (int i = 0; i < n; i++) {
            Node node = new Node(i, n, graph);
            nodeMap.put(i, node);
            new Thread(node).start();
        }

        // Start snap shot of the choosen node
        Node startNode = nodeMap.get(0);
        startNode.snapshot_started = true;
        startNode.clock = (int) (Math.random() * 5 + 1);
        startNode.state = "Local state is stored and clock: " + startNode.clock;
        System.out.println("For process " + startNode.id + ": " + startNode.state);
        for (int i = 0; i < startNode.total_nodes; i++) {
            if (startNode.id != i && startNode.graph[startNode.id][i] == 1) {
                // sending marker message to all of the out going channels
                Message marker = new Message(startNode.id, "This is a marker message", i, true);
                Main.sendMessage(marker);
            }
        }

        Thread.sleep(6000);
        for (int i = 0; i < n; i++) {
            System.out.println("Process: " + nodeMap.get(i).id);
            System.out.println("Transmitted Messages: " + nodeMap.get(i).transmit);
            System.out.println("State: " + nodeMap.get(i).stateData);
        }
    }

    // Send marker message
    public static void sendMessage(Message msg) {
        // System.out.println(marker.sender_id + " " + marker.message + " " +
        // marker.receiver_id + " " +
        // marker.is_marker);
        Node receiver = nodeMap.get(msg.receiver_id);
        if (receiver != null) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // Simulate message travel time
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                receiver.receiveMessage(msg);
            }).start();

        } else {
            System.out.println("Receiver node not found: " + msg.receiver_id);
        }
    }
}
