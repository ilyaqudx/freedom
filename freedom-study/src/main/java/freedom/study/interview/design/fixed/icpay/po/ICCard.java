package freedom.study.interview.design.fixed.icpay.po;
/**
 * IC卡(可以进行消费)
 * */
public class ICCard {

	private String cardNo;
	private int type;
	private double fixedMoney;
	private double freeMoney;
	public ICCard(String cardNo, int type, double fixedMoney, double freeMoney) {
		super();
		this.cardNo = cardNo;
		this.type = type;
		this.fixedMoney = fixedMoney;
		this.freeMoney = freeMoney;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getFixedMoney() {
		return fixedMoney;
	}
	public void setFixedMoney(double fixedMoney) {
		this.fixedMoney = fixedMoney;
	}
	public double getFreeMoney() {
		return freeMoney;
	}
	public void setFreeMoney(double freeMoney) {
		this.freeMoney = freeMoney;
	}
	@Override
	public String toString() {
		return "ICCard [cardNo=" + cardNo + ", type=" + type + ", fixedMoney=" + fixedMoney + ", freeMoney=" + freeMoney
				+ "]";
	}
}
