package HoRamamurthy;

import java.util.ArrayList;

public class Site extends Thread{
	
	//member variables
	int id;
	int[][] statustable; //status table to understand which process is holding/waiting for which resource
	ArrayList<Process> processes=new ArrayList<Process>(); //arraylist of all processes belonging to the site
	ArrayList<Resource> resources=new ArrayList<Resource>(); //arraylist of all resources belonging to the site
	
	//constructor
	public Site(int id) {
		this.id=id;
		statustable=new int[HoRamamurthyTest.numprocesses][HoRamamurthyTest.numresources]; //constructing a numprocesses*numresources status table
		//initializing the status table to 0
		for(int i=0;i<HoRamamurthyTest.numprocesses;i++) {
			for(int j=0;j<HoRamamurthyTest.numresources;j++) {
				statustable[i][j]=0;
			}
		}
		CentralServer.sites.add(this); //adding its own reference to the central server so that status tables can be read by the site
	}
	
	//starting site's lifecycle
	public void run() {
		
		//start each member process's lifecycle
		for(Process process: processes) {
			process.start();
		}
	}
	
	//function to add resource to the site
	void addResource(Resource resource) {
		System.out.println("Resource R"+resource.id+" is added to Site S"+this.id);
		resources.add(resource); //add reference of the resource to the resources arraylist of this site
		resource.site=this; //setting the value of the site variable of the resource to this site.
	}
	
	//function to add process to the site
	void addProcess(Process process) {
		System.out.println("Process P"+process.id+" is added to Site S"+this.id);
		processes.add(process); //add reference of the process to the processes arraylist of this site
		process.site=this; //setting the value of the site variable of the process to this site.
	}
}
