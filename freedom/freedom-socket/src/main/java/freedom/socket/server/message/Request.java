
package freedom.socket.server.message;

public class Request extends Message{

	private long uid;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String toString() {
		return "Request [uid=" + uid + "]" + super.toString();
	}
}
