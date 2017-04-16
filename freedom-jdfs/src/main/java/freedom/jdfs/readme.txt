tracker:
功能:
	1-管理storage组,收集storage状态
	2-根据客户端请求的连接,路由到相应的storage地址返回给客户端
	3-心跳检测
	4-tracker集群 HA
	5-leader的选择,做唯一性选择


storage

	1-线程模型reactor
		线程READ逻辑:
		线程WRITE逻辑:
	2-磁盘线程读写分离的实现
	3-storage path index的选择规则/data目录的生成/上传文件名规则
	4-StorageTaskPool实现/fast_timer_info还不知道干什么的
	5-binlog日志规则
	6-sync同步规则 
	7-同group storages sync规则
	
	
question:
	如何检测data目录已经初始化?
		猜测是第一次初始化完成后,将状态保存在了某个文件中,具体是哪个文件需要看源码 参考方法:storage_check_and_make_data_dirs
		
		data创建后同时在DATA下面创建了一个文件 .data_init_flag,下次启动先判断该文件,同时该文件内还保存了STORAGE的一些其他信息
		
	如何检测data目录下的实际存储目录(1,2级)是否已存在
		实际上是通过检查一级目录的00目录和FF目录是否存在来判断是否需要创建存储目录  - 参考方法:storage_make_data_dirs
		sprintf(min_sub_path, FDFS_STORAGE_DATA_DIR_FORMAT"/"FDFS_STORAGE_DATA_DIR_FORMAT,0, 0);
		sprintf(max_sub_path, FDFS_STORAGE_DATA_DIR_FORMAT"/"FDFS_STORAGE_DATA_DIR_FORMAT,g_subdir_count_per_path-1, g_subdir_count_per_path-1);
		if (fileExists(min_sub_path) && fileExists(max_sub_path))
		{
			return 0;
		}
	storage服务器配置参数解析及初始化 参考: storage_func_init
	
	
	上传文件过程中,StorageTask/StorageClientInfo/StorageFileContext中的offset和bytes的意义
	
	StorageTask
		length : 一个请求数据可能会大于256K,会被分为多次处理。length代表一次的数据量
		offset : 一次数据的偏移量(主要是在从内核COPY数据到BUFFER中使用,因为这一次的数据也不一定只读一次就完成了)
		size   : 一次最大的数据量，默认为256K
	StorageClientInfo:
		total_length: 整个请求的总长度 : Header(10) + Body[store_path_index:1,file_length:8,ext_file_name:6] + 文件所有字节长度
		total_offset: 整个请求的偏移量
	StorageFileContext:
		buff_offset: 当次写入磁盘StorageTask buffer的偏移量(首次写入磁盘时，buffer中是包含有Header和body其他数据的,所以需要从BUFF的偏移量开始)
		start : 整个文件的开始字节
		end   : 整个文件的结束字节
		offset: 整个文件的偏移量(在写入时进行seek的偏移量)
		
2017-04-14  对NIOPROCESSOR 的SOCKET读取和数据的解码进行重构
	