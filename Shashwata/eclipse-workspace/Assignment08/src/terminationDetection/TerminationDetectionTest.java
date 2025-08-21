package terminationDetection;

import java.util.ArrayList;

public class TerminationDetectionTest {
	static ArrayList<Node> nodes=new ArrayList<Node>();
	static CentralServer server=new CentralServer();
	static int numnodes=5;
	static int numrequests=5;
	
	public static void main(String args[]) throws InterruptedException {
		for(int i=0;i<5;i++) {
			nodes.add(new Node(i));
			System.out.println("Node "+i+" created");
		}
		for(Node node: nodes) {
			node.start();
		}
	}
}
