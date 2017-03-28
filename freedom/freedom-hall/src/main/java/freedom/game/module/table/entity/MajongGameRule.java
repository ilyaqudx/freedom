package freedom.game.module.table.entity;

public abstract class MajongGameRule implements GameRule{

	//椅子数
	private int chairCount;
	//是否允许么九
	private int hasYaoJiu;
	//是否允许将对
	private int hasJiangDui;
	//是否允许金勾钓
	private int hasJinGouDiao;
	//是否允许海底捞
	private int hasHaiDiLao;
	//是否允许未过庄胡大
	private int hasHuDaBeforeGuoZhuang;
	
	//胡
	private int hasHu;
	//杠
	private int hasGang;
	//碰
	private int hasPeng;
	//吃
	private int hasChi;
	//是否一炮多响
	private int hasMultiPao;
	//是否自摸加番
	private int hasZimoJiaFan;
	//是否自摸加底
	private int hasZimoJiaDi;
	//查叫查大还是查小(1-大,0-小)
	private int peiJiaoDaOrXiao;
	
	//是否定缺
	private int hasDingQue;
	//是否换三张
	private int hasHuanSanZhang;
	//最大番(如果是-1则代表不限番)
	private int maxFan;
	
	public MajongGameRule()
	{
		
	}
}
