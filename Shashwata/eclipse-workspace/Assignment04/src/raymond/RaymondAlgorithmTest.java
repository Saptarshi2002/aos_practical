package raymond;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class RaymondAlgorithmTest {
	public static void main(String args[]) throws FileNotFoundException {
		ArrayList<Node> nodes=new ArrayList<Node>();
		String directedArrow[]=new String[100];
		
		Scanner sc=new Scanner(new File("C:\\Users\\Admin\\eclipse-workspace\\Assignment04\\src\\raymond\\input.txt"));
		int numNodes=Integer.parseInt(sc.nextLine());
		int tokenHolderIndex=Integer.parseInt(sc.nextLine());
		for(int i=0;i<numNodes;i++) {
			nodes.add(new Node(i));
		}
		while(sc.hasNextLine()) {
			directedArrow=sc.nextLine().split(" ");
			nodes.get(Integer.parseInt(directedArrow[1])).holder=nodes.get(Integer.parseInt(directedArrow[0]));
			nodes.get(tokenHolderIndex).hasToken=true;
		}
		
		sc.close();

        for (Node node : nodes) {
            node.start();
        }
	}
	
}