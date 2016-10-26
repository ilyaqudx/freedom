package freedom.project.service;

import freedom.project.po.User;

public interface IOrderService {

	public void tradeOut(User fromUser,User toUser,long amount)throws Exception;
}
