package freedom.game.module.table.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freedom.game.module.room.manager.RoomManager;
import freedom.game.module.room.sender.TableMessageSender;
import freedom.game.module.table.MajongService;
import freedom.game.module.table.entity.Card;
import freedom.game.module.table.entity.Operator;
import freedom.game.module.table.entity.Operator.OPT;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.game.module.table.entity.Table.PlayingState;
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
		else if(PlayingState.WAIT_RESPONSE != table.getPlayingState())
			msg.setEx(new LogicException(-1, "请求不符合牌桌状态"));
		else
		{
			Player player = table.getPlayer(playerId);
			boolean isSelf = table.getCurrentPlayer().getId() == player.getId();
			if(player.hasOpt(opt))
			{
				if(opt == OPT.HU.ordinal())
				{
					Player currentPlayer = table.getCurrentPlayer();
					Card targetCard = isSelf ? currentPlayer.getPutCard() : currentPlayer.getOutCard();
					boolean isGangFlag = currentPlayer.isGangFlag();
					//再次检查是否能胡
					boolean hu = 
							MajongService.canHu(player.getHandCard(),isSelf ? null : targetCard);
					if(hu)
					{
						int huType = isSelf? (isGangFlag ? Player.HU_TYPE_GANG_SHANG_HUA
								: Player.HU_TYPE_ZI_MO) : (isGangFlag ? Player.HU_TYPE_GANG_SHANG_PAO
										: Player.HU_TYPE_DIAN_PAO);
						player.setHu(true,currentPlayer,targetCard,huType);
						//通知其他玩家有人胡牌
						sender.sendHuNotify(table,player);
						//下一个玩家
						Player nextPlayer = table.computeNextPlayer(player);
						table.nextPlayer(nextPlayer, PlayingState.PUT_CARD);
					}else
						msg.setEx(new LogicException(-1, "操作不合法"));
				}
				else if(opt == OPT.GANG.ordinal())
				{
					for (Operator o : player.getOpts()) 
					{
						if(o.getOpt() == OPT.GANG)
						{
							//找到了杠牌
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
											if(MajongService.sameCard(c, gangCard))
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
													table.nextPlayer(player, PlayingState.PUT_CARD);
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
											if(pengOrCard.size() == 3 && MajongService.sameCard(pengOrCard.get(0), gangCard))
											{
												pengOrCard.add(gangCard);
												//处理巴杠
												player.getHandCard().remove(gangCard);
												System.out.println("玩家" + player.getName()+"巴杠[" + gangCard+"]成功");
												//杠牌成功 , 发送杠牌通知
												sender.sendGangNotify(table, player,o,pengOrCard);
												player.setGangFlag(true);
												table.nextPlayer(player, PlayingState.PUT_CARD);
												break;
											}
										}
									}
								}
								else
								{
									//没有从打牌者OUT中删除被碰的牌	
									Card outCard = table.getCurrentPlayer().getOutCard();
									List<Card> gangGroupItem = new ArrayList<Card>(4);
									player.getGangCardList().add(gangGroupItem);
									gangGroupItem.add(outCard);
									List<Card> handCard  = player.getHandCard(); 
									for (int i = handCard.size() - 1; i >= 0; i -- )
									{
										Card c = handCard.get(i);
										if(MajongService.sameCard(c, outCard))
										{
											gangGroupItem.add(c);
											handCard.remove(i);
											if(gangGroupItem.size() == 4)
											{
												System.out.println("玩家" + player.getName()+"直杠[" + outCard+"]成功");
												//杠牌成功
												//发送杠牌通知
												sender.sendGangNotify(table, player,o,gangGroupItem);
												player.setGangFlag(true);
												table.nextPlayer(player, PlayingState.PUT_CARD);
												break;
											}
										}
									}
									
								}
							}
						}
					}
				}
				else if(opt == OPT.PENG.ordinal())
				{
					for (Operator o : player.getOpts()) 
					{
						if(o.getOpt() == OPT.PENG)
						{
							//没有从打牌者OUT中删除被碰的牌
							Card outCard = table.getCurrentPlayer().getOutCard();
							List<Card> pengGroupItem = new ArrayList<Card>(3);
							player.getPengCardList().add(pengGroupItem);
							pengGroupItem.add(outCard);
							List<Card> handCard  = player.getHandCard(); 
							for (int i = handCard.size() - 1; i >= 0; i -- )
							{
								Card c = handCard.get(i);
								if(MajongService.sameCard(c, outCard))
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
							table.nextPlayer(player, PlayingState.WAIT_PLAYER_OUT);
							//发送等待打牌通知
							//--发现原来没有通知
						}
					}
				}
				else if(opt == OPT.GUO.ordinal())
				{
					table.nextPlayerPutCard();
				}
			}
			else if(player.hasOpt() && opt == Operator.OPT.GUO.ordinal())
			{
				if(isSelf)
					table.timeoutInPlaying(PlayingState.WAIT_PLAYER_OUT, 5000);
				else
					table.nextPlayerPutCard();
			}
			else
				msg.setEx(new LogicException(-1, "玩家不能进行此操作"));
		}
		
		return msg;
	}

}
