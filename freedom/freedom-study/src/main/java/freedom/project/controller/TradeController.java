package freedom.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import freedom.project.dao.impl.UserDaoImpl;
import freedom.project.po.User;
import freedom.project.service.IOrderService;

@RestController
public class TradeController {

	@Autowired
	private UserDaoImpl userDAO;
	@Autowired
	private IOrderService orderService;
	@RequestMapping("/trade/out")
	public String tradeOut(long from,long to,long amount) throws Exception
	{
		User fromUser = userDAO.get(from);
		User toUser   = userDAO.get(to);
		if(fromUser == null || toUser == null)
			throw new Exception("from or to userId not exist");
		orderService.tradeOut(fromUser, toUser, amount);
		return JSON.toJSONString(fromUser);
	}
}
