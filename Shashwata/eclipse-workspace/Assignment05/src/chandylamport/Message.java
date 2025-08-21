package chandylamport;

public class Message {
	int id;
	int senttimestamp;
	int receivedtimestamp;
	
	public Message(int id,int senttimestamp,int receivedtimestamp) {
		this.id=id;
		this.senttimestamp=senttimestamp;
		this.receivedtimestamp=receivedtimestamp;
	}
}
