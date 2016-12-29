package freedom.jdfs.storage;


/**
 * 
typedef struct
{
	char group_name[FDFS_GROUP_NAME_MAX_LEN + 8];  //for 8 bytes alignment
	int64_t total_mb;  //total disk storage in MB
	int64_t free_mb;  //free disk storage in MB
	int64_t trunk_free_mb;  //trunk free disk storage in MB
	int count;        //server count
	int storage_port; //storage server port
	int storage_http_port; //storage server http port
	int active_count; //active server count
	int current_write_server; //current server index to upload file
	int store_path_count;  //store base path count of each storage server
	int subdir_count_per_path;
	int current_trunk_file_id;  //current trunk file id
	
	
} FDFSGroupStat;
 * */
public class GroupStat {

	String group_name;  //for 8 bytes alignment
	long total_mb;  //total disk storage in MB
	long free_mb;  //free disk storage in MB
	long trunk_free_mb;  //trunk free disk storage in MB
	int count;        //server count
	int storage_port; //storage server port
	int storage_http_port; //storage server http port
	int active_count; //active server count
	int current_write_server; //current server index to upload file
	int store_path_count;  //store base path count of each storage server
	int subdir_count_per_path;
	int current_trunk_file_id;  //current trunk file id
}
