package freedom.bio.moudel.logon;


public class LogonUtils {

	public static final LogonSuccess buildLogonSuccess()
	{
		LogonSuccess logon = new LogonSuccess();
		logon.setInsureEnabled((byte) 1);
		logon.setGender((byte) 1);
		logon.setMachine((byte) 0);
		logon.setShowServerStatus((byte) 0);
		logon.setFaceId((short) 110);
		logon.setUserId(10086);
		logon.setGameId(1001);
		logon.setGroupId(10086);
		logon.setCustomId(10086);
		logon.setUserMedal(5);
		logon.setExp(9999);
		logon.setLoveliness(10);
		logon.setSpreaderId(1);
		logon.setUserScore(88888);
		logon.setUserInsure(55558888);
		logon.setAccount("WxAccount");
		logon.setNickName("wxNickName");
		logon.setGroupName("wxGroupName");
		return logon;
	}
}
