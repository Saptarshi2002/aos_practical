package mitchellmerrit;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MitchellMerritTest {
	static int[][] adjmatrix;
	static int numnodes;
	static ArrayList<Node> nodes=new ArrayList<Node>();
	static int max(int num1,int num2) {
		if(num1>num2) {
			return num1;
		}
		return num2;
	}
	public static void main(String args[]) {
		Scanner sc=new Scanner(System.in);
		ArrayList<Integer> privatevalues=new ArrayList<Integer>();
		int randomvalue=0;
		System.out.print("Enter number of nodes: ");
		numnodes=sc.nextInt();
		adjmatrix=new int[numnodes][numnodes];
		for(int i=0;i<numnodes;i++) {
			for(int j=0;j<numnodes;j++) {
				adjmatrix[i][j]=0;
			}
		}
		for(int i=0;i<numnodes;i++) {
			do{
				randomvalue=new Random().nextInt(25);
			}
			while(privatevalues.contains(randomvalue));
			privatevalues.add(randomvalue);
			nodes.add(new Node(i,randomvalue,randomvalue));
			
			System.out.println("Node "+i+" succesfully created with: private-> "+randomvalue+", public-> "+randomvalue);
		}
		int numedges;
		System.out.print("Enter number of edges: ");
		numedges=sc.nextInt();
		for(int i=0;i<numedges;i++) {
			System.out.println("\nEdge "+i+": Enter id of blocked process and id of blocking process: ");
			int blocked=sc.nextInt();
			int blocking=sc.nextInt();
			
			adjmatrix[blocked][blocking]=1;
			
			Node blockedprocess=nodes.get(blocked);
			Node blockingprocess=nodes.get(blocking);
			
			System.out.println("Blocked Process: Node "+blockedprocess.id+" Private: "+blockedprocess.v+" Public: "+blockedprocess.u);
			System.out.println("Blocking Process: Node "+blockingprocess.id+" Private: "+blockingprocess.v+" Public: "+blockingprocess.u);
			
			int updatedvalue=max(blockedprocess.u,blockingprocess.u)+1;
			
			blockedprocess.u=blockedprocess.v=updatedvalue;
			System.out.println("==>New Updated Blocked Process: Node "+blockedprocess.id+" Private: "+blockedprocess.v+" Public: "+blockedprocess.u);
			
			transmit(blockedprocess);
			int checkdeadlock=deadlockdetection(blockedprocess,blockingprocess);
			if(checkdeadlock==1) {
				return;
			}
		}
	}
	public static void transmit(Node blockedprocess) {
		for(int i=0;i<numnodes;i++) {
			if(adjmatrix[i][blockedprocess.id]==1) {
				Node node=nodes.get(i);
				if(node.u<blockedprocess.u) {
					node.u=blockedprocess.u;
					System.out.println("Transmiting from node "+blockedprocess.id+" to node "+node.id);
					System.out.println("After Transmission: Node "+node.id+" Private: "+node.v+" Public: "+node.u);
					transmit(node);
				}
			}
		}
	}
	static int deadlockdetection(Node blockedprocess,Node blockingprocess) {
		System.out.println("CHECKING FOR DEADLOCK: ");
		System.out.println("Blocked Process: Node "+blockedprocess.id+" Private: "+blockedprocess.v+" Public: "+blockedprocess.u);
		System.out.println("Blocking Process: Node "+blockingprocess.id+" Private: "+blockingprocess.v+" Public: "+blockingprocess.u);
		if(blockedprocess.u==blockingprocess.u && blockedprocess.u==blockedprocess.v) {
			System.out.println("DEADLOCK DETECTED!!!!");
			return 1;
			
		}
		else {
			System.out.println("NO DEADLOCK");
			return 0;
		}
	}
}
