package HoRamamurthy;

import java.util.ArrayList;
import java.util.Scanner;

public class HoRamamurthyTest {
	static int numresources;
	static int numprocesses;
	static int numsites;
	static int maxrequests;
	static ArrayList<Process> processes=new ArrayList<Process>();
	static ArrayList<Resource> resources=new ArrayList<Resource>();
	static ArrayList<Site> sites=new ArrayList<Site>();
	
	public static void main(String args[]) throws InterruptedException {
		
		Scanner sc=new Scanner(System.in); //scanner for dynamic input
		int sitenumber; //variable to input site number to assign resouces and processes
				
		//Creating Processes
		System.out.print("Enter the number of processes: ");
		numprocesses=sc.nextInt();
		for(int i=0;i<numprocesses;i++) {
			processes.add(new Process(i));
			System.out.println("Process P"+i+" created");
		}
		System.out.println("");
		
		//Creating Resources
		System.out.print("Enter the number of resources: ");
		numresources=sc.nextInt();
		System.out.println("");
		for(int i=0;i<numresources;i++) {
			resources.add(new Resource(i));
			System.out.println("Resource R"+i+" created");
		}
		System.out.println("");
		
		//Creating Sites
		System.out.print("Enter the number of sites: ");
		numsites=sc.nextInt();
		System.out.println("");
		for(int i=0;i<numsites;i++) {
			sites.add(new Site(i));
			System.out.println("Site S"+i+" created");
		}
		System.out.println("");
		
		//Creating Central Server
		System.out.println("Central Server Created\n");
		CentralServer server=new CentralServer();
		
		//Assigning processes and resources to respective sites statically
//		sites.get(0).addProcess(processes.get(0));
//		sites.get(0).addProcess(processes.get(1));
//		sites.get(0).addResource(resources.get(0));
//		sites.get(0).addResource(resources.get(1));
//		sites.get(1).addProcess(processes.get(2));
//		sites.get(1).addProcess(processes.get(3));
//		sites.get(1).addResource(resources.get(2));
//		sites.get(1).addResource(resources.get(3));
		
		//Assigning processes and resources to respective sites dynamically
		for(Process process: processes) {
			System.out.print("Which site do you want to assign process P"+process.id+" to? ");
			sitenumber=sc.nextInt();
			sites.get(sitenumber).addProcess(process);
		}
		for(Resource resource: resources) {
			System.out.print("Which site do you want to assign resource R"+resource.id+" to? ");
			sitenumber=sc.nextInt();
			sites.get(sitenumber).addResource(resource);
		}
		System.out.println(""); 
		
		//taking input for the maximum number of requests for resources that a process can make
		System.out.print("How many requests for resources can a process make? ");
		maxrequests=sc.nextInt();
		
		//Starting sites' lifecycle
		for(Site site: sites) {
			site.start();
		}
		Thread.sleep(2000);
		
		//starting central server to detect deadlocks
		server.start();
		
		sc.close();
	}
}
