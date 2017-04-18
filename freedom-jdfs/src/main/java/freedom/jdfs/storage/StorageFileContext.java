package freedom.jdfs.storage;

import java.io.RandomAccessFile;

import freedom.jdfs.protocol.ProtoCommon;
import freedom.jdfs.storage.trunk.FDFSTrunkFullInfo;

public class StorageFileContext {

	public String fileName;//new byte[ProtoCommon.MAX_PATH_SIZE + 128];  	//full filename

	/* FDFS logic filename to log not including group name */
	//public byte[] fname2log = new byte[128+"-m".length()];
	public String fname2Log;

	public byte op;            //w for writing, r for reading, d for deleting etc.
	public byte syncFlag;     //sync flag log to binlog
	public boolean calcCrc32;    //if calculate file content hash code
	public boolean calcFileHash;      //if calculate file content hash code
	public int openFlags;           //open file flags
	public int[] fileHashCodes = new int[4];   //file hash code
	public int crc32;   //file content crc32 signature
	public MD5_CTX md5Context;

	public ExtraInfoUnion extraInfo = new ExtraInfoUnion();

	public int dioThreadIndex;		//dio thread index
	public long timestamp2Log;		//timestamp to log
	public int deleteFlag;     //delete file flag
	public int createFlag;    //create file flag
	public int buffOffset;    //buffer offset after recv to write to file
	//public int fd;         //file description no
	public RandomAccessFile file;  //java中不能用文件描述符,直接保存为file
	public long start;  //the start offset of file
	public long end;    //the end offset of file
	public long offset; //the current offset of file
	public FileDealDoneCallback doneCallback;
	public DeleteFileLogCallback logCallback;

	public long tvDealStart; //task deal start tv for access log
	
	public boolean hasRemaining()
	{
		return end > offset;
	}
	
	public long remaining()
	{
		return end - offset;
	}
	
	public class ExtraInfoUnion{
		public StorageUploadInfo upload = new StorageUploadInfo();//c 中的union  中的元素  extra_info
		public StorageSetMetaInfo setmeta = new StorageSetMetaInfo();//c 中的union  中的元素  extra_info
	}
	
	public class StorageUploadInfo{
		public boolean ifGenFileName;	  //if upload generate filename
		public byte fileType;           //regular or link file
		public boolean ifSubPathAlloced; //if sub path alloced since V3.0
		public String masterFileName ;//= new byte[128];//128
		public String fileExtName ;//= new byte[ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN + 1];
		public String formattedExtName;// = new byte[ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN + 2];
		public String prefixName ;//= new byte[ProtoCommon.FDFS_FILE_PREFIX_MAX_LEN + 1];
		public String groupName ;//new byte[ProtoCommon.FDFS_GROUP_NAME_MAX_LEN + 1];  	//the upload group name 16
		public long startTime;		//upload start timestamp
		public FDFSTrunkFullInfo trunkInfo = new FDFSTrunkFullInfo();
		public FileBeforeOpenCallback beforeOpenCallback;
		public FileBeforeCloseCallback beforeCloseCallback;
	}
}
