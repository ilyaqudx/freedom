package freedom.cache.client;

import java.util.ArrayList;
import java.util.List;

public class ServerNode {

	private String address;
	private List<Integer> hashCodes = new ArrayList<Integer>();
	public ServerNode(String address) {
		// TODO Auto-generated constructor stub
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<Integer> getHashCodes() {
		return hashCodes;
	}
	public void setHashCodes(List<Integer> hashCodes) {
		this.hashCodes = hashCodes;
	}
}
