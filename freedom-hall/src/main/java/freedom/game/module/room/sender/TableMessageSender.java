package freedom.game.module.room.sender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freedom.common.constants.Consts;
import freedom.game.SessionManager;
import freedom.game.module.table.entity.Card;
import freedom.game.module.table.entity.Operator;
import freedom.game.module.table.entity.Player;
import freedom.game.module.table.entity.Table;
import freedom.game.module.table.message.notify.NotifyEntryRoomMessage;
import freedom.game.module.table.message.notify.NotifyGangMessage;
import freedom.game.module.table.message.notify.NotifyHuMessage;
import freedom.game.module.table.message.notify.NotifyMessage;
import freedom.game.module.table.message.notify.NotifyPengMessage;
import freedom.game.module.table.message.notify.NotifyPlayerPutCard;
import freedom.game.module.table.message.notify.NotifyReadyMessage;
import freedom.game.module.table.message.notify.NotifySelectLackCardResultMessage;
import freedom.game.module.table.message.notify.NotifySelectLackCardResultMessage.Item;
import freedom.game.module.table.message.notify.NotifyStartGameFirstPutCardMessage;
import freedom.game.module.table.message.notify.NotifyStartSelectLackCardMessage;
import freedom.game.module.table.message.notify.NotifyWaitResponseMessage;
import freedom.hall.Cmd;
import freedom.socket.server.message.Response;

@Component
public class TableMessageSender {

	@Autowired
	private SessionManager sessionManager;
	
	public final void sendEntryRoomMessage(Player player,Table table)
	{
		NotifyEntryRoomMessage message = new NotifyEntryRoomMessage();
		message.setPlayer(player);
		message.setCmd(Cmd.Notify.ENTRY_ROOM);
		
		List<Player> players = table.getUsers();
		for (Player p : players) 
		{
			if(p.getId() == player.getId())
				continue;
			send(p,message);
		}
	}
	
	/**
	 * 准备消息
	 * */
	public final void sendReadyMessage(Player player,Table table)
	{
		NotifyReadyMessage message = new NotifyReadyMessage();
		message.setPlayerId(player.getId());
		message.setSeat(player.getSeat());
		message.setCmd(Cmd.Notify.READY);
		
		List<Player> players = table.getUsers();
		for (Player p : players) 
		{
			if(p.getId() == player.getId())
				continue;
			send(p,message);
		}
	}
	
	public void sendStartGameFirstPutCard(Table table)
	{
		List<Player> players = table.getUsers();
		for (Player player : players) 
		{
			NotifyStartGameFirstPutCardMessage
			message = new NotifyStartGameFirstPutCardMessage();
			message.setCmd(Cmd.Notify.START_GAME);
			message.setFirstSeat(table.getFirstSeat());
			message.setHandCard(player.getHandCard());
			
			send(player, message);
		}
		
	}

	private void send(Player p,NotifyMessage message)
	{
		if(p.getRobot() == Consts.FALSE)
		{
			Response response = new Response();
			response.setMid(0);
			response.setOneWay((byte) 1);
			response.setBody(message);
			response.setCode(Consts.SUCCESS_SHORT);
			response.setCmd(message.getCmd());
			sessionManager.get(p.getId()).writeAndFlush(response);
		}
	}

	/**
	 * 通知玩家开始选择缺牌
	 * */
	public void sendStartSelectLackCard(Table table) 
	{
		NotifyStartSelectLackCardMessage message = 
				new NotifyStartSelectLackCardMessage();
		message.setCmd(Cmd.Notify.START_SELECT_LACK_COLOR);
		List<Player> players = table.getUsers();
		for (Player player : players) 
		{
			send(player, message);
		}
	}
	
	/**
	 * 通知玩家选择缺牌结果(正式开始游戏)
	 * */
	public void sendSelectLackCardResult(Table table) 
	{
		List<Player> players = table.getUsers();
		List<Item> results = new ArrayList<Item>(players.size());
		for (Player player : players) 
		{
			Item result = new Item();
			result.setUserId(player.getId());
			result.setColor(player.getSelectLackColor());
			results.add(result);
		}
		
		for (Player p : players) 
		{
			NotifySelectLackCardResultMessage message = 
					new NotifySelectLackCardResultMessage(Cmd.Notify.SELECT_LACK_COLOR_RESULT
							,table.getLogic().getTimeout(),results);
			send(p, message);
		}
	}
	
	/**
	 * 广播玩家摸牌
	 * */
	public void sendPutCardInPlaying(Table table)
	{
		//当前玩家
		Player currentPlayer = table.getCurrentPlayer();
		Card putCard = currentPlayer.getPutCard().getCard();
		for (Player p : table.getUsers()) 
		{
			NotifyPlayerPutCard message = 
					new NotifyPlayerPutCard(Cmd.Notify.PLAYER_PUT_CARD, currentPlayer.getId(), 
							currentPlayer.getId() == p.getId() ? putCard : null);
			send(p, message);
		}
	}

	public void sendPlayerOutCard(Table table) 
	{
		//当前玩家
		Player currentPlayer = table.getCurrentPlayer();
		Card   card          = currentPlayer.getOutCard().getCard();
		NotifyPlayerPutCard message = 
				new NotifyPlayerPutCard(Cmd.Notify.PLAYER_OUT_CARD, currentPlayer.getId(), card);
		for (Player p : table.getUsers()) 
		{
			send(p, message);
		}
	}

	/**
	 * 发送等待玩家响应通知
	 * */
	public void sendWaitResponseAfterOutCard(Table table)
	{
		for (Player p : table.getUsers()) 
		{
			NotifyWaitResponseMessage message = 
					new NotifyWaitResponseMessage(Cmd.Notify.WAIT_RESPONSE,null);
			if(p.hasOpt())
				message.setOpts(p.getOpts());
			send(p, message);
		}
	}
	
	/**
	 * 摸牌后可进行的操作(杠、胡操作),只发送给玩家本身,其他玩家不发,
	 * 并且这个时间不单独计算响应时间,因为如果单独算时间,其他玩家就知道你可能有杠或者是可胡了.
	 * */
	public void sendWaitResponseAfterPutCard(Table table)
	{
			Player p = table.getCurrentPlayer();
			NotifyWaitResponseMessage message = 
					new NotifyWaitResponseMessage(Cmd.Notify.WAIT_RESPONSE,p.getOpts());
			send(p, message);
	}
	
	public void sendPengNotify(Table table,Player operatorPlayer)
	{
		LinkedList<List<Card>> pengGroupList = operatorPlayer.getPengCardList();
		List<Card> pengList = pengGroupList.getLast().subList(1, 3);
		NotifyPengMessage
			message = new NotifyPengMessage(Cmd.Notify.OPERATOR_PENG,operatorPlayer.getId(),pengList);
		for (Player p : table.getUsers()) 
		{
			send(p,message);
		}
	}

	public void sendHuNotify(Table table, Player player) 
	{
		NotifyHuMessage message = 
				new NotifyHuMessage(Cmd.Notify.OPERATOR_HU,
						player.getId(),player.getHuType(), player.getHuCard());
		for (Player p : table.getUsers()) {
			send(p,message);
		}
	}

	public void sendGangNotify(Table table, Player operatorPlayer,Operator operator,List<Card> gangCardItem) 
	{
		int gangMark = operator.getMark();
		int startIndex = gangMark == Operator.MARK_AN_GANG ? 0 : (gangMark == Operator.MARK_ZHI_GANG ? 1 : 3);
		List<Card> pengList = gangCardItem.subList(startIndex, 4);
		NotifyGangMessage
			message = new NotifyGangMessage(Cmd.Notify.OPERATOR_GANG,operatorPlayer.getId(),pengList,operator.getMark());
		for (Player p : table.getUsers()) 
		{
			send(p,message);
		}
	}
}
