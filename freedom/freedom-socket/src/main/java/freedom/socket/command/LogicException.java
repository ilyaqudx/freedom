package freedom.socket.command;

public class LogicException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7732718620178281802L;
	private int code;
	private String msg;
	public LogicException(int code, String msg) 
	{
		this.code = code;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
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
		return "LogicException [code=" + code + ", msg=" + msg + "]";
	}
	
}
