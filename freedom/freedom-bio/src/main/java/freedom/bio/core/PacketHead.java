package freedom.bio.core;

public final class PacketHead {
	private byte dataKindId = 2, checkCode = 1;
	private short packetSize, mainCmd, subCmd;

	public byte getDataKindId() {
		return dataKindId;
	}

	public void setDataKindId(byte dataKindId) {
		this.dataKindId = dataKindId;
	}

	public byte getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(byte checkCode) {
		this.checkCode = checkCode;
	}

	public short getPacketSize() {
		return packetSize;
	}

	public void setPacketSize(short packetSize) {
		this.packetSize = packetSize;
	}

	public short getMainCmd() {
		return mainCmd;
	}

	public void setMainCmd(short mainCmd) {
		this.mainCmd = mainCmd;
	}

	public short getSubCmd() {
		return subCmd;
	}

	public void setSubCmd(short subCmd) {
		this.subCmd = subCmd;
	}

	@Override
	public String toString() {
		return "PackageHead [dataKindId=" + dataKindId + ", checkCode="
				+ checkCode + ", packetSize=" + packetSize + ", mainCmd="
				+ mainCmd + ", subCmd=" + subCmd + "]";
	}
}