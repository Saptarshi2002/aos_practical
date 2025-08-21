package distributed_system_os.F_a02.assignment2;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Main implements Runnable{

    enum State { RELEASED, WANTED, HELD };

    private int pid;
    private int n;
    private int clock = 0;
    private ArrayList<Main> processes;
    private int requestTimeStamp = -1;
    private State state = State.RELEASED;
    private int replyCount = 0;
    private Set<Integer> deferredReplies = ConcurrentHashMap.newKeySet();
    public Main(int pid, int n, ArrayList<Main> processes) {
        this.pid = pid;
        this.n = n;
        this.processes = processes;
    }

    public void run() {
        System.out.println("Process " + this.pid + " started.");
        try {
            Thread.sleep(this.pid * 1000);
            this.requestCriticalSection();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    private synchronized void incrementClock(int receivedTimestamp) {
        this.clock = Math.max(this.clock, receivedTimestamp) + 1;
    }

    private void requestCriticalSection() {
        synchronized(this) {
            this.clock++;
            this.state = State.WANTED;
            this.requestTimeStamp = clock;
            System.out.println("Process " + pid + " requests CS at time " + clock);
        }
        for(Main p : processes) {
            if(p.pid != this.pid){
            p.receiveRequest(this.pid, this.requestTimeStamp);
            }
        }

        this.waitForReply();

        this.enterCriticalSection();

    }

    private void receiveRequest(int frompid, int fromTimeStamp) {
        synchronized(this) {
            this.incrementClock(fromTimeStamp);
            System.out.println("Process " + this.pid + " received REQUEST from " + frompid + " with timestamp " + fromTimeStamp + ", local clock = " + clock);
            boolean deferReply = false;
            if(this.state == State.HELD) {
                deferReply = true;
            }
            else if(this.state == State.WANTED && this.requestTimeStamp < fromTimeStamp || this.state == State.WANTED && this.requestTimeStamp == fromTimeStamp && this.pid < frompid) {
                deferReply = true;
            }
            else if(this.state == State.RELEASED) {
                deferReply = false;
            }

            if (deferReply) {
                this.deferredReplies.add(frompid);
                System.out.println("Process " + this.pid + " defers reply to " + frompid);
            } else {
                sendReply(frompid);
            }
        }
    }

    private void sendReply(int frompid) {
        System.out.println("Process " + pid + " sending REPLY to " + frompid);
        for(Main p : this.processes) {
            if(p.pid == frompid) {
                p.receiveReply(this.pid);
                break;
            }
        }
    }

    private void receiveReply(int fromPid) {
        synchronized (this) {
            replyCount++;
            System.out.println("Process " + this.pid + " received REPLY from " + fromPid);
        }
    }

    private void waitForReply() {
        while(true) {
            synchronized(this){
            if(this.replyCount == this.n - 1) {
                break;
            }
        }
        }
    }

    private void enterCriticalSection() {
        synchronized(this) {
        System.out.println("Process " + this.pid + " Entering Critical Section");
        this.state = State.HELD;
        }
        try {
            Thread.sleep(2000);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        this.exitCriticalSection();
    }

    private void exitCriticalSection() {
        synchronized(this) {
        System.out.println("Process " + this.pid + " Exiting Critical Section");
        this.state = State.RELEASED;
        for(Integer pid : this.deferredReplies) {
            this.sendReply(pid);
        }
        this.deferredReplies.clear();
        }
    }

    public static void main(String[] args) {
        int n = 5;
        ArrayList<Main> processes = new ArrayList<>();

        for(int i = 0; i < n; i++) {
            processes.add(new Main(i, n, processes));
        }

        for(Main process : processes) {
            new Thread(process).start();
        }
    }
}
