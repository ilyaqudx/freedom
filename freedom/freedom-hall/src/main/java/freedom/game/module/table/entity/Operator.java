package freedom.game.module.table.entity;


public class Operator {

	public static enum OPT{
		GUO , HU , GANG , PENG 
	}
	
	/**
	 * 胡 : 自摸/杠上花/点炮/杠上炮(1-4)
	 * <br>
	 * 杠 : 直杠/暗杠/碰杠(5-7)
	 * */
	public static final int MARK_ZI_MO = 1,MARK_GANG_SHANG_HUA = 2,MARK_DIAN_PAO = 3,MARK_GANG_SHANG_PAO = 4,
			MARK_AN_GANG = 5,MARK_ZHI_GANG = 6,MARK_PENG_GANG = 7;
	
	private OPT opt;
	private Card target;
	private boolean self;
	private int mark;
	
	
	public Operator(OPT opt,Card target) 
	{
		this.opt = opt;
		this.target = target;
	}
	
	public Operator(OPT opt, Card target, boolean self, int mark) 
	{
		this.opt = opt;
		this.target = target;
		this.self = self;
		this.mark = mark;
	}

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		this.self = self;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public OPT getOpt() {
		return opt;
	}
	public void setOpt(OPT opt) {
		this.opt = opt;
	}
	public Card getTarget() {
		return target;
	}
	public void setTarget(Card target) {
		this.target = target;
	}
	@Override
	public String toString() 
	{
		return "Operator [opt=" + opt + ", target=" + target + "]";
	}
}
