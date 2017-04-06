package freedom.jdfs.storage;

/**
 * typedef struct
{
	 following count stat by source server,
           not including synced count
	
	long total_upload_count;
	long success_upload_count;
	long total_append_count;
	long success_append_count;
	long total_modify_count;
	long success_modify_count;
	long total_truncate_count;
	long success_truncate_count;
	long total_set_meta_count;
	long success_set_meta_count;
	long total_delete_count;
	long success_delete_count;
	long total_download_count;
	long success_download_count;
	long total_get_meta_count;
	long success_get_meta_count;
	long total_create_link_count;
	long success_create_link_count;
	long total_delete_link_count;
	long success_delete_link_count;
	long total_upload_bytes;
	long success_upload_bytes;
	long total_append_bytes;
	long success_append_bytes;
	long total_modify_bytes;
	long success_modify_bytes;
	long total_download_bytes;
	long success_download_bytes;
	long total_sync_in_bytes;
	long success_sync_in_bytes;
	long total_sync_out_bytes;
	long success_sync_out_bytes;
	long total_file_open_count;
	long success_file_open_count;
	long total_file_read_count;
	long success_file_read_count;
	long total_file_write_count;
	long success_file_write_count;

	 last update timestamp as source server, 
           current server' timestamp
	
	time_t last_source_update;

	 last update timestamp as dest server, 
           current server' timestamp
	
	time_t last_sync_update;

	 last syned timestamp, 
	   source server's timestamp
	
	time_t last_synced_timestamp;

	 last heart beat time 
	time_t last_heart_beat_time;

    struct {
        int alloc_count;
        volatile int current_count;
        int max_count;
    } connection;
} FDFSStorageStat;
 * */

public class StorageStat {

	long total_upload_count;
	long success_upload_count;
	long total_append_count;
	long success_append_count;
	long total_modify_count;
	long success_modify_count;
	long total_truncate_count;
	long success_truncate_count;
	long total_set_meta_count;
	long success_set_meta_count;
	long total_delete_count;
	long success_delete_count;
	long total_download_count;
	long success_download_count;
	long total_get_meta_count;
	long success_get_meta_count;
	long total_create_link_count;
	long success_create_link_count;
	long total_delete_link_count;
	long success_delete_link_count;
	long total_upload_bytes;
	long success_upload_bytes;
	long total_append_bytes;
	long success_append_bytes;
	long total_modify_bytes;
	long success_modify_bytes;
	long total_download_bytes;
	long success_download_bytes;
	long total_sync_in_bytes;
	long success_sync_in_bytes;
	long total_sync_out_bytes;
	long success_sync_out_bytes;
	long total_file_open_count;
	long success_file_open_count;
	long total_file_read_count;
	long success_file_read_count;
	long total_file_write_count;
	long success_file_write_count;
	
	long last_source_update;

	long last_sync_update;
	
	long last_synced_timestamp;
	long last_heart_beat_time;
	
	static final class Connection{
	      int alloc_count;
	      volatile int current_count;
	      int max_count;
	}
}
