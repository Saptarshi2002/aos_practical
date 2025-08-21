package HoRamamurthy;

import java.util.LinkedList;
import java.util.Queue;

public class Resource {
	
	//member variables
	int id;
	Process holder=null; //reference to the process holding the resource
	Site site=null; //reference to the site it belongs to
	Queue<Process> waitingfor=new LinkedList<Process>(); //queue of processes waiting on it
	
	//constructor
	public Resource(int id) {
		this.id=id;
	}
}
