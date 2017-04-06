package freedom.jdfs.storage;
/**
 *  char group_name[FDFS_GROUP_NAME_MAX_LEN + 8];   //for 8 bytes alignment
	long total_mb;  //total disk storage in MB
	long free_mb;  //free disk storage in MB
	long trunk_free_mb;  //trunk free disk storage in MB
	int alloc_size;  //alloc storage count
	int count;    //total server count
	int active_count; //active server count
	int storage_port;  //storage server port
	int storage_http_port; //storage http server port
	int current_trunk_file_id;  //current trunk file id report by storage
	StorageDetail **all_servers;   //all storage servers
	StorageDetail **sorted_servers;  //storages order by ip addr
	StorageDetail **active_servers;  //storages order by ip addr
	StorageDetail *pStoreServer;  //for upload priority mode
	StorageDetail *pTrunkServer;  //point to the trunk server
	char last_trunk_server_id[FDFS_STORAGE_ID_MAX_SIZE];

#ifdef WITH_HTTPD
	StorageDetail **http_servers;  //storages order by ip addr
	int http_server_count; //http server count
	int current_http_server; //current http server index
#endif

	int current_read_server;   //current read storage server index
	int current_write_server;  //current write storage server index

	int store_path_count;  //store base path count of each storage server

	// subdir_count * subdir_count directories will be auto created
	//   under each store_path (disk) of the storage servers
	int subdir_count_per_path;

	int **last_sync_timestamps;//row for src storage, col for dest storage

	int chg_count;   //current group changed count
	int trunk_chg_count;   //trunk server changed count
	time_t last_source_update;  //last source update timestamp
	time_t last_sync_update;    //last synced update timestamp
 * 
 * */
public class GroupInfo {

	String group_name;   //for 8 bytes alignment
	long total_mb;  //total disk storage in MB
	long free_mb;  //free disk storage in MB
	long trunk_free_mb;  //trunk free disk storage in MB
	int alloc_size;  //alloc storage count
	int count;    //total server count
	int active_count; //active server count
	int storage_port;  //storage server port
	int storage_http_port; //storage http server port
	int current_trunk_file_id;  //current trunk file id report by storage
	StorageDetail all_servers;   //all storage servers
	StorageDetail sorted_servers;  //storages order by ip addr
	StorageDetail active_servers;  //storages order by ip addr
	StorageDetail pStoreServer;  //for upload priority mode
	StorageDetail pTrunkServer;  //point to the trunk server
	String last_trunk_server_id;
	
	StorageDetail http_servers;  //storages order by ip addr
	int http_server_count; //http server count
	int current_http_server; //current http server index
	
	int current_read_server;   //current read storage server index
	int current_write_server;  //current write storage server index

	int store_path_count;  //store base path count of each storage server

	// subdir_count * subdir_count directories will be auto created
	//   under each store_path (disk) of the storage servers
	int subdir_count_per_path;

	int last_sync_timestamps;//row for src storage, col for dest storage

	int chg_count;   //current group changed count
	int trunk_chg_count;   //trunk server changed count
	long last_source_update;  //last source update timestamp
	long last_sync_update;    //last synced update timestamp
}
