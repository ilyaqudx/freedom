package freedom.game.module.table.message.notify;

import java.util.ArrayList;
import java.util.List;

public class NotifySelectLackCardResultMessage extends NotifyMessage {

	
	public NotifySelectLackCardResultMessage(String cmd,int timeout, List<Item> results) {
		super.setCmd(cmd);
		this.timeout = timeout;
		this.results = results;
	}
	private int timeout;
	private List<Item> results = new ArrayList<Item>();
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public List<Item> getResults() {
		return results;
	}
	public void setResults(List<Item> results) {
		this.results = results;
	}
	public static final class Item
	{
		private long userId;
		private int  color;
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
		}
		@Override
		public String toString() {
			return "Item [userId=" + userId + ", color=" + color + "]";
		}
		
	}
}
