package freedom.game.module.table.entity;

public class HuCardInfo {

	//胡牌玩家
	private long huPlayer;
	//输牌玩家
	private long loserPlayer;
	//杠牌
	private boolean gang;
	//自摸
	private boolean zimo;
	
	public HuCardInfo(long huPlayer, long loserPlayer, boolean gang, boolean zimo)
	{
		this.huPlayer = huPlayer;
		this.loserPlayer = loserPlayer;
		this.gang = gang;
		this.zimo = zimo;
	}
	public long getHuPlayer() {
		return huPlayer;
	}
	public void setHuPlayer(long huPlayer) {
		this.huPlayer = huPlayer;
	}
	public long getLoserPlayer() {
		return loserPlayer;
	}
	public void setLoserPlayer(long loserPlayer) {
		this.loserPlayer = loserPlayer;
	}
	public boolean isGang() {
		return gang;
	}
	public void setGang(boolean gang) {
		this.gang = gang;
	}
	public boolean isZimo() {
		return zimo;
	}
	public void setZimo(boolean zimo) {
		this.zimo = zimo;
	}
	
}
