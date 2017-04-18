package freedom.jdfs.protocol;

import freedom.jdfs.common.Packet;

public class UploadFilePacket extends Packet {

	private byte 	storePathIndex;//1
	private String 	fileExtName;//6
	private long 	fileLength;//8
	
	public UploadFilePacket(byte storePathIndex,String fileExtName, long fileLength)
	{
		this.storePathIndex = storePathIndex;
		this.fileExtName    = fileExtName;
		this.fileLength     = fileLength;
	}
	public byte getStorePathIndex() {
		return storePathIndex;
	}

	public void setStorePathIndex(byte storePathIndex) {
		this.storePathIndex = storePathIndex;
	}

	public String getFileExtName() {
		return fileExtName;
	}

	public void setFileExtName(String fileExtName) {
		this.fileExtName = fileExtName;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	@Override
	public String toString() {
		return "UploadFileRequest [storePathIndex=" + storePathIndex
				+ ", fileExtName=" + fileExtName + ", fileLength=" + fileLength
				+ "]";
	}
}
