package freedom.hall.module.room.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import freedom.game.module.table.entity.Table;

public class Room {

	/**
	 * 房间类型
	 * xlch 血流成河
	 * xzdd 血战到底
	 * */
	public static final int TYPE_XLCH = 1000,TYPE_XZDD = 2000;
	
	private long id;
	private String sn;
	private String name;
	private int type;
	private long downLimit;
	private long topLimit;
	private long chip;
	private int maxTable;
	private List<Table> tables = new ArrayList<Table>();
	private Map<Long,Table> tableMap = new ConcurrentHashMap<Long,Table>();
	public List<Table> getTables() {
		return tables;
	}
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	public long getChip() {
		return chip;
	}
	public void setChip(long chip) {
		this.chip = chip;
	}
	public int getMaxTable() {
		return maxTable;
	}
	public void setMaxTable(int maxTable) {
		this.maxTable = maxTable;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getDownLimit() {
		return downLimit;
	}
	public void setDownLimit(long downLimit) {
		this.downLimit = downLimit;
	}
	public long getTopLimit() {
		return topLimit;
	}
	public void setTopLimit(long topLimit) {
		this.topLimit = topLimit;
	}
	@Override
	public String toString() {
		return "Room [id=" + id + ", sn=" + sn + ", name=" + name + ", type="
				+ type + ", downLimit=" + downLimit + ", topLimit=" + topLimit
				+ "]";
	}
	
	public Table getTable(long tableId)
	{
		return tableMap.get(tableId);
	}
	
	/**
	 * 房间初始化
	 * */
	public void init() 
	{
		for (int i = 0; i < maxTable; i++) 
		{
			Table table = new Table(this);
			tables.add(table);
			tableMap.put(table.getId(), table);
		}
	}
}
