/**
  @author Debottam Kar
  @problem Token based ring topology  
*/

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Token based Ring topology");
        ArrayList<Node> nodes = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter total number of processes: ");
        int n = sc.nextInt();
        int p_hold_id = new Random().nextInt(n);
        for (int i = 0; i < n; i++) {
            if (i == p_hold_id) {
                nodes.add(new Node(i, true, "locked"));
            } else {
                nodes.add(new Node(i, false, "unlocked"));
            }
        }
        for (int i = 0; i < n; i++) {
            nodes.get(i).next_hop = nodes.get((i + 1) % n);
            Node node = nodes.get(i);
            System.out.println(
                    "process_id: " + node.p_id + " p_hold: " + node.p_hold + " Next-hop: " + node.next_hop.p_id);
        }
        sc.close();
        for (Node node : nodes) {
            new Thread(node).start();
        }
    }
}