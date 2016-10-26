package freedom.project.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freedom.project.dao.IOrderDao;
import freedom.project.po.Order;
import freedom.project.po.User;
import freedom.project.service.IOrderService;
@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private IOrderDao orderDAO;
	
	@Override
	public void tradeOut(User fromUser, User toUser, long amount) throws Exception 
	{
		if(fromUser.getMoney() >= amount)
		{
			//create trade order
			Order order = new Order();
			order.setOrderNo(UUID.randomUUID().toString());
			order.setFromId(fromUser.getId());
			order.setToId(toUser.getId());
			order.setCreateTime(new Date());
			order.setStatus(1);
			orderDAO.insert(order);
			//remote execute operator
			Thread.sleep(10000);
			//update local trade order
		}
		throw new Exception(String.format("%s余额不足", fromUser.getName()));
	}

}
