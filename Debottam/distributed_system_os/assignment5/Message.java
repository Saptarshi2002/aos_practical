package distributed_system_os.assignment5;

public class Message {
    public int sender_id;
    public String message;
    public int receiver_id;
    public boolean is_marker;

    public Message(int sender_id, String message, int receiver_id, boolean is_marker) {
        this.sender_id = sender_id;
        this.message = message;
        this.receiver_id = receiver_id;
        this.is_marker = is_marker;
    }

    public String toString() {
        return this.sender_id + this.message + this.receiver_id;
    }
}
