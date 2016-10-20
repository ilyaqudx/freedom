package freedom.study.interview.ood;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LibraryServiceImpl implements LibraryService {

	private Map<Integer,BookState> states = new ConcurrentHashMap<Integer,BookState>();
	
	@Override
	public void borrowBook(int userId, Book book) throws Exception
	{
		//检查书的数量
		BookState bookState = states.get(book.getId());
		synchronized (bookState) {
			//减少库存
			if(0 == bookState.getCount())
				throw new Exception(String.format("%d is empty", book.getId()));
			bookState.setCount(bookState.getCount() - 1);
		}
		//添加借书记录
		BorrowBookInfo borrowInfo = new BorrowBookInfo(userId,book.getId());
	}

	@Override
	public void returnBook(int userId,Book book)
	{
		//根据用户和图书编号查询借书记录
		BorrowBookInfo borrowInfo = null;
		BookState bookState = states.get(book.getId());
		synchronized (bookState) {
			bookState.setCount(bookState.getCount() + 1);
		}
		borrowInfo.setReturnTime(new Date());
	}

}
