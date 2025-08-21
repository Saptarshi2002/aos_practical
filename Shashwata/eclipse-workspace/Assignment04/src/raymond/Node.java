package raymond;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class Node extends Thread{
	int id;
	Node holder=null;
	boolean hasToken=false;
	boolean inCS=false;
	private Queue<Node> queue=new LinkedList<>();
	public Node(int id) {
		this.id=id;
		this.holder=null;
	}
	
	@Override
	public void run(){
		if(hasToken) {
			try {
				enterCriticalSection();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				Thread.sleep(new Random().nextInt(5000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("++ Node "+id+" is requesting CS");
			requestCriticalSection();
		}
	}
	
	public void requestCriticalSection() {
		if(hasToken) {
			try {
				enterCriticalSection();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			queue.offer(this);
			if(!holder.queue.contains(this)) {
				holder.recieveRequest(this);
			}
		}
	}
	
	public void recieveRequest(Node requester) {
		synchronized(this) {
			System.out.println("[REQ]  Node "+id+" received request from node "+requester.id+" and added it to queue");
			queue.offer(requester);
			if(!hasToken) {
				System.out.println("[FWD]  Node "+id+" forwarding request to holder");
				if(!holder.queue.contains(this)) {
					holder.recieveRequest(this);
				}
			}
			if(hasToken && !inCS) {
				Node sendTo=queue.poll();
				holder=sendTo;
				sendTo.holder=null;
				hasToken=false;
				System.out.println("[TKN]  Node "+id+" has token so forwarding token to node "+holder.id);
				holder.handleToken();
			}
		}
	}
	
	public void handleToken() {
		Node sendTo=queue.poll();
		if(sendTo==this) {
			hasToken=true;
			try {
				enterCriticalSection();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			holder=sendTo;
			sendTo.holder=null;
			System.out.println("[TKN]  Node "+id+" is forwarding token to node "+sendTo.id);
			holder.handleToken();
			if(!queue.isEmpty()) {
				holder.recieveRequest(this);
			}
		}
	}
	
	public void enterCriticalSection() throws InterruptedException {
		inCS=true;
		System.out.println(">>>Node "+this.id+" is entering CS");
		Thread.sleep(new Random().nextInt(5000)+1000);
		exitCriticalSection();
	}
	
	public void exitCriticalSection() {
		System.out.println("<<<Node "+id+" is exiting CS");
		inCS=false;
		if(!queue.isEmpty()) {
			Node sendTo=queue.poll();
			hasToken=false;
			System.out.println("[FWD]  Node "+id+" is forwarding token to node "+sendTo.id);
			holder=sendTo;
			sendTo.holder=null;
			sendTo.handleToken();
			if(!queue.isEmpty()) {
				holder.recieveRequest(this);
			}
		}
	}
}
