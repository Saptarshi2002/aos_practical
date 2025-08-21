package initiator;

// Class representing a node in the adjacency list
public class Node {
	int dest; // Index of destination vertex
	Node next; // Pointer to the next node

	// Constructor to initialize destination
	public Node(int dest) {
		this.dest = dest;
		this.next = null;
	}
}
