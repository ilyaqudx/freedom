package freedom.jdfs.storage;

public interface FileBeforeCloseCallback {

	public int callback(StorageTask task);
}
