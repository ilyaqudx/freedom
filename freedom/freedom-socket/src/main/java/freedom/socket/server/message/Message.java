
package freedom.socket.server.message;


public class Message {

	private short cmd;
	private long mid;
	private byte oneWay;
	private Object body;
	public short getCmd() {
		return cmd;
	}
	public void setCmd(short cmd) {
		this.cmd = cmd;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public byte getOneWay() {
		return oneWay;
	}
	public void setOneWay(byte oneWay) {
		this.oneWay = oneWay;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "Message [cmd=" + cmd + ", mid=" + mid + ", oneWay=" + oneWay
				+ ", body=" + body + "]";
	}
	
}
