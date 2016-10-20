
package freedom.socket.server.message;

public class Response extends Message{

	private short code = 0;
	private String msg;
	public short getCode() {
		return code;
	}
	public void setCode(short code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Response [code=" + code + ", msg=" + msg + "]" + super.toString();
	}
}
