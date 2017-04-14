package freedom.jdfs.protocol;

import java.nio.ByteBuffer;

import freedom.jdfs.storage.Globle;

public class DownloadPacket {
	private long offset;
	private long downloadBytes;
	private String group;
	private String fileName;

	public DownloadPacket(long offset, long downloadBytes, String group,String fileName) 
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
	
	public static final DownloadPacket parsePacket(ByteBuffer buffer,long totalLength)
	{
		int bodyLen = (int) (totalLength - ProtoCommon.HEADER_LENGTH);
		buffer.position(ProtoCommon.HEADER_LENGTH);
		long offset = buffer.getLong();
		long downloadBytes = buffer.getLong();
		String group = Globle.parseCString(buffer.array(),buffer.position());
		String fileName = new String(buffer.array(),buffer.position() + 16,bodyLen - 32);
		
		return new DownloadPacket(offset, downloadBytes, group, fileName);
	}

	@Override
	public String toString() {
		return "DownloadPacket [offset=" + offset + ", downloadBytes="
				+ downloadBytes + ", group=" + group + ", fileName=" + fileName
				+ "]";
	}
}