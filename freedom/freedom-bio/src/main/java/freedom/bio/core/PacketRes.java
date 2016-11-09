package freedom.bio.core;

public class PacketRes {

	private PacketHead head;
	private byte[]     data;
	public PacketRes(PacketHead head)
	{
		this.head = head;
	}
	public PacketRes(PacketHead head,byte[] data)
	{
		this.head = head;
		this.data = data;
	}
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public PacketHead getHead() {
		return head;
	}

	public void setHead(PacketHead head) {
		this.head = head;
	}
}
