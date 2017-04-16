package freedom.jdfs.protocol;
/**
 * 包头
 * */
public class PacketHeader {
	private long bodyLen;
	private byte cmd;
	private byte errno;

	public PacketHeader(long bodyLen, byte cmd) {
		this(bodyLen, cmd, ProtoCommon.SUCCESS);
	}

	public PacketHeader(long bodyLen, byte cmd, byte errno) {
		this.bodyLen = bodyLen;
		this.cmd = cmd;
		this.errno = errno;
	}

	public long getBodyLen() {
		return bodyLen;
	}

	public void setBodyLen(long bodyLen) {
		this.bodyLen = bodyLen;
	}

	public byte getCmd() {
		return cmd;
	}

	public void setCmd(byte cmd) {
		this.cmd = cmd;
	}

	public byte getErrno() {
		return errno;
	}

	public void setErrno(byte errno) {
		this.errno = errno;
	}
	
}