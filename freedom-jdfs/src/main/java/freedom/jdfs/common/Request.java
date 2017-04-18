package freedom.jdfs.common;

public class Request{

	private Header header;
	private Packet packet;

	public Request(Header header, Packet packet) 
	{
		this.header = header;
		this.packet = packet;
	}
	
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
		return "Request [packet=" + packet + "]";
	}
	
	
}
