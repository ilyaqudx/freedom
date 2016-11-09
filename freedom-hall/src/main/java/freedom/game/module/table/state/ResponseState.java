package freedom.game.module.table.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import freedom.common.kit.LogKit;
import freedom.game.GameManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.CardUtil;
import freedom.game.module.table.entity.Card;
import freedom.game.module.table.entity.Operator;
import freedom.game.module.table.entity.Operator.OPT;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.ResponseInfo;
import freedom.game.module.table.entity.Table;
import freedom.socket.command.LogicException;
/**
 * 响应状态还需要再细分(摸牌响应和打牌响应还有巴杠响应)
 * */
public class ResponseState extends GameState {

	public ResponseState(Table table) {
		super(table,4);
	}

	@Override
	public void action() 
	{
		boolean timeout = logic.isTimeout();
		if(timeout)
		{
			//帮助玩家进行响应
			try {
				if(table.responseList.isEmpty())
					table.getLogic().guo(table.responseType == Table.RESPONSE_TYPE_IN);
				else
				{
					for (ResponseInfo res : table.responseList.values()) 
					{
						Map<OPT,Operator> opts = res.getOpts();
						if(opts.containsKey(OPT.HU))
							table.getLogic().hu(table.getPlayer(res.getPlayerId()),opts.get(OPT.HU));
						else if(opts.containsKey(OPT.GANG))
							table.getLogic().gang(table.getPlayer(res.getPlayerId()), opts.get(OPT.GANG));
						else if(opts.containsKey(OPT.PENG))
							table.getLogic().peng(table.getPlayer(res.getPlayerId()), opts.get(OPT.PENG));
						else 
							table.getLogic().guo(res.isSelf());
					}
					logic.resetOpts();
				}
			}
			catch(LogicException e)
			{
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * 过逻辑
	 * */
	private void handleGuo(boolean isSelf) {
		if(isSelf)
			table.getLogic().setState(table.OUT_CARD);
		else
			table.getLogic().nextPlayerPutCard();
	}
	
	/**
	 * 胡逻辑
	 * */
	private void handleHu(Player player, boolean isSelf)
	{
		Player currentPlayer = table.getCurrentPlayer();
		Card targetCard = isSelf ? currentPlayer.getPutCard().getCard() : currentPlayer.getOutCard().getCard();
		boolean isGangFlag = currentPlayer.isGangFlag();
		//再次检查是否能胡
		boolean hu = 
				CardUtil.canHu(player.getHandCard(),isSelf ? null : targetCard);
		if(hu)
		{
			int huType = isSelf? (isGangFlag ? Player.HU_TYPE_GANG_SHANG_HUA
					: Player.HU_TYPE_ZI_MO) : (isGangFlag ? Player.HU_TYPE_GANG_SHANG_PAO
							: Player.HU_TYPE_DIAN_PAO);
			player.setHu(true,currentPlayer,targetCard,huType);
			//通知其他玩家有人胡牌
			GameManager.context.getBean(TableMessageSender.class).sendHuNotify(table,player);
			//下一个玩家
			Player nextPlayer = table.getLogic().computeNextPlayer(player);
			table.getLogic().nextPlayer(nextPlayer, table.PUT_CARD);
			LogKit.info(String.format("【%s】%s%s", player.getName(),isSelf ? "自摸" : "胡牌",targetCard.colorString()), this.getClass());
			for (Card card : player.getHandCard()) 
			{
				System.err.println(card.colorString());
			}
		}
	}
	
	/**
	 * 杠逻辑
	 * @throws LogicException 
	 * */
	private void handleGang(Player player,Operator o,boolean isSelf)
	{
		if(isSelf)
		{
			//获取杠牌
			Card gangCard = o.getTarget();//player.getCard(cardId);
			int mark      = o.getMark();
			//暗杠
			if(mark == Operator.MARK_AN_GANG)
			{
				//处理暗杠
				List<Card> gangGroupItem = new ArrayList<Card>(4);
				player.getGangCardList().add(gangGroupItem);
				//gangGroupItem.add(gangCard);
				List<Card> handCard  = player.getHandCard(); 
				for (int i = handCard.size() - 1; i >= 0; i -- )
				{
					Card c = handCard.get(i);
					if(CardUtil.sameCard(c, gangCard))
					{
						gangGroupItem.add(c);
						handCard.remove(i);
						if(gangGroupItem.size() == 4)
						{
							LogKit.info(String.format("【%s】暗杠%s",player.getName(),gangCard.colorString()),this.getClass());
							
							//杠牌成功
							//发送杠牌通知
							GameManager.context.getBean(TableMessageSender.class).sendGangNotify(table, player,o,gangGroupItem);
							player.setGangFlag(true);
							table.getLogic().nextPlayer(player, table.PUT_CARD);
							break;
						}
					}
				}
				
			}
			else if(mark == Operator.MARK_PENG_GANG)
			{
				//检查碰杠
				for (List<Card> pengOrCard : player.getPengCardList()) 
				{
					if(pengOrCard.size() == 3 && CardUtil.sameCard(pengOrCard.get(0), gangCard))
					{
						LogKit.info(String.format("【%s】巴杠%s",player.getName(),gangCard.colorString()),this.getClass());
						pengOrCard.add(gangCard);
						//处理巴杠
						player.getHandCard().remove(gangCard);
						//杠牌成功 , 发送杠牌通知
						GameManager.context.getBean(TableMessageSender.class).sendGangNotify(table, player,o,pengOrCard);
						player.setGangFlag(true);
						table.getLogic().nextPlayer(player, table.PUT_CARD);
						break;
					}
				}
			}
		}
		else
		{
			//其他玩家打牌,手上必然有3张
			System.out.println("开始处理直杠逻辑");
			Card outCard = table.getCurrentPlayer().getOutCard().getCard();
			List<Card> gangCardGroup = new ArrayList<Card>(4);
			List<Card> handCardGroup = player.getHandCard();
			for (Card card : handCardGroup) 
			{
				if(CardUtil.sameCard(card, outCard))
					gangCardGroup.add(card);
				if(gangCardGroup.size() == 3)
					break;
			}
			
			if(gangCardGroup.size() == 3)
			{
				System.out.println("成功找到要杠的牌,从手牌中删除这3张牌");
				for (Card card : gangCardGroup)
				{
					handCardGroup.remove(card);
				}
				gangCardGroup.add(outCard);
				
				LogKit.info(String.format("【%s】直杠%s",player.getName(),outCard.colorString()),this.getClass());
				//杠牌成功
				//发送杠牌通知
				GameManager.context.getBean(TableMessageSender.class).sendGangNotify(table, player,o,gangCardGroup);
				player.setGangFlag(true);
				table.getLogic().nextPlayer(player, table.PUT_CARD);
			}
		}
	}
	
	/**
	 * 碰逻辑
	 * */
	private void handlePeng(Player player) {
		//没有从打牌者OUT中删除被碰的牌
		Card outCard = table.getCurrentPlayer().getOutCard().getCard();
		List<Card> pengGroupItem = new ArrayList<Card>(3);
		player.getPengCardList().add(pengGroupItem);
		pengGroupItem.add(outCard);
		List<Card> handCard  = player.getHandCard(); 
		for (int i = handCard.size() - 1; i >= 0; i -- )
		{
			Card c = handCard.get(i);
			if(CardUtil.sameCard(c, outCard))
			{
				pengGroupItem.add(c);
				handCard.remove(i);
				if(pengGroupItem.size() == 3)
					break;
			}
		}
		//碰牌成功
		//发送碰牌通知
		GameManager.context.getBean(TableMessageSender.class).sendPengNotify(table, player);
		table.getLogic().nextPlayer(player, table.OUT_CARD);
	}
}
