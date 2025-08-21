package chandylamport;

import java.util.ArrayList;
import java.util.Random;

public class Node extends Thread {
	int id;
	ArrayList<Node> outgoingedges = new ArrayList<Node>();
	ArrayList<Integer> incomingedges = new ArrayList<Integer>();
	ArrayList<Message> sentmessages = new ArrayList<Message>();
	ArrayList<Message> receivedmessages = new ArrayList<Message>();
	ArrayList<ArrayList<Message>> incomingchannel = new ArrayList<ArrayList<Message>>();
	ArrayList<Integer> trackchannel = new ArrayList<Integer>();
	boolean hasRecorded;
	boolean initiator;
	boolean terminated;
	int numnodes;
	int clock = 0;

	public Node(int id, int numnodes) {
		this.id = id;
		hasRecorded = false;
		this.numnodes = numnodes;
	}

	int max(int num1, int num2) {
		if (num1 > num2) {
			return num1;
		}
		return num2;
	}

	@Override
	public void run() {
		for (int i = 0; i < numnodes; i++) {
			incomingchannel.add(new ArrayList<Message>());
		}
		if (initiator) {
			try {
				Thread.sleep(new Random().nextInt(2000));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println("[INIT]      Node " + this.id + " initiating snapshot at clock= " + clock);
			hasRecorded = true;
			System.out.println("[REC]       Initiator Node " + this.id + " recorded its state at clock= " + clock);
			clock++;
			for (Node node : outgoingedges) {
				int sendclock = clock++;
				System.out.println("[MKR-S]     Node " + this.id + " is sending marker to Node " + node.id + " at clock= " + sendclock);
				new Thread(() -> {
					try {
						node.receivemarker(this.id, sendclock);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}).start();
			}
			if (trackchannel.isEmpty()) {
				terminated = true;
				System.out.println("[EXIT]      Node " + this.id + " has terminated at logical clock= " + clock);
				ChandyLamportTest.numterminated.incrementAndGet();
				clock++;
			}
		} else {
			while (!hasRecorded) {
				try {
					Thread.sleep(new Random().nextInt(2000) + 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(outgoingedges.isEmpty()) {
					break;
				}
				int index = new Random().nextInt(outgoingedges.size());
				if (hasRecorded) {
					break;
				}
				System.out.println("[MSG-S]     Node " + this.id + " sending message to Node " + outgoingedges.get(index).id + " at clock= " + clock);
				clock++;
				sentmessages.add(new Message(outgoingedges.get(index).id, clock - 1, 0));
				try {
					outgoingedges.get(index).receivemessage(this.id, clock - 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	void receivemarker(int id, int timestamp) throws InterruptedException {
		Thread.sleep(new Random().nextInt(4000));
		clock = max(clock, timestamp + 1);
		synchronized (this) {
			if (hasRecorded) {
				System.out.println("[MKR-R(1+)] Marker received on Node " + this.id + " from Node " + id + " at clock= " + clock + " and stopped recording messages from channel " + id + "-" + this.id);
				trackchannel.remove(0);
				if (trackchannel.isEmpty()) {
					terminated = true;
					System.out.println("[EXIT]      Node " + this.id + " has terminated at clock= " + clock);
					ChandyLamportTest.numterminated.incrementAndGet();
				}
			} else {
				System.out.println("[MKR-R(1)]  Marker received on Node " + this.id + " for first time from Node " + id + " at clock= " + clock + " and recorded state, and recorded state of channel " + id + "-" + this.id + " as empty");
				hasRecorded = true;
				for (Node node : outgoingedges) {
					int sendclock = clock++;
					System.out.println("[MKR-S]     Node " + this.id + " is sending marker to Node " + node.id + " at clock= " + sendclock);
					new Thread(() -> {
						try {
							node.receivemarker(this.id, sendclock);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}).start();
				}
				trackchannel.remove(0);
				if (trackchannel.isEmpty()) {
					terminated = true;
					System.out.println("[EXIT]      Node " + this.id + " has terminated at clock= " + clock);
					ChandyLamportTest.numterminated.incrementAndGet();
				}
			}
		}
	}

	void receivemessage(int id, int timestamp) throws InterruptedException {
		synchronized (this) {
			Thread.sleep(new Random().nextInt(2000));
			clock = max(clock, timestamp+1);
			if (!hasRecorded) {
				System.out.println("[MSG-R]     Node " + this.id + " received message from Node " + id + "(" + timestamp + ") at clock= " + clock);
				clock++;
				receivedmessages.add(new Message(id, timestamp, clock - 1));
			} else {
				System.out.println("[MSG-TST]   Message from Node " + id + "(" + timestamp + ") to Node " + this.id + " has been recorded in the state of the channel " + id + "-" + this.id + " at clock= " + clock);
				clock++;
				incomingchannel.get(id).add(new Message(id, timestamp, clock - 1));
			}
		}
	}
}
