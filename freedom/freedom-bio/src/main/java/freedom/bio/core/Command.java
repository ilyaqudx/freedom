package freedom.bio.core;

public interface Command<I,O> {

	public static final class Main{
		static final short CORE  = 0;
		static final short GAME  = 1;
	}
	
	public static final class Sub{
		static final short ACCOUNT_LOGON = 2;
	}
	
	public PacketRes execute(IoSession session,byte[] data) throws Exception;
}
