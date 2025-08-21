package ricart;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RicartAgrawal extends Thread {
    private static final int TOTAL_NODES = 6;
    private static final RicartAgrawal[] nodes = new RicartAgrawal[TOTAL_NODES];
    private static final Random random = new Random();

    private static final Object globalLock = new Object();
    private final Object nodeLock = new Object();

    private static final int MAX_TOTAL_CS_EXITS = 10;
    private static final AtomicInteger totalCsExits = new AtomicInteger(0);
    private static volatile boolean stopRequested = false;

    private final int id;
    private int clock = 0;
    private boolean requestingCS = false;
    private boolean inCS = false;

    private final Set<Integer> repliesPending = new HashSet<>();
    private final Queue<Request> deferredRequests = new LinkedList<>();

    private static class Request {
        int fromId, timestamp;

        Request(int fromId, int timestamp) {
            this.fromId = fromId;
            this.timestamp = timestamp;
        }
    }

    public RicartAgrawal(int id) {
        this.id = id;
        nodes[id] = this;
    }

    @Override
    public void run() {
        try {
            while (!stopRequested) {
                Thread.sleep(random.nextInt(5000)); // Random delay before requesting CS

                if (stopRequested) break; // Check again before entering CS

                enterCriticalSection();
            }
            System.out.println("Node " + id + " stopping gracefully.");
        } catch (InterruptedException e) {
            System.out.println("Node " + id + " interrupted.");
        }
    }

    public void enterCriticalSection() throws InterruptedException {
        int requestTimestamp;
        synchronized (nodeLock) {
            synchronized (globalLock) {
                clock++;
                requestTimestamp = clock;
                requestingCS = true;
                repliesPending.clear();

                for (int i = 0; i < TOTAL_NODES; i++) {
                    if (i != id) {
                        repliesPending.add(i);
                    }
                }

                // Log intent to request CS BEFORE sending requests
                System.out.println(">>> Node " + id + " REQUESTING CS at timestamp " + requestTimestamp);

                for (int i = 0; i < TOTAL_NODES; i++) {
                    if (i != id) {
                        System.out.println("Node " + id + " sending REQUEST to Node " + i + " at timestamp " + requestTimestamp);
                        nodes[i].receiveRequest(id, requestTimestamp);
                    }
                }
            }

            while (!repliesPending.isEmpty()) {
                nodeLock.wait();
            }

            inCS = true;
            requestingCS = false;
            System.out.println(">>> Node " + id + " ENTERING CRITICAL SECTION");
        }

        Thread.sleep(random.nextInt(2000) + 1000); // Simulate being in CS

        synchronized (globalLock) {
            inCS = false;
            System.out.println("<<< Node " + id + " LEAVING CRITICAL SECTION");

            int currentCount = totalCsExits.incrementAndGet();
            if (currentCount >= MAX_TOTAL_CS_EXITS) {
                stopRequested = true;
            }

            while (!deferredRequests.isEmpty()) {
                Request r = deferredRequests.poll();
                nodes[r.fromId].receiveReply(id);
            }
        }
    }

    public void receiveRequest(int fromId, int timestamp) {
        synchronized (globalLock) {
            clock = Math.max(clock, timestamp) + 1;

            boolean shouldDefer = inCS ||
                (requestingCS && (
                    timestamp < clock ||
                    (timestamp == clock && fromId < id)
                ));

            if (shouldDefer) {
                deferredRequests.add(new Request(fromId, timestamp));
            } else {
                System.out.println("Node " + id + " sending REPLY to Node " + fromId);
                nodes[fromId].receiveReply(id);
            }
        }
    }

    public void receiveReply(int fromId) {
        synchronized (nodeLock) {
            repliesPending.remove(fromId);
            System.out.println("Node " + id + " received REPLY from Node " + fromId);
            if (repliesPending.isEmpty()) {
                nodeLock.notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RicartAgrawal[] threads = new RicartAgrawal[TOTAL_NODES];
        for (int i = 0; i < TOTAL_NODES; i++) {
            threads[i] = new RicartAgrawal(i);
            threads[i].start();
        }

        // Wait for all threads to finish after stopRequested == true
        for (RicartAgrawal t : threads) {
            t.join();
        }

        System.out.println("All nodes have stopped after " + totalCsExits.get() + " total critical section exits.");
    }
}
