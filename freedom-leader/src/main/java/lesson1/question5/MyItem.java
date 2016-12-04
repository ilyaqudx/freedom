package lesson1.question5;

public class MyItem {

	private byte type;
	private byte color;
	private byte price;
	public MyItem(byte type, byte color, byte price) {
		this.type = type;
		this.color = color;
		this.price = price;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getColor() {
		return color;
	}
	public void setColor(byte color) {
		this.color = color;
	}
	public byte getPrice() {
		return price;
	}
	public void setPrice(byte price) {
		this.price = price;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + color;
		result = prime * result + price;
		result = prime * result + type;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyItem other = (MyItem) obj;
		if (color != other.color)
			return false;
		if (price != other.price)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MyItem [type=" + type + ", color=" + color + ", price=" + price + "]";
	}
}
