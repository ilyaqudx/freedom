package freedom.hall;

public interface Cmd {

	public static interface Req{
		public static final String LOGIN = "3001";
		public static final String TASK_LIST = "4001";
		public static final String SELECT_ROOM = "4003";
		public static final String PRODUCT_LIST = "4005";
		public static final String ENTRY_ROOM = "4007";
		public static final String READY = "4009";
		public static final String SELECT_LACK_CARD = "4011";//无单独返回 oneway
		public static final String OUT_CARD = "4013";//无单独返回 oneway
		public static final String RESPONSE_OPT = "4015";//响应操作 无单独返回
		
	}
	
	public static interface Res{
		public static final String LOGIN = "3002";
		public static final String TASK_LIST = "4002";
		public static final String SELECT_ROOM = "4004";
		public static final String PRODUCT_LIST = "4006";
		public static final String ENTRY_ROOM = "4008";
		public static final String READY = "4010";
	}
	
	public static interface Notify{
		public static final String ENTRY_ROOM = "8002";
		public static final String READY = "8004";
		public static final String START_GAME = "8006";
		public static final String START_SELECT_LACK_COLOR = "8008";
		public static final String SELECT_LACK_COLOR_RESULT = "8010";
		public static final String PLAYER_PUT_CARD = "8012";//通知有玩家摸了牌
		public static final String PLAYER_OUT_CARD = "8014";//通知有玩家打了牌
		public static final String WAIT_RESPONSE   = "8016";//打牌/碰杠等待玩家响应
		public static final String OPERATOR_PENG   = "8018";//玩家碰操作
		public static final String OPERATOR_GANG   = "8020";//玩家杠操作
		public static final String OPERATOR_HU     = "8022";//玩家胡操作
		
		
		
	}
}
