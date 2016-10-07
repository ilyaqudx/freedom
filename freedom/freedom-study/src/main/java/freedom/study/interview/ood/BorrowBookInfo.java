package freedom.study.interview.ood;

import java.util.Date;
/**
 * 借书记录
 * */
public class BorrowBookInfo {

	private long userId;
	private long bookId;
	private Date borrowTime;
	private Date returnTime;
	public BorrowBookInfo(long userId, long bookId)
	{
		this.userId = userId;
		this.bookId = bookId;
		this.borrowTime = new Date();
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getBookId() {
		return bookId;
	}
	public void setBookId(long bookId) {
		this.bookId = bookId;
	}
	public Date getBorrowTime() {
		return borrowTime;
	}
	public void setBorrowTime(Date borrowTime) {
		this.borrowTime = borrowTime;
	}
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	@Override
	public String toString() {
		return "BorrowBookInfo [userId=" + userId + ", bookId=" + bookId + ", borrowTime=" + borrowTime
				+ ", returnTime=" + returnTime + "]";
	}
	
	
}
