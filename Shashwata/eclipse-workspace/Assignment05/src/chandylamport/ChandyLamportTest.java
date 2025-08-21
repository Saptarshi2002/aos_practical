package chandylamport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class ChandyLamportTest {
	
	public static AtomicInteger numterminated = new AtomicInteger(0);
	
	public int findFirstEligibleInitiator(ArrayList<Node> nodes) {
	    int totalNodes = nodes.size();

	    for (int i = 0; i < totalNodes; i++) {
	        boolean[] visited = new boolean[totalNodes];
	        dfsUtil(nodes, i, visited);

	        boolean canReachAll = true;
	        for (boolean v : visited) {
	            if (!v) {
	                canReachAll = false;
	                break;
	            }
	        }

	        if (canReachAll) {
	            return i; // node i can reach all nodes
	        }
	    }

	    // No node can reach all others, fallback to node 0
	    return 0;
	}

	private void dfsUtil(ArrayList<Node> nodes, int current, boolean[] visited) {
	    visited[current] = true;
	    for (Node neighbor : nodes.get(current).outgoingedges) {
	        if (!visited[neighbor.id]) {
	            dfsUtil(nodes, neighbor.id, visited);
	        }
	    }
	}

	
	public static void main(String args[]) throws FileNotFoundException, InterruptedException {
		ArrayList<Node> nodes=new ArrayList<Node>();
		String directedArrow[]=new String[100];
		
		Scanner sc=new Scanner(new File("C:\\Users\\Admin\\eclipse-workspace\\Assignment05\\src\\chandylamport\\input.txt"));
		int numnodes=Integer.parseInt(sc.nextLine());
		for(int i=0;i<numnodes;i++) {
			nodes.add(new Node(i,numnodes));
		}
		while(sc.hasNextLine()) {
			directedArrow=sc.nextLine().split(" ");
			nodes.get(Integer.parseInt(directedArrow[0])).outgoingedges.add(nodes.get(Integer.parseInt(directedArrow[1])));
			nodes.get(Integer.parseInt(directedArrow[1])).incomingedges.add(Integer.parseInt(directedArrow[0]));
			nodes.get(Integer.parseInt(directedArrow[1])).trackchannel.add(1);
		}
		int initiator = new ChandyLamportTest().findFirstEligibleInitiator(nodes);
		nodes.get(initiator).initiator = true;
		System.out.println("Chosen initiator node: " + initiator);
		
		for(Node node: nodes) {
			Collections.shuffle(node.outgoingedges);;
		}
		
		for(Node node: nodes) {
			node.start();
		}
		// Wait for all nodes to terminate
		while (numterminated.get() < nodes.size()) {
		    Thread.sleep(500);
		}

		// Print Global State
		System.out.println("\n\nGLOBAL STATE RECORDING:\n");

		for (Node node : nodes) {
		    System.out.println("NODE " + node.id + ":");

		    // Messages Sent
		    System.out.print("MESSAGES SENT TO: ");
		    if (node.sentmessages.isEmpty()) {
		        System.out.println("<empty>");
		    } else {
		        for (Message msg : node.sentmessages) {
		            System.out.print("NODE " + msg.id + "(" + msg.senttimestamp + "), ");
		        }
		        System.out.println();
		    }

		    // Messages Received
		    System.out.print("MESSAGES RECIEVED FROM: ");
		    if (node.receivedmessages.isEmpty()) {
		        System.out.println("<empty>");
		    } else {
		        for (Message msg : node.receivedmessages) {
		            System.out.print("NODE " + msg.id + "(" + msg.senttimestamp + "," + msg.receivedtimestamp + "), ");
		        }
		        System.out.println();
		    }

		    // Messages in transit (channel states)
		    for (int i = 0; i < node.incomingchannel.size(); i++) {
		        ArrayList<Message> chMsgs = node.incomingchannel.get(i);
		        if (!chMsgs.isEmpty()) {
		            System.out.print("CHANNEL " + i + "-" + node.id + ": ");
		            for (Message msg : chMsgs) {
		                System.out.print("MSG(" + msg.id + "," + msg.senttimestamp + ") ");
		            }
		            System.out.println();
		        } else if (node.incomingedges.contains(i)) {
		            System.out.println("CHANNEL " + i + "-" + node.id + ": <empty>");
		        }
		    }

		    System.out.println();
		}

	}
}
