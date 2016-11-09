package freedom.bio.core;

import java.util.Arrays;

public class PacketReq {

	private PacketHead head;
	private byte[] data;
	public PacketReq(PacketHead head,byte[] data)
	{
		this.head = head;
		this.data = data;
	}
	
	public PacketHead getHead() {
		return head;
	}
	public void setHead(PacketHead head) {
		this.head = head;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data)
	{
		this.data = data;
	}
	@Override
	public String toString() {
		return "PacketReq [head=" + head + ", data=" + Arrays.toString(data)
				+ "]";
	}
}
