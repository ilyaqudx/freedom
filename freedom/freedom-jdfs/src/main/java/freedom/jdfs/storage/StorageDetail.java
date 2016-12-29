package freedom.jdfs.storage;
/**
  
typedef struct StructFDFSStorageDetail
{
	byte status;
	byte padding;  //just for padding
	byte id[FDFS_STORAGE_ID_MAX_SIZE];
	byte ip_addr[IP_ADDRESS_SIZE];
	byte version[FDFS_VERSION_SIZE];
	byte domain_name[FDFS_DOMAIN_NAME_MAX_SIZE];

	struct StructFDFSStorageDetail psync_src_server;
	long path_total_mbs; //total disk storage in MB
	long path_free_mbs;  //free disk storage in MB

	long total_mb;  //total disk storage in MB
	long free_mb;  //free disk storage in MB
	long changelog_offset;  //changelog file offset

	long sync_until_timestamp;
	long join_time;  //storage join timestamp (create timestamp)
	long up_time;    //startup timestamp

	int store_path_count;  //store base path count of each storage server
	int subdir_count_per_path;
	int upload_priority; //storage upload priority

	int storage_port;   //storage server port
	int storage_http_port; //storage http server port

	int current_write_path; //current write path index

	int chg_count;    //current server changed counter
	int trunk_chg_count;   //trunk server changed count
	FDFSStorageStat stat;

#ifdef WITH_HTTPD
	int http_check_last_errno;
	int http_check_last_status;
	int http_check_fail_count;
	byte http_check_error_info[256];
#endif
} FDFSStorageDetail;
  */
public class StorageDetail {

	byte status;
	byte padding;  //just for padding
	String id;//FDFS_STORAGE_ID_MAX_SIZE];
	String ip_addr;
	String version;//[FDFS_VERSION_SIZE];
	String domain_name;//[FDFS_DOMAIN_NAME_MAX_SIZE];

	StorageDetail psync_src_server;
	long path_total_mbs; //total disk storage in MB
	long path_free_mbs;  //free disk storage in MB

	long total_mb;  //total disk storage in MB
	long free_mb;  //free disk storage in MB
	long changelog_offset;  //changelog file offset

	long sync_until_timestamp;
	long join_time;  //storage join timestamp (create timestamp)
	long up_time;    //startup timestamp

	int store_path_count;  //store base path count of each storage server
	int subdir_count_per_path;
	int upload_priority; //storage upload priority

	int storage_port;   //storage server port
	int storage_http_port; //storage http server port

	int current_write_path; //current write path index

	int chg_count;    //current server changed counter
	int trunk_chg_count;   //trunk server changed count
	StorageStat stat;
}
