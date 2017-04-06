package freedom.jdfs.storage;
/**
 * char id[FDFS_STORAGE_ID_MAX_SIZE];
	char group_name[FDFS_GROUP_NAME_MAX_LEN + 8];  //for 8 bytes alignment
	char ip_addr[IP_ADDRESS_SIZE];
    int port;   //since v5.05
 * */
public class StorageIdInfo {

	String id;
	String group_name;  //for 8 bytes alignment
	String ip_addr;
    int port;   //since v5.05
}
