package freedom.hall.module.task.dao;

import java.util.List;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;

import freedom.hall.module.task.entity.Task;
@DB
public interface TaskDAO {

	@SQL("select * from task where status = 0")
	public List<Task> list();
}
