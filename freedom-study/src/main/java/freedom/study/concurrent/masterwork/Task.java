package freedom.study.concurrent.masterwork;

public class Task {

	private int id;
	private String name;
	private int wage;
	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", wage=" + wage + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWage() {
		return wage;
	}
	public void setWage(int wage) {
		this.wage = wage;
	}
}
