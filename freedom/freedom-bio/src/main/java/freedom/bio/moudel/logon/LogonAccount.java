package freedom.bio.moudel.logon;

import freedom.bio.core.StringLen;

public class LogonAccount implements StringLen{
	private int version;
	private String machine;
	private String password;
	private String account;
	private int flags;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public int getStringLen(String field) {
		if ("machine".equals(field) || "password".equals(field))
			return 33;
		else if ("account".equals(field))
			return 32;
		return 0;
	}

	@Override
	public String toString() {
		return "LogonAccount [version=" + version + ", machine=" + machine
				+ ", password=" + password + ", account=" + account
				+ ", flags=" + flags + "]";
	}
}