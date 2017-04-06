package freedom.jdfs.storage;
/**
 * struct {
        char sz_alloc_count[4];
        char sz_current_count[4];
        char sz_max_count[4];
    } connection;

	char sz_total_upload_count[];
	char sz_success_upload_count[];
	char sz_total_append_count[];
	char sz_success_append_count[];
	char sz_total_modify_count[];
	char sz_success_modify_count[];
	char sz_total_truncate_count[];
	char sz_success_truncate_count[];
	char sz_total_set_meta_count[];
	char sz_success_set_meta_count[];
	char sz_total_delete_count[];
	char sz_success_delete_count[];
	char sz_total_download_count[];
	char sz_success_download_count[];
	char sz_total_get_meta_count[];
	char sz_success_get_meta_count[];
	char sz_total_create_link_count[];
	char sz_success_create_link_count[];
	char sz_total_delete_link_count[];
	char sz_success_delete_link_count[];
	char sz_total_upload_bytes[];
	char sz_success_upload_bytes[];
	char sz_total_append_bytes[];
	char sz_success_append_bytes[];
	char sz_total_modify_bytes[];
	char sz_success_modify_bytes[];
	char sz_total_download_bytes[];
	char sz_success_download_bytes[];
	char sz_total_sync_in_bytes[];
	char sz_success_sync_in_bytes[];
	char sz_total_sync_out_bytes[];
	char sz_success_sync_out_bytes[];
	char sz_total_file_open_count[];
	char sz_success_file_open_count[];
	char sz_total_file_read_count[];
	char sz_success_file_read_count[];
	char sz_total_file_write_count[];
	char sz_success_file_write_count[];
	char sz_last_source_update[];
	char sz_last_sync_update[];
	char sz_last_synced_timestamp[];
	char sz_last_heart_beat_time[];
 * */
public class StorageStatBuff {

	public static final class connection{
		char sz_alloc_count[] = new char[4];
		char sz_current_count[] = new char[4];
		char sz_max_count[] = new char[4];
	}

	char sz_total_upload_count[] = new char[8];
	char sz_success_upload_count[] = new char[8];
	char sz_total_append_count[]= new char[8];
	char sz_success_append_count[]= new char[8];
	char sz_total_modify_count[]= new char[8];
	char sz_success_modify_count[]= new char[8];
	char sz_total_truncate_count[]= new char[8];
	char sz_success_truncate_count[]= new char[8];
	char sz_total_set_meta_count[]= new char[8];
	char sz_success_set_meta_count[]= new char[8];
	char sz_total_delete_count[]= new char[8];
	char sz_success_delete_count[]= new char[8];
	char sz_total_download_count[]= new char[8];
	char sz_success_download_count[]= new char[8];
	char sz_total_get_meta_count[]= new char[8];
	char sz_success_get_meta_count[]= new char[8];
	char sz_total_create_link_count[]= new char[8];
	char sz_success_create_link_count[]= new char[8];
	char sz_total_delete_link_count[]= new char[8];
	char sz_success_delete_link_count[]= new char[8];
	char sz_total_upload_bytes[]= new char[8];
	char sz_success_upload_bytes[]= new char[8];
	char sz_total_append_bytes[]= new char[8];
	char sz_success_append_bytes[]= new char[8];
	char sz_total_modify_bytes[]= new char[8];
	char sz_success_modify_bytes[]= new char[8];
	char sz_total_download_bytes[]= new char[8];
	char sz_success_download_bytes[]= new char[8];
	char sz_total_sync_in_bytes[]= new char[8];
	char sz_success_sync_in_bytes[]= new char[8];
	char sz_total_sync_out_bytes[]= new char[8];
	char sz_success_sync_out_bytes[]= new char[8];
	char sz_total_file_open_count[]= new char[8];
	char sz_success_file_open_count[]= new char[8];
	char sz_total_file_read_count[]= new char[8];
	char sz_success_file_read_count[]= new char[8];
	char sz_total_file_write_count[]= new char[8];
	char sz_success_file_write_count[]= new char[8];
	char sz_last_source_update[]= new char[8];
	char sz_last_sync_update[]= new char[8];
	char sz_last_synced_timestamp[]= new char[8];
	char sz_last_heart_beat_time[]= new char[8];
}
