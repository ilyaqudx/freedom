package freedom.jdfs.common;

public class Response {

	private Header header;
	private Packet packet;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	@Override
	public String toString() {
		return "Response [header=" + header + ", packet=" + packet + "]";
	}
}
