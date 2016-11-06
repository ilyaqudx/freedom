package freedom.study.interview.design.fixed.icpay.po;
/**
 * 员工
 * */
public class Employee {

	
	
	public Employee(long id, String name, ICCard card)
	{
		this.id = id;
		this.name = name;
		this.card = card;
	}
	private long id;
	private String name;
	private ICCard card;
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
	public ICCard getCard() {
		return card;
	}
	public void setCard(ICCard card) {
		this.card = card;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", card=" + card + "]";
	}
}
