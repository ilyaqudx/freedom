package freedom.common.moudle.user;

import java.util.Date;

public class User {

	private long id;
	private String name;
	private int sex;
	private String password;
	private long gold;
	private String imei;
	private Date createTime;
	
	public User() {
	}
	public User(String name, int sex, long gold) {
		super();
		this.name = name;
		this.sex = sex;
		this.gold = gold;
	}
	public long getGold() {
		return gold;
	}
	public void setGold(long gold) {
		this.gold = gold;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", sex=" + sex
				+ ", password=" + password + ", imei=" + imei + ", createTime="
				+ createTime + "]";
	}
}
