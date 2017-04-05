package freedom.jdfs.storage;

/**
 * 删除文件日志回调
 * */
public interface DeleteFileLogCallback {

	public void callback(StorageTask task,int errno);
}
