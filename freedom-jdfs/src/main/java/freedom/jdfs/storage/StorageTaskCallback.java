package freedom.jdfs.storage;


/**
 * StorageTask回调接口
 * */
public interface StorageTaskCallback {
	
	/**
	 * 完成
	 * @throws Exception 
	 * */
	public void complete(StorageTask storageTask) ;
	/**
	 * 异常
	 * */
	public void exception(StorageTask storageTask,Exception ex);
}
