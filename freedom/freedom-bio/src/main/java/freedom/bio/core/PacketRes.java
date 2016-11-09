package freedom.bio.core;

public class PacketRes {

	private PacketHead head;
	private Object     data;
	public PacketRes(PacketHead head)
	{
		this.head = head;
	}
	public PacketRes(PacketHead head,Object data)
	{
		this.head = head;
		this.data = data;
	}
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public PacketHead getHead() {
		return head;
	}

	public void setHead(PacketHead head) {
		this.head = head;
	}
}
