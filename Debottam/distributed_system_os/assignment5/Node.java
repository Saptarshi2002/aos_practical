package distributed_system_os.assignment5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Node implements Runnable {
    public int id;
    public String state;
    public int clock;
    public boolean snapshot_started = false;
    public int total_nodes;
    public int[][] graph;
    public Map<Integer, String> channelData = new HashMap<>();
    public ArrayList<Message> transmit = new ArrayList<>();
    public ArrayList<Message> normalMessages  = new ArrayList<>();
    public ArrayList<Message> stateData = new ArrayList<>();

    public Node(int id, int n, int[][] graph) {
        this.id = id;
        this.total_nodes = n;
        this.graph = graph;
    }

    @Override
    public void run() {
        System.out.println("Process " + this.id + " is started");
        int i = 0;
        while (i < 2) {
            try {
                Thread.sleep(500);
                for (int j = 0; j < this.total_nodes; j++) {
                    if (this.id != j && this.graph[this.id][j] == 1) {
                        Message msg = new Message(this.id, " send a normal message to ", j, false);
                        normalMessages.add(msg);
                        Main.sendMessage(msg);
                    }
                }
                i++;
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    public void receiveMessage(Message msg) {
        synchronized (this) {
            if (msg.is_marker && !this.snapshot_started) {

                System.out.println("Marker message received by Process " + this.id +
                        " on channel " + msg.sender_id + "_" + msg.receiver_id
                        + " and it is the first marker message received in this node");

                // this.clock = (int) (Math.random() * 5 + 1);
                // this.state = "Local state is stored and clock: " + this.clock;
                this.state = "Local state is stored";
                this.stateData = this.normalMessages;
                this.snapshot_started = true;

                System.out.println("For process " + this.id + ": " + this.state);
                System.out.println("Channel data between " + msg.sender_id + " and " + msg.receiver_id + " became : φ");
                this.channelData.put(msg.sender_id, "closed");

                for (int i = 0; i < this.total_nodes; i++) {
                    if (this.id != i && this.graph[this.id][i] == 1) {
                        Message marker = new Message(this.id, "This is a marker message", i, true);
                        Main.sendMessage(marker);
                    }
                }

            } else if (msg.is_marker && this.snapshot_started) {

                System.out.println("Marker message received by Process " + this.id +
                        " on channel " + msg.sender_id + "_" + msg.receiver_id);

                System.out.println("Local state is already captured in process: " + this.id + " and now the channel "
                        + msg.sender_id + "_" + msg.receiver_id + " is closed");
                this.channelData.put(msg.sender_id, "closed");

            } else if (!msg.is_marker) {
                System.out.println("Normal message received by Process " + this.id +
                        " on channel " + msg.sender_id + "_" + msg.receiver_id);
                String ch = this.channelData.get(msg.sender_id);
                if(this.snapshot_started) {
                    if( ch == null ) {
                    System.out.println("Local snapshot is already done on node " + this.id + " so this received message is stored as transit message on channel " + msg.sender_id + "_" + msg.receiver_id);
                    this.transmit.add(msg);
                }
                } else {
                    this.normalMessages.add(msg);
                }
            }
        }
    }
}
