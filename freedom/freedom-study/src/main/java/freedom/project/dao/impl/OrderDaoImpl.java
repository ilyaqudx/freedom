package freedom.project.dao.impl;

import org.springframework.stereotype.Service;

import freedom.project.dao.IOrderDao;
import freedom.project.po.Order;
import freedom.project.po.Order.OrderRowMapper;
@Service
public class OrderDaoImpl extends BaseDaoImpl<Order,OrderRowMapper> implements IOrderDao {

	@Override
	public int update(Order entity)
	{
		return template.update("UPDATE Order SET status = ? , updateTime = ? WHERE orderNo = ?", entity);
	}

}
