package freedom.jdfs.protocol;

import freedom.jdfs.common.Packet;

public class DownloadFilePacket extends Packet{
	private long offset;//8
	private long downloadBytes;//8
	private String group;//16
	private String fileName;

	public DownloadFilePacket(long offset, long downloadBytes, String group,String fileName) 
	{
		this.offset = offset;
		this.group = group;
		this.downloadBytes = downloadBytes;
		this.fileName = fileName;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getDownloadBytes() {
		return downloadBytes;
	}

	public void setDownloadBytes(long downloadBytes) {
		this.downloadBytes = downloadBytes;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public String toString() {
		return "DownloadPacket [offset=" + offset + ", downloadBytes="
				+ downloadBytes + ", group=" + group + ", fileName=" + fileName
				+ "]";
	}
}