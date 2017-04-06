tracker:
功能:
	1-管理storage组,收集storage状态
	2-根据客户端请求的连接,路由到相应的storage地址返回给客户端
	3-心跳检测
	4-tracker集群 HA
	5-leader的选择,做唯一性选择


tracker请求命令的解析

101-命令

tracker_service.c中的tracker_deal_task方法是对收到的包做转发的,
现在我们来分析101命令也就是-TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE，这个
命令是向tracker获取可用的storage服务器地址的


->根据命令转到tracker_deal_service_query_storage方法中,此方法中还处理了HA相关的逻辑

直接跳转到2747行,查看101命令的处理,调用tracker_get_writable_storage获取可用的storage,此方法
在tracker_mem.c中,storage的列表信息和状态信息都是在内存中直接处理的.

/-----选择storage group--------------
else if (g_groups.store_lookup == FDFS_STORE_LOOKUP_ROUND_ROBIN
		||g_groups.store_lookup==FDFS_STORE_LOOKUP_LOAD_BALANCE)
	{
		int write_group_index;

		bHaveActiveServer = false;
		write_group_index = g_groups.current_write_group;
		if (write_group_index >= g_groups.count)
		{
			write_group_index = 0;
		}

		pStoreGroup = NULL;
		ppFoundGroup = g_groups.sorted_groups + write_group_index;
		if ((*ppFoundGroup)->active_count > 0)
		{
			bHaveActiveServer = true;
			if (tracker_check_reserved_space(*ppFoundGroup))
			{
				pStoreGroup = *ppFoundGroup;
			}
			else if (g_if_use_trunk_file && \
				g_groups.store_lookup == \
				FDFS_STORE_LOOKUP_LOAD_BALANCE && \
				tracker_check_reserved_space_trunk( \
					*ppFoundGroup))
			{
				pStoreGroup = *ppFoundGroup;
			}
		}

		if (pStoreGroup == NULL)
		{
			FDFSGroupInfo **ppGroupEnd;
			ppGroupEnd = g_groups.sorted_groups + \
				     g_groups.count;
			for (ppGroup=ppFoundGroup+1; \
					ppGroup<ppGroupEnd; ppGroup++)
			{
				if ((*ppGroup)->active_count == 0)
				{
					continue;
				}

				bHaveActiveServer = true;
				if (tracker_check_reserved_space(*ppGroup))
				{
					pStoreGroup = *ppGroup;
					g_groups.current_write_group = \
					       ppGroup-g_groups.sorted_groups;
					break;
				}
			}

			if (pStoreGroup == NULL)
			{
				for (ppGroup=g_groups.sorted_groups; \
						ppGroup<ppFoundGroup; ppGroup++)
				{
					if ((*ppGroup)->active_count == 0)
					{
						continue;
					}

					bHaveActiveServer = true;
					if (tracker_check_reserved_space(*ppGroup))
					{
						pStoreGroup = *ppGroup;
						g_groups.current_write_group = \
						 ppGroup-g_groups.sorted_groups;
						break;
					}
				}
			}

			if (pStoreGroup == NULL)
			{
				if (!bHaveActiveServer)
				{
					pTask->length = sizeof(TrackerHeader);
					return ENOENT;
				}

				if (!g_if_use_trunk_file)
				{
					pTask->length = sizeof(TrackerHeader);
					return ENOSPC;
				}

				for (ppGroup=g_groups.sorted_groups; \
						ppGroup<ppGroupEnd; ppGroup++)
				{
					if ((*ppGroup)->active_count == 0)
					{
						continue;
					}
					if (tracker_check_reserved_space_trunk(*ppGroup))
					{
						pStoreGroup = *ppGroup;
						g_groups.current_write_group = \
						 ppGroup-g_groups.sorted_groups;
						break;
					}
				}

				if (pStoreGroup == NULL)
				{
					pTask->length = sizeof(TrackerHeader);
					return ENOSPC;
				}
			}
		}

		if (g_groups.store_lookup == FDFS_STORE_LOOKUP_ROUND_ROBIN)
		{
			g_groups.current_write_group++;
			if (g_groups.current_write_group >= g_groups.count)
			{
				g_groups.current_write_group = 0;
			}
		}
	}
/-----选择storage group-------------

/**从存储服务器组中选择具体的一个服务器*/
FDFSStorageDetail *tracker_get_writable_storage(FDFSGroupInfo *pStoreGroup)
{
	int write_server_index;//可写服务器索引
	
	//当tracker接收到upload file的请求时，会为该文件分配一个可以存储该文件的group，支持如下选择group的规则： 
	//1. Round robin，所有的group间轮询 2. Specified group，指定某一个确定的group 3. Load balance，剩余存储空间多多group优先
	//判断选择存储服务器的方法是否是轮循
	if (g_groups.store_server == FDFS_STORE_SERVER_ROUND_ROBIN)
	{
		//当前可写服务，下一次轮到下一个服务器组,采用这种方式来负载
		write_server_index = pStoreGroup->current_write_server++;
		if (pStoreGroup->current_write_server >= \
				pStoreGroup->active_count)
		{
			//如果已经是最后一组了则再重0开始
			pStoreGroup->current_write_server = 0;
		}
	
		if (write_server_index >= pStoreGroup->active_count)
		{
		    //如果选择的服务器组下标超过激活的存储组数量,则默认返回第一组
			write_server_index = 0;
		}
		//返回选中的storage group
		return  *(pStoreGroup->active_servers + write_server_index);
	}
	else //use the first server
	{
		//如果不是轮循的方式,默认选中第一个服务器
		return pStoreGroup->pStoreServer;
	}
}








storage
