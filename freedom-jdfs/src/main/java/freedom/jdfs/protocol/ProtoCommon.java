package freedom.jdfs.protocol;

import java.security.NoSuchAlgorithmException;


/**
* protocol common functions
* @author Happy Fish / YuQing
* @version Version 1.18
*/
public class ProtoCommon
{
	/**
	* receive header info
	*/
	public static class RecvHeaderInfo
	{
		public byte errno;
		public long body_len;
		
		public RecvHeaderInfo(byte errno, long body_len)
		{
			this.errno = errno;
			this.body_len = body_len;
		}
	}
	
	public static final byte STORAGE_FILE_SIGNATURE_METHOD_HASH = 1;
	public static final byte STORAGE_FILE_SIGNATURE_METHOD_MD5  = 2;
	
	/**
	 * FDFH
	 * */
	
	public static final int  FDHT_MAX_NAMESPACE_LEN	  = 64;
	public static final int  FDHT_MAX_OBJECT_ID_LEN	 = 128;
	public static final int  FDHT_MAX_SUB_KEY_LEN	 = 128;
	public static final String  FDHT_FULL_KEY_SEPERATOR = 	"\\x1";//char '\x1'

	public static final int  FDHT_EXPIRES_NEVER	  = 0;  //never timeout
	public static final int  FDHT_EXPIRES_NONE	  = -1;  //invalid timeout, should ignore
	
	public static final int FDFS_MAX_SERVERS_EACH_GROUP = 	32;
	public static final int FDFS_MAX_GROUPS		        = 512;
	public static final int FDFS_MAX_TRACKERS		    = 16;

	public static final int FDFS_MAX_META_NAME_LEN		  = 64;
	public static final int FDFS_MAX_META_VALUE_LEN		 = 256;

	public static final int FDFS_LOGIC_FILE_PATH_LEN	 = 10;
	public static final int FDFS_TRUE_FILE_PATH_LEN		  = 6;

	public static final int FDFS_ID_TYPE_SERVER_ID     = 1;
	public static final int FDFS_ID_TYPE_IP_ADDRESS    = 2;
	
	public static final int FDFS_FILE_DIST_DEFAULT_ROTATE_COUNT = 100;
	
	//the mode of the files distributed to the data path
	public static final int FDFS_FILE_DIST_PATH_ROUND_ROBIN = 	0 ; //round robin
	public static final int  FDFS_FILE_DIST_PATH_RANDOM	 = 1;  //random
	
	public static final char  FDFS_STORAGE_STORE_PATH_PREFIX_CHAR    = 'M';
	public static final String FDFS_STORAGE_DATA_DIR_FORMAT          = "%02X";
	public static final String FDFS_STORAGE_META_FILE_EXT            = "-m";
	
	public static final int g_fdfs_network_timeout = 0x8000;
	public static final int IOEVENT_TIMEOUT  =  0x8000;
	public static final int MAX_PATH_SIZE = 256;
	public static final int HEADER_LENGTH = 10;
	
	
	//////////////////////////////////////
	
	public static final byte FDFS_PROTO_CMD_QUIT      = 82;
	public static final byte TRACKER_PROTO_CMD_SERVER_LIST_GROUP     = 91;
	public static final byte TRACKER_PROTO_CMD_SERVER_LIST_STORAGE   = 92;
	public static final byte TRACKER_PROTO_CMD_SERVER_DELETE_STORAGE = 93;

	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE = 101;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE = 102;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE = 103;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE = 104;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL = 105;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ALL = 106;
	public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ALL = 107;
	public static final byte TRACKER_PROTO_CMD_RESP = 100;
	public static final byte FDFS_PROTO_CMD_ACTIVE_TEST = 111;
	public static final byte STORAGE_PROTO_CMD_UPLOAD_FILE  = 11;
	public static final byte STORAGE_PROTO_CMD_DELETE_FILE	= 12;
	public static final byte STORAGE_PROTO_CMD_SET_METADATA	 = 13;
	public static final byte STORAGE_PROTO_CMD_DOWNLOAD_FILE = 14;
	public static final byte STORAGE_PROTO_CMD_GET_METADATA	 = 15;
	public static final byte STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE   = 21;
	public static final byte STORAGE_PROTO_CMD_QUERY_FILE_INFO     = 22;
	public static final byte STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE= 23;  //create appender file
	public static final byte STORAGE_PROTO_CMD_APPEND_FILE         = 24;  //append file
	public static final byte STORAGE_PROTO_CMD_MODIFY_FILE         = 34;  //modify appender file
	public static final byte STORAGE_PROTO_CMD_TRUNCATE_FILE       = 36;  //truncate appender file

	public static final byte STORAGE_PROTO_CMD_RESP	 = TRACKER_PROTO_CMD_RESP;

	public static final byte FDFS_STORAGE_STATUS_INIT        = 0;
	public static final byte FDFS_STORAGE_STATUS_WAIT_SYNC   = 1;
	public static final byte FDFS_STORAGE_STATUS_SYNCING     = 2;
	public static final byte FDFS_STORAGE_STATUS_IP_CHANGED  = 3;
	public static final byte FDFS_STORAGE_STATUS_DELETED     = 4;
	public static final byte FDFS_STORAGE_STATUS_OFFLINE     = 5;
	public static final byte FDFS_STORAGE_STATUS_ONLINE      = 6;
	public static final byte FDFS_STORAGE_STATUS_ACTIVE      = 7;
	public static final byte FDFS_STORAGE_STATUS_NONE        = 99;
	
	/**
	* for overwrite all old metadata
	*/
	public static final byte STORAGE_SET_METADATA_FLAG_OVERWRITE = 'O';
	
	/**
	* for replace, insert when the meta item not exist, otherwise update it
	*/
	public static final byte STORAGE_SET_METADATA_FLAG_MERGE = 'M';

	public static final int FDFS_PROTO_PKG_LEN_SIZE	= 8;
	public static final int FDFS_PROTO_CMD_SIZE		= 1;
	public static final int FDFS_GROUP_NAME_MAX_LEN  = 16;
	public static final int FDFS_IPADDR_SIZE	 = 16;
	public static final int FDFS_DOMAIN_NAME_MAX_SIZE = 128;
	public static final int FDFS_VERSION_SIZE = 6;
	public static final int FDFS_STORAGE_ID_MAX_SIZE = 16;
	
	public static final String FDFS_RECORD_SEPERATOR	= "\u0001";
	public static final String FDFS_FIELD_SEPERATOR	  = "\u0002";

	public static final int TRACKER_QUERY_STORAGE_FETCH_BODY_LEN = FDFS_GROUP_NAME_MAX_LEN
                        + FDFS_IPADDR_SIZE - 1 + FDFS_PROTO_PKG_LEN_SIZE;
	public static final int TRACKER_QUERY_STORAGE_STORE_BODY_LEN = FDFS_GROUP_NAME_MAX_LEN
                        + FDFS_IPADDR_SIZE + FDFS_PROTO_PKG_LEN_SIZE;

	protected static final int PROTO_HEADER_CMD_INDEX	   = FDFS_PROTO_PKG_LEN_SIZE;
	protected static final int PROTO_HEADER_STATUS_INDEX = FDFS_PROTO_PKG_LEN_SIZE+1;
	
	public static final byte FDFS_FILE_EXT_NAME_MAX_LEN  = 6;
	public static final byte FDFS_FILE_PREFIX_MAX_LEN    = 16;
	public static final byte FDFS_FILE_PATH_LEN          = 10;
	public static final byte FDFS_FILENAME_BASE64_LENGTH = 27;
	public static final byte FDFS_TRUNK_FILE_INFO_LEN    = 16;
	
	public static final byte ERR_NO_ENOENT    = 2;
	public static final byte ERR_NO_EIO       = 5;
	public static final byte ERR_NO_EBUSY     = 16;
	public static final byte ERR_NO_EINVAL    = 22;
	public static final byte ERR_NO_ENOSPC    = 28;
	public static final byte ECONNREFUSED     = 61;
	public static final byte ERR_NO_EALREADY  = 114;
	
	public static final long INFINITE_FILE_SIZE   = 256 * 1024L * 1024 * 1024 * 1024 * 1024L;
	public static final long APPENDER_FILE_SIZE   = INFINITE_FILE_SIZE;
	public static final long TRUNK_FILE_MARK_SIZE = 512 * 1024L * 1024 * 1024 * 1024 * 1024L;
	public static final long NORMAL_LOGIC_FILENAME_LENGTH = FDFS_FILE_PATH_LEN + FDFS_FILENAME_BASE64_LENGTH + FDFS_FILE_EXT_NAME_MAX_LEN + 1;
	public static final long TRUNK_LOGIC_FILENAME_LENGTH = NORMAL_LOGIC_FILENAME_LENGTH + FDFS_TRUNK_FILE_INFO_LEN;
	
	private ProtoCommon()
	{
	}

	public static String getStorageStatusCaption(byte status)
	{
		switch(status)
		{
			case FDFS_STORAGE_STATUS_INIT:
				return "INIT";
			case FDFS_STORAGE_STATUS_WAIT_SYNC:
				return "WAIT_SYNC";
			case FDFS_STORAGE_STATUS_SYNCING:
				return "SYNCING";
			case FDFS_STORAGE_STATUS_IP_CHANGED:
				return "IP_CHANGED";
			case FDFS_STORAGE_STATUS_DELETED:
				return "DELETED";
			case FDFS_STORAGE_STATUS_OFFLINE:
				return "OFFLINE";
			case FDFS_STORAGE_STATUS_ONLINE:
				return "ONLINE";
			case FDFS_STORAGE_STATUS_ACTIVE:
				return "ACTIVE";
			case FDFS_STORAGE_STATUS_NONE:
				return "NONE";
			default:
				return "UNKOWN";
	}
}

	
 /**
* md5 function
* @param source the input buffer
* @return md5 string
*/
	public static String md5(byte[] source) throws NoSuchAlgorithmException
	{
  	char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};
    java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
    md.update(source);
    byte tmp[] = md.digest();
    char str[] = new char[32];
    int k = 0;
    for (int i = 0; i < 16; i++)
    {
     str[k++] = hexDigits[tmp[i] >>> 4 & 0xf];
     str[k++] = hexDigits[tmp[i] & 0xf];
    }
    
  	return new String(str);
 }
 
 /**
* generate slave filename
* @param master_filename the master filename to generate the slave filename
* @param prefix_name the prefix name to generate the slave filename
* @param ext_name the extension name of slave filename, null for same as the master extension name
* @return slave filename string
*/
 public static String genSlaveFilename(String master_filename, 
                String prefix_name, String ext_name) throws MyException
 {
    String true_ext_name;
    int dotIndex;

    if (master_filename.length() < 28 + FDFS_FILE_EXT_NAME_MAX_LEN)
    {
            throw new MyException("master filename \"" + master_filename + "\" is invalid");
    }

    dotIndex = master_filename.indexOf('.', master_filename.length() - (FDFS_FILE_EXT_NAME_MAX_LEN + 1));
    if (ext_name != null)
    {
	      if (ext_name.length() == 0)
	      {
	              true_ext_name = "";
	      }
	      else if (ext_name.charAt(0) == '.')
	      {
	              true_ext_name = ext_name;
	      }
	      else
	      {
	              true_ext_name = "." + ext_name;
	      }
    }
		else
    {
	      if (dotIndex < 0)
	      {
	              true_ext_name = "";
	      }
	      else
	      {
	              true_ext_name = master_filename.substring(dotIndex);
	      }
    }

    if (true_ext_name.length() == 0 && prefix_name.equals("-m"))
    {
        throw new MyException("prefix_name \"" + prefix_name + "\" is invalid");
    }

    if (dotIndex < 0)
    {
        return master_filename + prefix_name + true_ext_name;
    }
    else
    {
        return master_filename.substring(0, dotIndex) + prefix_name + true_ext_name;
    }
	}
 
 
 
 
 	/**
 	 * error code
 	 * */
 public static final int	EPERM	=	 1;	/* Operation not permitted */
 public static final int	ENOENT	=	 2;	/* No such file or directory */
 public static final int	ESRCH	=	 3;	/* No such process */
 public static final int	EINTR	=	 4;	/* Interrupted system call */
 public static final int	EIO		=	 5;	/* I/O error */
 public static final int	ENXIO	=	 6;	/* No such device or address */
 public static final int	E2BIG	=	 7;	/* Argument list too long */
 public static final int	ENOEXEC	=	 8;	/* Exec format error */
 public static final int	EBADF	=	 9;	/* Bad file number */
 public static final int	ECHILD	=	10;	/* No child processes */
 public static final int	EAGAIN	=	11;	/* Try again */
 public static final int	ENOMEM	=	12;	/* Out of memory */
 public static final int	EACCES	=	13;	/* Permission denied */
 public static final int	EFAULT	=	14;	/* Bad address */
 public static final int	ENOTBLK	=	15;	/* Block device required */
 public static final int	EBUSY	=	16;	/* Device or resource busy */
 public static final int	EEXIST	=	17;	/* File exists */
 public static final int	EXDEV	=	18;	/* Cross-device link */
 public static final int	ENODEV	=	19;	/* No such device */
 public static final int	ENOTDIR	=	20;	/* Not a directory */
 public static final int	EISDIR	=	21;	/* Is a directory */
 public static final int	EINVAL	=	22;	/* Invalid argument */
 public static final int	ENFILE	=	23;	/* File table overflow */
 public static final int	EMFILE	=	24;	/* Too many open files */
 public static final int	ENOTTY	=	25;	/* Not a typewriter */
 public static final int	ETXTBSY	=	26;	/* Text file busy */
 public static final int	EFBIG	=	27;	/* File too large */
 public static final int	ENOSPC	=	28;	/* No space left on device */
 public static final int	ESPIPE	=	29;	/* Illegal seek */
 public static final int	EROFS	=	30;	/* Read-only file system */
 public static final int	EMLINK	=	31;	/* Too many links */
 public static final int	EPIPE	=	32;	/* Broken pipe */
 public static final int	EDOM	=	33;	/* Math argument out of domain of func */
 public static final int	ERANGE	=	34;	/* Math result not representable */
 
 
 
 
 
 /**
  * File Type
  * */
 public static final byte _FILE_TYPE_APPENDER = 1;
 public static final byte _FILE_TYPE_TRUNK    = 2;   //if trunk file, since V3.0
 public static final byte _FILE_TYPE_SLAVE    = 4;
 public static final byte _FILE_TYPE_REGULAR  = 8;
 public static final byte _FILE_TYPE_LINK     = 16;
 
 /**
  * storage sync
  * */
 public static final byte STORAGE_OP_TYPE_SOURCE_CREATE_FILE	 = 'C';  //upload file
 public static final byte STORAGE_OP_TYPE_SOURCE_APPEND_FILE	 = 'A';  //append file
 public static final byte STORAGE_OP_TYPE_SOURCE_DELETE_FILE	 = 'D';  //delete file
 public static final byte STORAGE_OP_TYPE_SOURCE_UPDATE_FILE	 = 'U';  //for whole file update such as metadata file
 public static final byte STORAGE_OP_TYPE_SOURCE_MODIFY_FILE	 = 'M';  //for part modify
 public static final byte STORAGE_OP_TYPE_SOURCE_TRUNCATE_FILE	 = 'T';  //truncate file
 public static final byte STORAGE_OP_TYPE_SOURCE_CREATE_LINK	 = 'L';  //create symbol link
 public static final byte STORAGE_OP_TYPE_REPLICA_CREATE_FILE	 = 'c';
 public static final byte STORAGE_OP_TYPE_REPLICA_APPEND_FILE	 = 'a';
 public static final byte STORAGE_OP_TYPE_REPLICA_DELETE_FILE	 = 'd';
 public static final byte STORAGE_OP_TYPE_REPLICA_UPDATE_FILE	 = 'u';
 public static final byte STORAGE_OP_TYPE_REPLICA_MODIFY_FILE	 = 'm';
 public static final byte STORAGE_OP_TYPE_REPLICA_TRUNCATE_FILE	 = 't';
 public static final byte STORAGE_OP_TYPE_REPLICA_CREATE_LINK	 = 'l';

 public static final int STORAGE_BINLOG_BUFFER_SIZE		 = 64 * 1024;
 public static final int STORAGE_BINLOG_LINE_SIZE		 = 256;
 
 /**
  * storage nio op
  * */
 
 public static final byte FDFS_STORAGE_FILE_OP_READ      = 'R';
 public static final byte FDFS_STORAGE_FILE_OP_WRITE     = 'W';
 public static final byte FDFS_STORAGE_FILE_OP_APPEND    = 'A';
 public static final byte FDFS_STORAGE_FILE_OP_DELETE    = 'D';
 public static final byte FDFS_STORAGE_FILE_OP_DISCARD   = 'd';
 
 
 /* open/fcntl - O_SYNC is only implemented on blocks devices and on files
 located on a few file systems.  */
 public static final int  O_ACCMODE	    = 0x0003;
 public static final int  O_RDONLY	    = 0x00;
 public static final int  O_WRONLY	    = 0x01;
 public static final int  O_RDWR		= 0x02;
 public static final int  O_CREAT		= 0x0100;	/* not fcntl */
 public static final int  O_EXCL		= 0x0200;	/* not fcntl */
 public static final int  O_NOCTTY	    = 0x0400;	/* not fcntl */
 public static final int  O_TRUNC		= 0x01000;	/* not fcntl */
 public static final int  O_APPEND	    = 0x02000;
 public static final int  O_NONBLOCK	= 0x04000;
 public static final int  O_NDELAY	    = O_NONBLOCK;
 public static final int  O_SYNC	    = 0x04010000;
 public static final int  O_FSYNC		= O_SYNC;
 public static final int  O_ASYNC		= 0x020000;

 public static final int RAND_MAX = 0x7fff;

public static final int CRC32_XINIT = 0xFFFFFFFF;
public static final int FAIL = -1;
public static final int FILE_SIGNATURE_SIZE = 24;
public static final byte SUCCESS = 0;
}
