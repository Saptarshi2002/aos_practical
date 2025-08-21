package ring;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Node implements Runnable {
    Integer processId;
    Queue<Node> requestQueue = new LinkedList<>();
    boolean hasToken;
    String csStatus;
    Node neighbor;

    public Node(Integer processId, boolean hasToken, String csStatus) {
        this.processId = processId;
        this.hasToken = hasToken;
        this.csStatus = csStatus;
    }

    @Override
    public void run() {
        if (this.hasToken) {
            this.enterCriticalSection();
        } else {
            try {
                Thread.sleep(new Random().nextInt(5000));
                this.requestAccess();
            } catch (Exception e) {
                System.out.println("Error for process " + this.processId + ": " + e.getMessage());
            }
        }
    }

    public void requestAccess() {
        if (this.hasToken) {
            this.enterCriticalSection();
        } else {
            System.out.println("# Process " + this.processId + " is requesting access to the critical section.");
            this.neighbor.handleRequest(this);
        }
    }

    public void handleRequest(Node requester) {
        synchronized (this) {
            if (this.hasToken) {
                System.out.println("Process " + this.processId + " received a request from " + requester.processId +
                        " and holds the token. Added to queue.");
                if (this != requester && !this.requestQueue.contains(requester)) {
                    this.requestQueue.offer(requester);
                }

                if (this.csStatus.equals("unlocked") && !this.requestQueue.isEmpty()) {
                    displayQueue();
                    Node next = this.requestQueue.poll();
                    System.out.println("Process " + this.processId + " is passing token to: " + next.processId);
                    this.hasToken = false;
                    this.neighbor.handleToken(next, new LinkedList<>(this.requestQueue));
                    this.requestQueue.clear();
                }
            } else {
                System.out.println("Process " + this.processId + " received a request from " + requester.processId +
                        " but doesn't hold the token. Forwarding request.");
                this.neighbor.handleRequest(requester);
            }
        }
    }

    public void handleToken(Node receiver, Queue<Node> incomingQueue) {
        if (this == receiver) {
            System.out.println("Process " + this.processId + " received the token.");
            this.hasToken = true;
            this.requestQueue.clear();
            this.requestQueue.addAll(incomingQueue);
            incomingQueue.clear();
            this.enterCriticalSection();
        } else {
            System.out.println("Process " + this.processId + " is forwarding token for " + receiver.processId);
            this.neighbor.handleToken(receiver, incomingQueue);
        }
    }

    public void enterCriticalSection() {
        try {
            System.out.println(">> Process " + this.processId + " is entering the critical section.");
            this.csStatus = "locked";
            Thread.sleep(2000);
            this.exitCriticalSection();
        } catch (Exception e) {
            System.out.println("Error while entering CS for process " + this.processId + ": " + e.getMessage());
        }
    }

    public void exitCriticalSection() {
        System.out.println("<< Process " + this.processId + " is exiting the critical section.");
        this.csStatus = "unlocked";
        if (!this.requestQueue.isEmpty()) {
            displayQueue();
            Node next = this.requestQueue.poll();
            System.out.println("Process " + this.processId + " is transferring token to: " + next.processId);
            this.hasToken = false;
            this.neighbor.handleToken(next, new LinkedList<>(this.requestQueue));
            this.requestQueue.clear();
        }
    }

    private void displayQueue() {
        System.out.print("Queue at process " + this.processId + ": ");
        System.out.print("[");
        boolean first = true;
        for (Node n : requestQueue) {
            if (!first) System.out.print(", ");
            System.out.print(n.processId);
            first = false;
        }
        System.out.println("]");
    }
}