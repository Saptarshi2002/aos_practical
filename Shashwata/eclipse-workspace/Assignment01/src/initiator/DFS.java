// Package declaration
package initiator;

import java.util.*;
import java.io.*;

// Main class implementing DFS to find initiators in a graph
public class DFS {

	// Number of vertices and edges in the graph
	int numvertices, numedges;
	
	// List to store vertex names
	ArrayList<String> vertices = new ArrayList<String>();
	
	// List to store initiator nodes (vertices from which all other nodes are reachable)
	ArrayList<String> initiators = new ArrayList<String>();
	
	// Array to track visited nodes during DFS
	int visited[] = new int[100];
	
	// Temporary array to hold vertex names read from the file
	String ver[] = new String[100];
	
	// Temporary array to hold edge data from each line
	String content[] = new String[2];
	
	// Adjacency list representing the graph
	ArrayList<Node> adjlist = new ArrayList<Node>();
	
	// Node used while building the adjacency list
	Node newnode;

	// Method to read graph data from input.txt and build the adjacency list
	void readfile() throws FileNotFoundException {
		// Open the file
		Scanner sc = new Scanner(new File("\\input.txt"));
		
		// Read number of vertices and edges
		numvertices = Integer.parseInt(sc.nextLine());
		numedges = Integer.parseInt(sc.nextLine());

		// Clear any existing data
		vertices = new ArrayList<String>();
		initiators = new ArrayList<String>();
		adjlist = new ArrayList<Node>();

		// Read and store the vertex names
		ver = sc.nextLine().split(" ");
		for (String v : ver) {
			vertices.add(v);
			adjlist.add(null); // Initialize adjacency list with null pointers
		}

		// Read and add edges to the adjacency list
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			content = line.split(" ");
			String src = content[0];
			String dest = content[1];

			// Create a new node for the destination
			newnode = new Node(vertices.indexOf(dest));

			// Insert the node at the beginning of the adjacency list for the source
			newnode.next = adjlist.get(vertices.indexOf(src));
			adjlist.set(vertices.indexOf(src), newnode);
		}

		sc.close(); // Close the file

		// Print the adjacency list for visualization
		int count = 0;
		for (Node p : adjlist) {
			Node n = p;
			System.out.print(vertices.get(count++) + "--->");
			while (n != null) {
				System.out.print(vertices.get(n.dest) + "->");
				n = n.next;
			}
			System.out.print("null\n");
		}
	}

	// Recursive DFS function starting from a given vertex
	void dfsrun(int start) {
		visited[start] = 1; // Mark current node as visited
		Node head = adjlist.get(start);

		// Traverse all adjacent vertices
		while (head != null) {
			if (visited[head.dest] != 1) {
				dfsrun(head.dest); // Recurse for unvisited adjacent nodes
			}
			head = head.next;
		}
	}

	// Function to identify initiator nodes
	void dfs() {
		// Try starting DFS from each node
		for (int i = 0; i < numvertices; i++) {
			int flag = 0;

			// Reset visited array before each DFS run
			for (int j = 0; j < numvertices; j++) {
				visited[j] = 0;
			}

			// Perform DFS from vertex i
			dfsrun(i);

			// Check if all nodes were visited
			for (int j = 0; j < numvertices; j++) {
				if (visited[j] == 0) {
					flag = 1; // Some nodes are not reachable
				}
			}

			// If all nodes are visited, vertex i is an initiator
			if (flag == 0) {
				initiators.add(vertices.get(i));
			}
		}
	}

	// Main method: reads the graph, runs DFS, and prints initiators
	public static void main(String args[]) throws FileNotFoundException {
		DFS a = new DFS();
		a.readfile(); // Build the graph
		a.dfs(); // Find initiators

		// Output the initiators
		System.out.println("Initiators: ");
		for (String s : a.initiators) {
			System.out.print(s + " ");
		}
	}
}
