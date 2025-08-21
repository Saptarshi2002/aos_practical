package distributed_system_os.assignment4;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Site implements Runnable {
    public int site_id;
    public boolean request_send = false;
    public Site holder;
    public Queue<Site> request_q = new ConcurrentLinkedQueue<>();
    public boolean token = false;
    public boolean cs = false;

    // Constructor Function
    public Site(int site_id) {
        this.site_id = site_id;
    }

    @Override
    public void run() {
        System.out.println("Site " + this.site_id + " is Started");
        if (this.holder == this && this.token == true) {
            this.enterCS();
        } else {
            try {
                Thread.sleep(4000);
                this.requestToken();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Own Token request
    public void requestToken() {
        System.out.println(this.site_id + " Requested for token to " + this.holder.site_id);
        if (this.token && this.request_q.isEmpty()) {
            this.enterCS();
        } else if (!this.token && !this.request_send) {
            this.request_q.offer(this);
            this.request_send = true;
            this.holder.receiveTokenRequest(this);
        } else if (!this.token && this.request_send) {
            this.request_q.offer(this);
        }
    }

    // Receive Token request
    public void receiveTokenRequest(Site site) {
        synchronized (this) {
            System.out.println(this.site_id + " Received token request from " + site.site_id);
            if (this.token && !this.cs && this.request_q.isEmpty() && !this.request_send) {
                if (this != site) {
                    this.token = false;
                    this.holder = site;
                    this.holder.receiveToken();
                }
            } else if (!this.token && this.request_send) {
                this.request_q.offer(site);
            } else if (!this.token && !this.request_send) {
                this.request_q.offer(site);
                // if (this != site) {
                    this.holder.receiveTokenRequest(this);
                // }
            }
        }
    }

    // Receive Token
    // Sender site's queue is not empty so it gives the token with a token request
    public void receiveTokenWithReq(Site site) {
        System.out.println(this.site_id + " Received token from it's holder with a token request");
        if (this.request_send) {
            this.request_send = false;
        }
        this.token = true;
        this.request_q.offer(site);
        if (this.request_q.peek() == this) {
            this.request_q.poll();
            this.enterCS();
        } else {
            this.holder = this.request_q.poll();
            if (this.request_q.isEmpty()) {
                this.token = false;
                this.holder.receiveToken();
            } else {
                this.token = false;
                this.request_send = true;
                this.holder.receiveTokenWithReq(this);
            }
        }
    }

    // Receive Token
    // Sender site's queue is empty so it gives the token directly
    public void receiveToken() {
        System.out.println(this.site_id + " Received token from it's holder");
        if (this.request_send) {
            this.request_send = false;
        }
        this.token = true;
        if (this.request_q.peek() == this) {
            this.request_q.poll();
            this.enterCS();
        } else {
            this.holder = this.request_q.poll();
            if (this.request_q.isEmpty()) {
                this.token = false;
                this.holder.receiveToken();
            } else {
                this.token = false;
                this.request_send = true;
                this.holder.receiveTokenWithReq(this);
            }
        }
    }

    public void enterCS() {
        this.cs = true;
        System.out.println("The Site: " + this.site_id + " Entering critical section");
        try {
            Thread.sleep(2000);
            this.exitCS();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void exitCS() {
        this.cs = false;
        System.out.println("The Site: " + this.site_id + " Exiting critical section");
        System.out.println("Local Queue for site " + site_id);
        if (!this.request_q.isEmpty()) {
            for (Site s : this.request_q) {
                System.out.println(s.site_id);
            }
            Site nextHolder = this.request_q.poll();
            if (nextHolder != null) {
                this.holder = nextHolder;
                if (this.request_q.isEmpty()) {
                    this.token = false;
                    this.holder.receiveToken();
                } else {
                    this.token = false;
                    this.request_send = true;
                    this.holder.receiveTokenWithReq(this);
                }
            } else {
                // Should never happen, but defensive fallback
                System.out.println("Warning: Site " + site_id + " found no holder while exiting CS.");
                this.holder = this;
                this.token = true;
            }
        } else {
            this.holder = this;
            this.token = true;
        }
    }

}
