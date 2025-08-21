

public class CentralServer{
	double weight=1.0;
	
	void receiveWeight(int id,double weight) {
		synchronized(this) {
			this.weight+=weight;
			System.out.println("Server received weight from Node "+id+" and set weight to "+this.weight);
			if(this.weight==1) {
				System.out.println("Termination Detected!!!!!");
			}
		}
	}
	
	void requestPermission(Node node) {
		synchronized(this) {
			System.out.println("Server received request from Node "+node.id);
			double toGiveWeight=weight/2;
			weight-=toGiveWeight;
			System.out.println("Server sending "+toGiveWeight+" weight to Node "+node.id);
			node.receiveWeight(toGiveWeight);
		}
	}
}
