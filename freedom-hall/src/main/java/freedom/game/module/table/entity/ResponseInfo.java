package freedom.game.module.table.entity;

import java.util.HashMap;
import java.util.Map;

import freedom.game.module.table.entity.Operator.OPT;

public class ResponseInfo {
	private long playerId;
	//响应类型
	private int responseType;
	//事件目标玩家(即摸牌人或打牌人)
	private long targetPlayerId;
	//<玩家id<操作id,操作>>
	private Map<OPT,Operator> opts = new HashMap<OPT,Operator>();
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getResponseType() {
		return responseType;
	}
	public void setResponseType(int responseType) {
		this.responseType = responseType;
	}
	public long getTargetPlayerId() {
		return targetPlayerId;
	}
	public void setTargetPlayerId(long targetPlayerId) {
		this.targetPlayerId = targetPlayerId;
	}
	
	public Map<OPT, Operator> getOpts() {
		return opts;
	}
	public void setOpts(Map<OPT, Operator> opts) {
		this.opts = opts;
	}
	/**
	 * 本家响应
	 * */
	public boolean isSelf()
	{
		return targetPlayerId == playerId;
	}
}
