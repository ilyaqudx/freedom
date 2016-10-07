package freedom.study.interview.ood;

/**图书馆服务(单例/无状态)
 * @author Administrator
 *
 */
public interface LibraryService {

	/**
	 * 借书
	 * @throws Exception 
	 * */
	public void borrowBook(int userId,Book book) throws Exception;
	/**
	 * 还书
	 * */
	public void returnBook(int userId,Book book) throws Exception;
}
