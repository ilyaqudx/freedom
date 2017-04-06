package freedom.jdfs.storage;

public interface FileBeforeOpenCallback {

	public int callback(StorageTask task);
}
