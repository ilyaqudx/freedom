package freedom.jdfs.common;

public class Header {

	private long bodyLength;
	private byte cmd;
	private byte code;
	public Header() {
	}
	public Header(long bodyLength, byte cmd, byte code) {
		this.bodyLength = bodyLength;
		this.cmd = cmd;
		this.code = code;
	}
	public long getBodyLength() {
		return bodyLength;
	}
	public void setBodyLength(long bodyLength) {
		this.bodyLength = bodyLength;
	}
	public byte getCmd() {
		return cmd;
	}
	public void setCmd(byte cmd) {
		this.cmd = cmd;
	}
	public byte getCode() {
		return code;
	}
	public void setCode(byte code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "Header [bodyLength=" + bodyLength + ", cmd=" + cmd + ", code="
				+ code + "]";
	}
}
