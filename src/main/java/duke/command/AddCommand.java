package duke.command;

import duke.storage.Storage;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.ToDo;
import duke.tasklist.TaskList;
import duke.ui.Ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * duke.command.AddCommand that deals with the adding of new duke.task.Task objects to the duke.tasklist.TaskList
 */
public class AddCommand extends Command {
	String description;
	String taskType;

	public AddCommand(String description, String taskType) {
		this.taskType = taskType;
		this.description = description;
	}

	@Override
	public void execute(TaskList tasks, Ui ui, Storage storage) throws ParseException {
		switch (taskType) {
			case "todo":
				tasks.add(new ToDo(description));
				break;
			case "deadline":
				String[] dInfo = description.split(" /by ");
				SimpleDateFormat dFormat = new SimpleDateFormat("ddMMyyyy HHmm");
				Date by = dFormat.parse(dInfo[1]);
				tasks.add(new Deadline(dInfo[0], by));
				break;
			case "event":
				String[] eInfo = description.split(" /at ");
				SimpleDateFormat eFormat = new SimpleDateFormat("ddMMyyyy HHmm");
				Date at = eFormat.parse(eInfo[1]);
				tasks.add(new Event(eInfo[0], at));
				break;
		}
	}

	@Override
	public boolean isExit() {
		return false;
	}
}