package freedom.bio.moudel.room;

import freedom.bio.core.StringLen;

public class CreateRoom implements StringLen {
	private byte gameType, gameCount, gameTypeIndex;
	private int gameRuleIndex;
	private String httpChannel;// 32 HTTP获取(什么东东)

	@Override
	public String toString() {
		return "CreateRoom [gameType=" + gameType + ", gameCount=" + gameCount
				+ ", gameTypeIndex=" + gameTypeIndex + ", gameRuleIndex="
				+ gameRuleIndex + ", httpChannel=" + httpChannel + "]";
	}

	public byte getGameType() {
		return gameType;
	}

	public void setGameType(byte gameType) {
		this.gameType = gameType;
	}

	public byte getGameCount() {
		return gameCount;
	}

	public void setGameCount(byte gameCount) {
		this.gameCount = gameCount;
	}

	public byte getGameTypeIndex() {
		return gameTypeIndex;
	}

	public void setGameTypeIndex(byte gameTypeIndex) {
		this.gameTypeIndex = gameTypeIndex;
	}

	public int getGameRuleIndex() {
		return gameRuleIndex;
	}

	public void setGameRuleIndex(int gameRuleIndex) {
		this.gameRuleIndex = gameRuleIndex;
	}

	public String getHttpChannel() {
		return httpChannel;
	}

	public void setHttpChannel(String httpChannel) {
		this.httpChannel = httpChannel;
	}

	@Override
	public int getStringLen(String field) {
		return 32;
	}
}