package freedom.jdfs.storage;

/**
 * int alloc_size;   //alloc group count
	int count;  //group count
	FDFSGroupInfo **groups;
	FDFSGroupInfo **sorted_groups; //groups order by group_name
	FDFSGroupInfo *pStoreGroup;  //the group to store uploaded files
	int current_write_group;  //current group index to upload file
	byte store_lookup;  //store to which group, from conf file
	byte store_server;  //store to which storage server, from conf file
	byte download_server; //download from which storage server, from conf file
	byte store_path;  //store to which path, from conf file
	char store_group[FDFS_GROUP_NAME_MAX_LEN + 8];  //for 8 bytes aliginment
 * */
public class Groups {

	int alloc_size;   //alloc group count
	int count;  //group count
	GroupInfo groups;
	GroupInfo sorted_groups; //groups order by group_name
	GroupInfo pStoreGroup;  //the group to store uploaded files
	int current_write_group;  //current group index to upload file
	byte store_lookup;  //store to which group, from conf file
	byte store_server;  //store to which storage server, from conf file
	byte download_server; //download from which storage server, from conf file
	byte store_path;  //store to which path, from conf file
	String store_group;  //for 8 bytes aliginment
}
