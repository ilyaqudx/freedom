package freedom.hall.module.task.vo;

import java.util.ArrayList;
import java.util.List;

import freedom.hall.module.task.entity.Task;
import freedom.hall.module.task.vo.ReqTaskMessage.Out;
import freedom.socket.command.CommandMessage;

public class ReqTaskMessage extends CommandMessage<Void,Out> {

	public static final class Out{
		private String taskTypeName;
		private List<Task> tasks = new ArrayList<Task>();
		
		public String getTaskTypeName() {
			return taskTypeName;
		}

		public void setTaskTypeName(String taskTypeName) {
			this.taskTypeName = taskTypeName;
		}

		public List<Task> getTasks() {
			return tasks;
		}

		public void setTasks(List<Task> tasks) {
			this.tasks = tasks;
		}

		@Override
		public String toString() {
			return "Out [tasks=" + tasks + "]";
		}
	}
}
