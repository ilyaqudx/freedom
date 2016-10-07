package freedom.study.interview.ood;

public class BookState {

	private int bookId;
	private int count;
	private String location;
	@Override
	public String toString() {
		return "BookState [bookId=" + bookId + ", count=" + count + ", location=" + location + "]";
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
