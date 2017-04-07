package freedom.jdfs.storage;
/**
 * group_name : group1
write_mark_file_freq : 500
sync_stat_file_interval : 300
store_path0 : /root/fastdfs/storage
tracker_server : 192.168.0.192:22122
rotate_error_log_size : 0
log_level : debug
max_connections : 256
run_by_group : 
use_access_log : false
disk_rw_separated : true
rotate_access_log_size : 0
bind_addr : 
connect_timeout : 30
disk_reader_threads : 1
sync_binlog_buff_interval : 10
disk_writer_threads : 1
subdir_count_per_path : 256
sync_start_time : 00:00
network_timeout : 60
work_threads : 4
stat_report_interval : 60
sync_end_time : 23:59
keep_alive : 0
file_distribute_rotate_count : 100
allow_hosts : *
client_bind : true
base_path : /root/fastdfs/storage
http.server_port : 8888
log_file_keep_days : 0
heart_beat_interval : 30
disabled : false
sync_log_buff_interval : 10
run_by_user : 
thread_stack_size : 512KB
port : 23000
file_signature_method : hash
upload_priority : 10
if_alias_prefix : 
file_distribute_path_mode : 0
key_namespace : FastDFS
check_file_duplicate : 0
sync_wait_msec : 50
use_connection_pool : false
connection_pool_max_idle_time : 3600
access_log_rotate_time : 00:00
rotate_access_log : false
http.domain_name : 
accept_threads : 1
rotate_error_log : false
file_sync_skip_invalid_record : false
sync_interval : 0
fsync_after_written_bytes : 0
error_log_rotate_time : 00:00
store_path_count : 1
buff_size : 256KB
 * */
public class StorageConfig {

	public boolean disable;
	public String bind_address;
	public String base_dir;
	public String group_name;
	public int write_mark_file_freq ;
	public int sync_stat_file_interval ;
	public String store_path0 ;
	public String tracker_server ;
	public int rotate_error_log_size ;
	public String log_level ;
	public int max_connections ;
	public int run_by_group ;
	public boolean use_access_log ;
	public boolean disk_rw_separated ;
	public int rotate_access_log_size ;
	public String bind_addr ;
	public int connect_timeout ;
	public int disk_reader_threads ;
	public int sync_binlog_buff_interval ;
	public int disk_writer_threads ;
	public int subdir_count_per_path ;
	public String sync_start_time ;
	public int network_timeout ;
	public int work_threads ;
	public int stat_report_interval ;
	public String sync_end_time ;
	public int keep_alive ;
	public int file_distribute_rotate_count ;
	public String allow_hosts ;
	public boolean client_bind ;
	public String base_path ;
	public int http_server_port ;
	public int log_file_keep_days ;
	public int heart_beat_interval ;
	public boolean disabled ;
	public int sync_log_buff_interval ;
	public String run_by_user ;
	public String thread_stack_size ;
	public int port ;
	public String file_signature_method ;
	public int upload_priority ;
	public String if_alias_prefix ;
	public int file_distribute_path_mode ;
	public String key_namespace ;
	public int check_file_duplicate ;
	public int sync_wait_msec ;
	public boolean use_connection_pool ;
	public int connection_pool_max_idle_time ;
	public String access_log_rotate_time ;
	public boolean rotate_access_log ;
	public String http_domain_name ;
	public int accept_threads ;
	public boolean rotate_error_log ;
	public boolean file_sync_skip_invalid_record ;
	public int sync_interval ;
	public int fsync_after_written_bytes ;
	public String error_log_rotate_time ;
	public int store_path_count ;
	public String buff_size ;
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public String getBind_address() {
		return bind_address;
	}
	public void setBind_address(String bind_address) {
		this.bind_address = bind_address;
	}
	public String getBase_dir() {
		return base_dir;
	}
	public void setBase_dir(String base_dir) {
		this.base_dir = base_dir;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public int getWrite_mark_file_freq() {
		return write_mark_file_freq;
	}
	public void setWrite_mark_file_freq(int write_mark_file_freq) {
		this.write_mark_file_freq = write_mark_file_freq;
	}
	public int getSync_stat_file_interval() {
		return sync_stat_file_interval;
	}
	public void setSync_stat_file_interval(int sync_stat_file_interval) {
		this.sync_stat_file_interval = sync_stat_file_interval;
	}
	public String getStore_path0() {
		return store_path0;
	}
	public void setStore_path0(String store_path0) {
		this.store_path0 = store_path0;
	}
	public String getTracker_server() {
		return tracker_server;
	}
	public void setTracker_server(String tracker_server) {
		this.tracker_server = tracker_server;
	}
	public int getRotate_error_log_size() {
		return rotate_error_log_size;
	}
	public void setRotate_error_log_size(int rotate_error_log_size) {
		this.rotate_error_log_size = rotate_error_log_size;
	}
	public String getLog_level() {
		return log_level;
	}
	public void setLog_level(String log_level) {
		this.log_level = log_level;
	}
	public int getMax_connections() {
		return max_connections;
	}
	public void setMax_connections(int max_connections) {
		this.max_connections = max_connections;
	}
	public int getRun_by_group() {
		return run_by_group;
	}
	public void setRun_by_group(int run_by_group) {
		this.run_by_group = run_by_group;
	}
	public boolean isUse_access_log() {
		return use_access_log;
	}
	public void setUse_access_log(boolean use_access_log) {
		this.use_access_log = use_access_log;
	}
	public boolean isDisk_rw_separated() {
		return disk_rw_separated;
	}
	public void setDisk_rw_separated(boolean disk_rw_separated) {
		this.disk_rw_separated = disk_rw_separated;
	}
	public int getRotate_access_log_size() {
		return rotate_access_log_size;
	}
	public void setRotate_access_log_size(int rotate_access_log_size) {
		this.rotate_access_log_size = rotate_access_log_size;
	}
	public String getBind_addr() {
		return bind_addr;
	}
	public void setBind_addr(String bind_addr) {
		this.bind_addr = bind_addr;
	}
	public int getConnect_timeout() {
		return connect_timeout;
	}
	public void setConnect_timeout(int connect_timeout) {
		this.connect_timeout = connect_timeout;
	}
	public int getDisk_reader_threads() {
		return disk_reader_threads;
	}
	public void setDisk_reader_threads(int disk_reader_threads) {
		this.disk_reader_threads = disk_reader_threads;
	}
	public int getSync_binlog_buff_interval() {
		return sync_binlog_buff_interval;
	}
	public void setSync_binlog_buff_interval(int sync_binlog_buff_interval) {
		this.sync_binlog_buff_interval = sync_binlog_buff_interval;
	}
	public int getDisk_writer_threads() {
		return disk_writer_threads;
	}
	public void setDisk_writer_threads(int disk_writer_threads) {
		this.disk_writer_threads = disk_writer_threads;
	}
	public int getSubdir_count_per_path() {
		return subdir_count_per_path;
	}
	public void setSubdir_count_per_path(int subdir_count_per_path) {
		this.subdir_count_per_path = subdir_count_per_path;
	}
	public String getSync_start_time() {
		return sync_start_time;
	}
	public void setSync_start_time(String sync_start_time) {
		this.sync_start_time = sync_start_time;
	}
	public int getNetwork_timeout() {
		return network_timeout;
	}
	public void setNetwork_timeout(int network_timeout) {
		this.network_timeout = network_timeout;
	}
	public int getWork_threads() {
		return work_threads;
	}
	public void setWork_threads(int work_threads) {
		this.work_threads = work_threads;
	}
	public int getStat_report_interval() {
		return stat_report_interval;
	}
	public void setStat_report_interval(int stat_report_interval) {
		this.stat_report_interval = stat_report_interval;
	}
	public String getSync_end_time() {
		return sync_end_time;
	}
	public void setSync_end_time(String sync_end_time) {
		this.sync_end_time = sync_end_time;
	}
	public int getKeep_alive() {
		return keep_alive;
	}
	public void setKeep_alive(int keep_alive) {
		this.keep_alive = keep_alive;
	}
	public int getFile_distribute_rotate_count() {
		return file_distribute_rotate_count;
	}
	public void setFile_distribute_rotate_count(int file_distribute_rotate_count) {
		this.file_distribute_rotate_count = file_distribute_rotate_count;
	}
	public String getAllow_hosts() {
		return allow_hosts;
	}
	public void setAllow_hosts(String allow_hosts) {
		this.allow_hosts = allow_hosts;
	}
	public boolean isClient_bind() {
		return client_bind;
	}
	public void setClient_bind(boolean client_bind) {
		this.client_bind = client_bind;
	}
	public String getBase_path() {
		return base_path;
	}
	public void setBase_path(String base_path) {
		this.base_path = base_path;
	}
	public int getHttp_server_port() {
		return http_server_port;
	}
	public void setHttp_server_port(int http_server_port) {
		this.http_server_port = http_server_port;
	}
	public int getLog_file_keep_days() {
		return log_file_keep_days;
	}
	public void setLog_file_keep_days(int log_file_keep_days) {
		this.log_file_keep_days = log_file_keep_days;
	}
	public int getHeart_beat_interval() {
		return heart_beat_interval;
	}
	public void setHeart_beat_interval(int heart_beat_interval) {
		this.heart_beat_interval = heart_beat_interval;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public int getSync_log_buff_interval() {
		return sync_log_buff_interval;
	}
	public void setSync_log_buff_interval(int sync_log_buff_interval) {
		this.sync_log_buff_interval = sync_log_buff_interval;
	}
	public String getRun_by_user() {
		return run_by_user;
	}
	public void setRun_by_user(String run_by_user) {
		this.run_by_user = run_by_user;
	}
	public String getThread_stack_size() {
		return thread_stack_size;
	}
	public void setThread_stack_size(String thread_stack_size) {
		this.thread_stack_size = thread_stack_size;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getFile_signature_method() {
		return file_signature_method;
	}
	public void setFile_signature_method(String file_signature_method) {
		this.file_signature_method = file_signature_method;
	}
	public int getUpload_priority() {
		return upload_priority;
	}
	public void setUpload_priority(int upload_priority) {
		this.upload_priority = upload_priority;
	}
	public String getIf_alias_prefix() {
		return if_alias_prefix;
	}
	public void setIf_alias_prefix(String if_alias_prefix) {
		this.if_alias_prefix = if_alias_prefix;
	}
	public int getFile_distribute_path_mode() {
		return file_distribute_path_mode;
	}
	public void setFile_distribute_path_mode(int file_distribute_path_mode) {
		this.file_distribute_path_mode = file_distribute_path_mode;
	}
	public String getKey_namespace() {
		return key_namespace;
	}
	public void setKey_namespace(String key_namespace) {
		this.key_namespace = key_namespace;
	}
	public int getCheck_file_duplicate() {
		return check_file_duplicate;
	}
	public void setCheck_file_duplicate(int check_file_duplicate) {
		this.check_file_duplicate = check_file_duplicate;
	}
	public int getSync_wait_msec() {
		return sync_wait_msec;
	}
	public void setSync_wait_msec(int sync_wait_msec) {
		this.sync_wait_msec = sync_wait_msec;
	}
	public boolean isUse_connection_pool() {
		return use_connection_pool;
	}
	public void setUse_connection_pool(boolean use_connection_pool) {
		this.use_connection_pool = use_connection_pool;
	}
	public int getConnection_pool_max_idle_time() {
		return connection_pool_max_idle_time;
	}
	public void setConnection_pool_max_idle_time(int connection_pool_max_idle_time) {
		this.connection_pool_max_idle_time = connection_pool_max_idle_time;
	}
	public String getAccess_log_rotate_time() {
		return access_log_rotate_time;
	}
	public void setAccess_log_rotate_time(String access_log_rotate_time) {
		this.access_log_rotate_time = access_log_rotate_time;
	}
	public boolean isRotate_access_log() {
		return rotate_access_log;
	}
	public void setRotate_access_log(boolean rotate_access_log) {
		this.rotate_access_log = rotate_access_log;
	}
	public String getHttp_domain_name() {
		return http_domain_name;
	}
	public void setHttp_domain_name(String http_domain_name) {
		this.http_domain_name = http_domain_name;
	}
	public int getAccept_threads() {
		return accept_threads;
	}
	public void setAccept_threads(int accept_threads) {
		this.accept_threads = accept_threads;
	}
	public boolean isRotate_error_log() {
		return rotate_error_log;
	}
	public void setRotate_error_log(boolean rotate_error_log) {
		this.rotate_error_log = rotate_error_log;
	}
	public boolean isFile_sync_skip_invalid_record() {
		return file_sync_skip_invalid_record;
	}
	public void setFile_sync_skip_invalid_record(
			boolean file_sync_skip_invalid_record) {
		this.file_sync_skip_invalid_record = file_sync_skip_invalid_record;
	}
	public int getSync_interval() {
		return sync_interval;
	}
	public void setSync_interval(int sync_interval) {
		this.sync_interval = sync_interval;
	}
	public int getFsync_after_written_bytes() {
		return fsync_after_written_bytes;
	}
	public void setFsync_after_written_bytes(int fsync_after_written_bytes) {
		this.fsync_after_written_bytes = fsync_after_written_bytes;
	}
	public String getError_log_rotate_time() {
		return error_log_rotate_time;
	}
	public void setError_log_rotate_time(String error_log_rotate_time) {
		this.error_log_rotate_time = error_log_rotate_time;
	}
	public int getStore_path_count() {
		return store_path_count;
	}
	public void setStore_path_count(int store_path_count) {
		this.store_path_count = store_path_count;
	}
	public String getBuff_size() {
		return buff_size;
	}
	public void setBuff_size(String buff_size) {
		this.buff_size = buff_size;
	}
	@Override
	public String toString() {
		return "StorageConfig [disable=" + disable + ", bind_address="
				+ bind_address + ", base_dir=" + base_dir + ", group_name="
				+ group_name + ", write_mark_file_freq=" + write_mark_file_freq
				+ ", sync_stat_file_interval=" + sync_stat_file_interval
				+ ", store_path0=" + store_path0 + ", tracker_server="
				+ tracker_server + ", rotate_error_log_size="
				+ rotate_error_log_size + ", log_level=" + log_level
				+ ", max_connections=" + max_connections + ", run_by_group="
				+ run_by_group + ", use_access_log=" + use_access_log
				+ ", disk_rw_separated=" + disk_rw_separated
				+ ", rotate_access_log_size=" + rotate_access_log_size
				+ ", bind_addr=" + bind_addr + ", connect_timeout="
				+ connect_timeout + ", disk_reader_threads="
				+ disk_reader_threads + ", sync_binlog_buff_interval="
				+ sync_binlog_buff_interval + ", disk_writer_threads="
				+ disk_writer_threads + ", subdir_count_per_path="
				+ subdir_count_per_path + ", sync_start_time="
				+ sync_start_time + ", network_timeout=" + network_timeout
				+ ", work_threads=" + work_threads + ", stat_report_interval="
				+ stat_report_interval + ", sync_end_time=" + sync_end_time
				+ ", keep_alive=" + keep_alive
				+ ", file_distribute_rotate_count="
				+ file_distribute_rotate_count + ", allow_hosts=" + allow_hosts
				+ ", client_bind=" + client_bind + ", base_path=" + base_path
				+ ", http_server_port=" + http_server_port
				+ ", log_file_keep_days=" + log_file_keep_days
				+ ", heart_beat_interval=" + heart_beat_interval
				+ ", disabled=" + disabled + ", sync_log_buff_interval="
				+ sync_log_buff_interval + ", run_by_user=" + run_by_user
				+ ", thread_stack_size=" + thread_stack_size + ", port=" + port
				+ ", file_signature_method=" + file_signature_method
				+ ", upload_priority=" + upload_priority + ", if_alias_prefix="
				+ if_alias_prefix + ", file_distribute_path_mode="
				+ file_distribute_path_mode + ", key_namespace="
				+ key_namespace + ", check_file_duplicate="
				+ check_file_duplicate + ", sync_wait_msec=" + sync_wait_msec
				+ ", use_connection_pool=" + use_connection_pool
				+ ", connection_pool_max_idle_time="
				+ connection_pool_max_idle_time + ", access_log_rotate_time="
				+ access_log_rotate_time + ", rotate_access_log="
				+ rotate_access_log + ", http_domain_name=" + http_domain_name
				+ ", accept_threads=" + accept_threads + ", rotate_error_log="
				+ rotate_error_log + ", file_sync_skip_invalid_record="
				+ file_sync_skip_invalid_record + ", sync_interval="
				+ sync_interval + ", fsync_after_written_bytes="
				+ fsync_after_written_bytes + ", error_log_rotate_time="
				+ error_log_rotate_time + ", store_path_count="
				+ store_path_count + ", buff_size=" + buff_size + "]";
	}
	
	
}
