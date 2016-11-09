package freedom.bio.moudel.logon;

import freedom.bio.core.StringLen;

public class LogonSuccess implements StringLen {
	// 注意字段的顺序,一定要严格保证,反射时按照书写顺序进行序列
	private short faceId;
	private int userId, gameId, groupId, customId, userMedal, exp, loveliness,
			spreaderId;
	private byte insureEnabled;
	private long userScore, userInsure;
	private byte gender, machine;
	private String account, nickName, groupName;
	private byte showServerStatus;

	public byte getInsureEnabled() {
		return insureEnabled;
	}

	public void setInsureEnabled(byte insureEnabled) {
		this.insureEnabled = insureEnabled;
	}

	public byte getGender() {
		return gender;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public byte getMachine() {
		return machine;
	}

	public void setMachine(byte machine) {
		this.machine = machine;
	}

	public byte getShowServerStatus() {
		return showServerStatus;
	}

	public void setShowServerStatus(byte showServerStatus) {
		this.showServerStatus = showServerStatus;
	}

	public short getFaceId() {
		return faceId;
	}

	public void setFaceId(short faceId) {
		this.faceId = faceId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getCustomId() {
		return customId;
	}

	public void setCustomId(int customId) {
		this.customId = customId;
	}

	public int getUserMedal() {
		return userMedal;
	}

	public void setUserMedal(int userMedal) {
		this.userMedal = userMedal;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getLoveliness() {
		return loveliness;
	}

	public void setLoveliness(int loveliness) {
		this.loveliness = loveliness;
	}

	public int getSpreaderId() {
		return spreaderId;
	}

	public void setSpreaderId(int spreaderId) {
		this.spreaderId = spreaderId;
	}

	public long getUserScore() {
		return userScore;
	}

	public void setUserScore(long userScore) {
		this.userScore = userScore;
	}

	public long getUserInsure() {
		return userInsure;
	}

	public void setUserInsure(long userInsure) {
		this.userInsure = userInsure;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getStringLen(String field) {
		return 32;
	}

	@Override
	public String toString() {
		return "LogonSuccess [insureEnabled=" + insureEnabled + ", gender="
				+ gender + ", machine=" + machine + ", showServerStatus="
				+ showServerStatus + ", faceId=" + faceId + ", userId="
				+ userId + ", gameId=" + gameId + ", groupId=" + groupId
				+ ", customId=" + customId + ", userMedal=" + userMedal
				+ ", exp=" + exp + ", loveliness=" + loveliness
				+ ", spreaderId=" + spreaderId + ", userScore=" + userScore
				+ ", userInsure=" + userInsure + ", account=" + account
				+ ", nickName=" + nickName + ", groupName=" + groupName + "]";
	}
}