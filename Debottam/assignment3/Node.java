import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Node implements Runnable {
    Integer p_id;
    Queue<Node> p_queue = new LinkedList<>();
    boolean p_hold;
    String in_cs;
    Node next_hop;

    public Node(Integer p_id, boolean p_hold, String in_cs) {
        this.p_id = p_id;
        this.p_hold = p_hold;
        this.in_cs = in_cs;
    }

    @Override
    public void run() {
        // System.out.println("process_id: " + this.p_id + " p_hold: " + this.p_hold + "
        // Next-hop: " + this.next_hop.p_id);
        if (this.p_hold) {
            this.enterCS();
        } else {
            try {
                Thread.sleep(new Random().nextInt(9000));
                // Requests for critical section
                this.requestCS();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Function for Request the next hop/node for critical section
    public void requestCS() {
        if (this.p_hold) {
            this.enterCS();
        } else {
            System.out.println(this.p_id + " Requests for critical section.");
            this.next_hop.receiveReq(this);
        }
    }

    // Receive request for Critical Section
    public void receiveReq(Node node) {
        synchronized (this) {
            if (this.p_hold) {
                // Received for critical section and it is p_hold now
                System.out.println(this.p_id
                        + " Received request for critical section from " + node.p_id
                        + " and it is p_hold and stores in the local queue");
                if (this != node && !this.p_queue.contains(node)) {
                    this.p_queue.offer(node);
                }
                if (this.in_cs.equals("unlocked")) {
                    if (!this.p_queue.isEmpty()) {
                        System.out.println("The local queue  for " + this.p_id + " is:");
                        for (Node n : this.p_queue) {
                            System.out.println(n.p_id);
                        }
                        Node nd = this.p_queue.poll();
                        System.out.println(this.p_id + " Forwards the token for: " + nd.p_id + " to the next hop");
                        this.p_hold = false;
                        this.next_hop.receiveResponse(nd, new LinkedList<>(this.p_queue));
                        this.p_queue.clear();
                    }
                }
            } else {
                // Received request but it is not p_hold so forwarding the request to the next
                // hop
                System.out.println(
                        this.p_id + " Received request from " + node.p_id
                                + " but it is not p_hold so forwarding the request to the next hop");
                this.next_hop.receiveReq(node);
            }
        }
    }

    // Receive Response from previous hop/node
    public void receiveResponse(Node node, Queue<Node> p_queue) {
        // If the response reaches the correct/destination node
        if (this == node) {

            // Makes itself the current p_hold
            System.out.println(this.p_id + " received the token.");
            this.p_hold = true;

            // Modifies the queue with the Sender node's queue
            this.p_queue.clear();
            for (Node n : p_queue) {
                this.p_queue.offer(n);
            }
            p_queue.clear();
            this.enterCS();
        } else {
            System.out.println(this.p_id + " Forwards the token which is came for " + node.p_id);
            this.next_hop.receiveResponse(node, p_queue);
        }
    }

    // Enter critical section
    public void enterCS() {
        try {
            System.out.println(this.p_id + " Enters critical section.");
            this.in_cs = "locked";
            Thread.sleep(2000);
            this.exitCS();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Exit critical section
    public void exitCS() {
        System.out.println(this.p_id + " Exists critical section.");
        this.in_cs = "unlocked";
        if (!this.p_queue.isEmpty()) {
            System.out.println("The local queue  for " + this.p_id + " is:");
            for (Node n : this.p_queue) {
                System.out.println(n.p_id);
            }
            Node node = this.p_queue.poll();
            System.out.println(this.p_id + " Forwards the token for: " + node.p_id + " to the next hop");
            this.p_hold = false;
            this.next_hop.receiveResponse(node, new LinkedList<>(this.p_queue));
            this.p_queue.clear();
        }
    }
}