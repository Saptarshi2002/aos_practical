package HoRamamurthy;

import java.util.ArrayList;
import java.util.Random;

public class Process extends Thread{
	
	//member variables
	int id;
	Site site=null; //reference of the site it belongs to
	ArrayList<Resource> holding=new ArrayList<Resource>(); //arraylist of resouces it is holding
	
	//constructor
	public Process(int id) {
		this.id=id;
	}
	
	//start of process lifecycle
	public void run(){
		
		//temporary arraylist to keep track of resources it requested once so as not to request same resource twice
		ArrayList<Integer> templist=new ArrayList<Integer>();
		
		//each process requests a maximum of 3 random resources
		for(int i=0;i<HoRamamurthyTest.maxrequests;i++) {
			
			//random delay before requesting a resource
			try {
				Thread.sleep(new Random().nextInt(400));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//generating a random resource index for requesting
			int randomnumber=new Random().nextInt(HoRamamurthyTest.numresources);
			
			//if not already requested the resource, it will request for that resource
			if(!templist.contains(randomnumber)) {
				this.requestResource(HoRamamurthyTest.resources.get(randomnumber)); //requesting resource
				templist.add(randomnumber); //adds index to templist for redundancy handling
			}
		}
	}
	
	//function to request a resource
	void requestResource(Resource resource){
		synchronized(resource) { //synchronized so multiple resources do not request this resource at the same time leading to race condition
			System.out.println("Process P"+this.id+" is requesting Resource R"+resource.id);
			
			//if resource is not held by any process, then acquire resource
			if(resource.holder==null) {
				System.out.println("Process P"+this.id+" acquired Resource R"+resource.id);
				holding.add(resource); //add resource to holding arraylist
				resource.holder=this; //set resource's holder to itself
				site.statustable[this.id][resource.id]=1; //update its own status table
			}
			
			//if resource is already held by some process, wait for the resource
			else {
				System.out.println("Process P"+this.id+" is waiting for Resource R"+resource.id+" as Process P"+resource.holder.id+" is holding it.");
				resource.waitingfor.add(this); //add resource to waiting arraylist
				site.statustable[this.id][resource.id]=-1; //update its own status table
			}
		}
	}
	
	//function to release a resource
	void releaseResource(Resource resource){
		synchronized(resource) { //synchronized to avoid race condition
			System.out.println("Process P"+this.id+" is releasing Resource R"+resource.id);
			if(!resource.waitingfor.isEmpty()) { //if some process(es) is(are) waiting for this resource, set holder to first resource in the waiting queue
				Process transferTo=resource.waitingfor.poll(); //extract the first waiting process in queue
				resource.holder=transferTo; //assign the resource to that process 
				System.out.println("Process P"+transferTo.id+" acquired Resource R"+resource.id);
				transferTo.site.statustable[transferTo.id][resource.id]=1; //update the site's status table
			}
			else { //if no process is waiting for this resource, set holder to null
				resource.holder=null;
			}
		}
		site.statustable[this.id][resource.id]=0; // update the site's status table
	}
}
