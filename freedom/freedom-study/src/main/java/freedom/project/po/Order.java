package freedom.project.po;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class Order extends Model{

	private String orderNo;
	private long   fromId;
	private long   toId;
	private int    status;
	private Date   createTime;
	private Date   updateTime;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public long getFromId() {
		return fromId;
	}
	public void setFromId(long fromId) {
		this.fromId = fromId;
	}
	public long getToId() {
		return toId;
	}
	public void setToId(long toId) {
		this.toId = toId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "Order [orderNo=" + orderNo + ", fromId=" + fromId + ", toId=" + toId + ", status=" + status
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
	public static final class OrderRowMapper implements RowMapper<Order>{

		@Override
		public Order mapRow(ResultSet set, int rowNum) throws SQLException
		{
			Order order = new Order();
			order.setOrderNo(set.getString("orderNo"));
			order.setFromId(set.getLong("fromId"));
			order.setToId(set.getLong("toId"));
			order.setStatus(set.getInt("status"));
			java.sql.Date createTime = set.getDate("createTime");
			java.sql.Date updateTime = set.getDate("updateDate");
			order.setCreateTime(null == createTime ? null : new Date(createTime.getTime()));
			order.setUpdateTime(null == updateTime ? null : new Date(updateTime.getTime()));
			return order;
		}
		
	}
}
