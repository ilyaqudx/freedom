package freedom.game.module.table.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freedom.game.module.room.manager.RoomManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.CardUtil;
import freedom.game.module.table.entity.Card;
import freedom.game.module.table.entity.Operator;
import freedom.game.module.table.entity.Operator.OPT;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.game.module.table.message.command.ResponseOptMessage;
import freedom.hall.Cmd;
import freedom.socket.command.AbstractCommand;
import freedom.socket.command.LogicException;

@Service(Cmd.Req.RESPONSE_OPT)
public class ResponseOptCommand extends AbstractCommand<ResponseOptMessage> {

	@Autowired
	private RoomManager roomManager;
	@Autowired
	private TableMessageSender sender;
	@Override
	public ResponseOptMessage execute(ResponseOptMessage msg) throws Exception 
	{
		long playerId = msg.getIn().getPlayerId();
		int  opt      = msg.getIn().getOpt();
		int  cardId   = msg.getIn().getCardId();
		Table table = roomManager.getTable(playerId);
		if(null == table)
			msg.setEx(new LogicException(-1, "玩家不在牌桌上"));
		else if(table.RESPONSE.V() != table.getState().V())
			msg.setEx(new LogicException(-1, "请求不符合牌桌状态"));
		else
		{
			Player player = table.getPlayer(playerId);
			boolean isSelf = table.getCurrentPlayer().getId() == player.getId();
			if(player.hasOpt(opt))
			{
				if(opt == OPT.HU.ordinal())
					handleHu(msg, table, player, isSelf);
				else if(opt == OPT.GANG.ordinal())
					handleGang(cardId, table, player, isSelf);
				else if(opt == OPT.PENG.ordinal())
					handlePeng(table, player);
				else if(opt == OPT.GUO.ordinal())
					handleGuo(table, isSelf);
			}
			else
				msg.setEx(new LogicException(-1, "玩家不能进行此操作"));
		}
		
		return msg;
	}
	
	/**
	 * 过逻辑
	 * */
	private void handleGuo(Table table, boolean isSelf) {
		if(isSelf)
			table.getLogic().setState(table.OUT_CARD);
		else
			table.getLogic().nextPlayerPutCard();
	}
	
	/**
	 * 碰逻辑
	 * */
	private void handlePeng(Table table, Player player) {
		for (Operator o : player.getOpts()) 
		{
			if(o.getOpt() == OPT.PENG)
			{
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
				sender.sendPengNotify(table, player);
				table.getLogic().nextPlayer(player, table.OUT_CARD);
				//发送等待打牌通知
				//--发现原来没有通知
			}
		}
	}
	
	/**
	 * 杠逻辑
	 * @throws LogicException 
	 * */
	private void handleGang(int cardId, Table table, Player player, boolean isSelf) throws LogicException {
		for (Operator o : player.getOpts()) 
		{
			if(o.getOpt() == OPT.GANG)
			{
				//找到了杠牌,BUG-1应该比较的颜色和值,应该是客户端传上来的牌的ID不对(JSON序列化引起的循环引用,没有赋值.需要解决)
				if(o.getTarget().getId() == cardId)
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
										System.out.println("玩家 : " + player.getName()+"暗杠[" + gangCard+"]成功");
										
										//杠牌成功
										//发送杠牌通知
										sender.sendGangNotify(table, player,o,gangGroupItem);
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
									pengOrCard.add(gangCard);
									//处理巴杠
									player.getHandCard().remove(gangCard);
									System.out.println("玩家" + player.getName()+"巴杠[" + gangCard+"]成功");
									//杠牌成功 , 发送杠牌通知
									sender.sendGangNotify(table, player,o,pengOrCard);
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
							
							System.out.println("玩家" + player.getName()+"直杠【" + outCard.colorString() +"】成功");
							//杠牌成功
							//发送杠牌通知
							sender.sendGangNotify(table, player,o,gangCardGroup);
							player.setGangFlag(true);
							table.getLogic().nextPlayer(player, table.PUT_CARD);
						}
						else
							throw new LogicException(-1, "玩家手牌中没有3张 : " + outCard.colorString());
					}
				}
			}
		}
	}
	
	/**
	 * 胡逻辑
	 * */
	private void handleHu(ResponseOptMessage msg, Table table, Player player, boolean isSelf) {
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
			sender.sendHuNotify(table,player);
			//下一个玩家
			Player nextPlayer = table.getLogic().computeNextPlayer(player);
			table.getLogic().nextPlayer(nextPlayer, table.PUT_CARD);
		}else
			msg.setEx(new LogicException(-1, "操作不合法"));
	}

}
