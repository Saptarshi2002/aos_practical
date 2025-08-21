package HoRamamurthy;

import java.util.ArrayList;
import java.util.Random;

public class CentralServer extends Thread {
	
	//member variables
	static ArrayList<Site> sites=new ArrayList<Site>(); //arraylist of all sites in the system
	int[][] initialWFG; //adjacency matrix to act as the initial Wait For Graph
	int[][] phaseTwoWFG; //adjacency matrix to act as the phase 2 Wait For Graph
	
	//constructor
	public CentralServer() {
		initialWFG=new int[HoRamamurthyTest.numprocesses][HoRamamurthyTest.numprocesses]; //n*n adjacency matrix 
		phaseTwoWFG=new int[HoRamamurthyTest.numprocesses][HoRamamurthyTest.numprocesses]; //n*n adjacency matrix 
	}
	
	//starting the thread
	public void run() {
		
		//starting deadlock detection process
		startDeadlockDetection();
	}
	
	//function to detect deadlock
	void startDeadlockDetection() {
		System.out.println("\nStarting Deadlock Detection: ");
		System.out.println("\nInitiating Phase 1:");
		
		//displaying status tables of the sites for phase 1
		for(Site site: sites) {
			System.out.println("\nStatus Table for Site S"+site.id);
			System.out.print("    "); //beautification
			for(int i=0;i<HoRamamurthyTest.numresources;i++) {
				System.out.print("R"+i+"\t"); //beautification
			}
			System.out.println();
			for(Process process: site.processes) { //since the status table is actually n*n, only dispay the rows for processes which are part of this site
				System.out.print("P"+process.id+"  "); //beautification
				
				//print the entire row corresponding to a process
				for(int i=0;i<HoRamamurthyTest.numresources;i++) {
					System.out.print(site.statustable[process.id][i]+"\t");
				}
				System.out.println();
			}
		}
		
		//traversing the status tables of the sites to create initial WFG
		for(Site site: sites) {
			for(int i=0;i<HoRamamurthyTest.numprocesses;i++) {
				for(int j=0;j<HoRamamurthyTest.numresources;j++) {
					if(site.statustable[i][j]==-1) { //-1 in the status table indicates that the resource is actually held by some process
						//the value i indicates the id of the blocked process
						initialWFG[i][HoRamamurthyTest.resources.get(j).holder.id]=1;  //<<resources.get(j).holder.id>> finds the id of the node holding the resource which is actually blocking this process
					}
				}
			}
		}
		
		//displaying initial WFG
		System.out.println("\nThe initial WFG constructed is: ");
		System.out.print("   "); //beautification
		for(int i=0;i<HoRamamurthyTest.numprocesses;i++) {
			System.out.print("P"+i+" "); //beautification
		}
		System.out.println();
		for(int i=0;i<HoRamamurthyTest.numprocesses;i++) {
			System.out.print("P"+i+"  "); //beautification
			
			//printing each row of the wfg
			for(int j=0;j<HoRamamurthyTest.numprocesses;j++) {
				System.out.print(initialWFG[i][j]+"  ");
			}
			System.out.println();
		}
		
		//checking for cycles in the initial WFG
		boolean cycle=checkforcycles(initialWFG);
		
		//this is for releasing resources after initial WFG has been constructed just to get a better understanding of the phantom deadlock scenario.
		//picks out a random process to release resource
		Process exampleProcess= HoRamamurthyTest.processes.get(new Random().nextInt(HoRamamurthyTest.numprocesses)); 
		
		//calculates the number of resources it is holding
		int numresourcesholding=exampleProcess.holding.size();
		
		//if it is holding at least 1 resource, release a random resource
		if(numresourcesholding>0) {
			System.out.println("");
			exampleProcess.releaseResource(exampleProcess.holding.get(new Random().nextInt(numresourcesholding)));
		}
		
		//cycle not found: no deadlock
		if(!cycle) {
			System.out.println("\nNo cycle detected in Phase 1: No Deadlock");
		}
		
		//cycle found: initiate phase 2
		else {
			System.out.println("\nInitiating Phase 2:");
			
			//displaying status tables of the sites for phase 2
			for(Site site: sites) {
				System.out.println("\nStatus Table for Site S"+site.id);
				System.out.print("    "); //beautification
				for(int i=0;i<HoRamamurthyTest.numresources;i++) {
					System.out.print("R"+i+"\t"); //beautification
				}
				System.out.println();
				for(Process process: site.processes) { //since the status table is actually n*n, only dispay the rows for processes which are part of this site
					System.out.print("P"+process.id+"  "); //beautification
					
					//print the entire row corresponding to a process
					for(int i=0;i<HoRamamurthyTest.numresources;i++) {
						System.out.print(site.statustable[process.id][i]+"\t");
					}
					System.out.println();
				}
			}
			
			//traversing the status tables of the sites to create phase 2 WFG
			for(Site site: sites) {
				for(int i=0;i<HoRamamurthyTest.numprocesses;i++) {
					for(int j=0;j<HoRamamurthyTest.numresources;j++) {
						if(site.statustable[i][j]==-1 && initialWFG[i][HoRamamurthyTest.resources.get(j).holder.id]==1) { //-1 in the status table indicates that the resource is actually held by some process
							//the value i indicates the id of the blocked process
							//also the extra condition is given so that the wfg is only created using common transactions in both phase
							phaseTwoWFG[i][HoRamamurthyTest.resources.get(j).holder.id]=1;  //<<resources.get(j).holder.id>> finds the id of the node holding the resource which is actually blocking this process
						}
					}
				}
			}
			
			// displaying new wfg
			System.out.println("\nThe phase two WFG constructed is: ");
			System.out.print("   "); //beautification
			for(int i=0;i<HoRamamurthyTest.numprocesses;i++) {
				System.out.print("P"+i+" "); //beautification
			}
			System.out.println();
			for(int i=0;i<HoRamamurthyTest.numprocesses;i++) {
				System.out.print("P"+i+"  "); //beautification
				
				//printing each row of the wfg
				for(int j=0;j<HoRamamurthyTest.numprocesses;j++) {
					System.out.print(phaseTwoWFG[i][j]+"  ");
				}
				System.out.println();
			}
			
			//checking for cycles again in the pahse 2 WFG
			cycle=checkforcycles(phaseTwoWFG);
			
			//if cycle exists, deadlock is detected
			if(cycle) {
				System.out.println("\nCycle still exists!! Deadlock Detected!!!!!!");
			}
			
			//if cycle does not exist now, it was a phantom deadlock
			else {
				System.out.println("\nCycle exists no more!! No Deadlock!!!!!!");

			}
		}
		
	}
	
	
	//function to check for cycles in the WFG
	boolean checkforcycles(int[][] WFG) {
		
		// boolean array to keep track of visited nodes during DFS
		boolean[] visited = new boolean[HoRamamurthyTest.numprocesses];
		
		// boolean array to keep track of the recursion stack (helps in identifying back edges)
		boolean[] recStack = new boolean[HoRamamurthyTest.numprocesses];
		
		// array list to keep track of the current path for printing the cycle
		ArrayList<Integer> path = new ArrayList<>();

		// iterate through all processes (nodes)
		for (int i = 0; i < HoRamamurthyTest.numprocesses; i++) {
		    if (!visited[i]) {
		        // if node not yet visited, perform DFS
		        if (dfsCycleDetect(i, visited, recStack, path, WFG)) {
		            return true; // cycle found
		        }
		    }
		}

		// if no cycle was found in the entire graph
		System.out.println("\nNo cycle found in WFG\n");
		return false;
	}

	//recursive function to perform DFS and detect cycle in the graph
	boolean dfsCycleDetect(int current, boolean[] visited, boolean[] recStack, ArrayList<Integer> path, int[][] WFG) {
		// mark current node as visited
		visited[current] = true;

		// mark current node as part of the current recursion stack
		recStack[current] = true;

		// add current node to the path
		path.add(current);

		// iterate through all possible adjacent nodes
		for (int i = 0; i < HoRamamurthyTest.numprocesses; i++) {
			
			// check if there is a directed edge from current to i
		    if (WFG[current][i] == 1) {
		        
		    	// if i is not visited, perform DFS recursively
		        if (!visited[i]) {
		            if (dfsCycleDetect(i, visited, recStack, path, WFG)) {
		                return true; // cycle detected in recursion
		            }
		        }
		        
		        // if i is in recursion stack, a back edge is found → cycle exists
		        else if (recStack[i]) {
		            // Cycle found
		            System.out.println("\nCycle Detected: ");
		            
		            // print the cycle path
		            int index = path.indexOf(i); // find the starting index of the cycle in the path
		            for (int j = index; j < path.size(); j++) {
		                System.out.print("P" + path.get(j) + " -> ");
		            }
		            System.out.println("P" + i); // complete the cycle
		            return true;
		        }
		    }
		}

		// backtrack: remove the current node from recursion stack and path
		recStack[current] = false;
		path.remove(path.size() - 1);
		return false; // no cycle found through this path
	}


}
