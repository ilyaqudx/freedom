package freedom.hall.module.task.command;

import java.util.List;

import org.springframework.stereotype.Component;

import freedom.common.dao.DBFactory;
import freedom.hall.Cmd;
import freedom.hall.module.task.dao.TaskDAO;
import freedom.hall.module.task.entity.Task;
import freedom.hall.module.task.vo.ReqTaskMessage;
import freedom.hall.module.task.vo.ReqTaskMessage.Out;
import freedom.socket.command.AbstractCommand;

@Component(Cmd.Req.TASK_LIST)
public class TaskCommand extends AbstractCommand<ReqTaskMessage> {

	@Override
	public ReqTaskMessage execute(ReqTaskMessage msg) throws Exception 
	{
		List<Task> tasks = DBFactory.getDAO(TaskDAO.class).list();
		
		Out out = new freedom.hall.module.task.vo.ReqTaskMessage.Out();
		out.setTaskTypeName("PPP任务");
		out.setTasks(tasks);
		return (ReqTaskMessage) msg.setOut(out).setResCmd(Cmd.Res.TASK_LIST);
	}

}
