package distributed_system_os;
/**
 * @author Debottam Kar
 * @problem Reaching all nodes of a connected Graph
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    public static int counter;

    public static void bfs(int start, int[][] graph, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        visited[start] = true;
        while (!queue.isEmpty()) {
            int p = queue.poll();
            System.out.print(p + "->");
            counter++;
            for (int i = 0; i < visited.length; i++) {
                if (visited[i] != true && graph[p][i] == 1) {
                    visited[i] = true;
                    queue.offer(i);
                }
            }
        }
    }

    public static void dfs(int start, int[][] graph, boolean[] visited) {
        if (visited[start] == true) {
            return;
        }
        visited[start] = true;
        counter++;
        System.out.print(start + "->");
        for (int i = 0; i < visited.length; i++) {
            if (visited[i] == false && graph[start][i] == 1) {
                dfs(i, graph, visited);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Reaching all nodes of a connected Graph!");
        String filePath = "./distributed_system_os/input.txt";
        String data = "";
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                data += scanner.nextLine();
            }
            System.out.println(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] input = data.split(",");
        for (int i = 0; i < input.length; i++) {
            System.out.println(input[i]);
        }
        // System.out.println("Enter the number of vertices in the graph: ");
        // Scanner sc = new Scanner(System.in);
        // int n = sc.nextInt();
        int n = Integer.parseInt(input[0]);
        int[][] graph = new int[n][n];
        boolean[] visited = new boolean[n];
        for (int i = 0; i < graph.length; i++) {
            visited[i] = false;
            for (int j = 0; j < graph[i].length; j++) {
                graph[i][j] = 0;
            }
        }
        // System.out.println("Enter the no of edges: ");
        // int edge = sc.nextInt();
        int edge = Integer.parseInt(input[1]);
        int u, v;
        String[] temp;
        for (int i = 2; i < edge + 2; i++) {
            // System.out.println("Enter the end vertices of the edge: " + (i + 1));
            temp = input[i].split(" ");
            u = Integer.parseInt(temp[0]);
            v = Integer.parseInt(temp[1]);
            graph[u][v] = 1;
        }
        // sc.close();
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                System.out.print(graph[i][j] + " ");
            }
            System.out.println();
        }
        FileWriter writer = new FileWriter("./distributed_system_os/output.txt"); // Creates or overwrites the file
        writer.write("");
        writer.close();
        writer = new FileWriter("./distributed_system_os/output.txt", true);
        /* Using DFS */
        // for (int i = 0; i < n; i++) {
        // counter = 0;
        // dfs(i, graph, visited);
        // if (counter == n) {
        // System.out.print("\n" + true + " " + "Every Vertex can be reached from " +
        // i);
        // String str = "\n" + true + " " + "Every Vertex can be reached from " + i;
        // writer.write(str);
        // }
        // Arrays.fill(visited, false);
        // System.out.println();
        // }
        /* Using BFS */
        for (int i = 0; i < n; i++) {
            counter = 0;
            bfs(i, graph, visited);
            if (counter == n) {
                System.out.print("\n" + true + " " + "Every Vertex can be reached from " + i);
                String str = "\n" + true + " " + "Every Vertex can be reached from " + i;
                writer.write(str);
            }
            Arrays.fill(visited, false);
            System.out.println();
        }
        writer.close();
    }
}