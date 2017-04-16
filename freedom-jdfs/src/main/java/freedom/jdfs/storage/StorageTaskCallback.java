package freedom.jdfs.storage;


/**
 * StorageTask回调接口
 * */
public interface StorageTaskCallback {
	
	/**
	 * 完成
	 * */
	public void complete(StorageTask storageTask);
	/**
	 * 异常
	 * */
	public void exception(StorageTask storageTask,Exception ex);
}
