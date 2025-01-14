package duke.logic.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import duke.exception.DukeException;
import duke.extensions.Recurrence;
import duke.logic.parser.EditCommandParser;
import duke.storage.Storage;
import duke.storage.UndoStack;
import duke.task.Task;
import duke.tasklist.TaskList;
import duke.ui.Ui;

class UndoCommandTest {
    private static final String FILE_PATH = "data/duke-test.json";

    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage(FILE_PATH);

    private static TaskList createTaskList() throws DukeException {
        TaskList list = new TaskList();

        //Recurrence parameters
        Recurrence haveRecurrence = new Recurrence(Optional.of("daily"));
        Recurrence noRecurrence = new Recurrence(Optional.empty());

        //Filter parameters
        Optional<String> haveFilter = Optional.of("filter");
        Optional<String> noFilter = Optional.empty();

        //Date parameters
        Optional<LocalDateTime> dateTime1 = Optional.of(LocalDateTime.of(2017, Month.OCTOBER,
                29, 0, 0));
        Optional<LocalDateTime> dateTime2 = Optional.of(LocalDateTime.of(2018, Month.OCTOBER,
                29, 0, 0));

        //Description parameters
        String description1 = "description1";
        String description2 = "description2";

        //Create different Tasks to use as template for testing
        list.add(new Task(haveFilter, dateTime1, haveRecurrence, description1, 1, "l")); // base
        list.add(new Task(haveFilter, dateTime1, haveRecurrence, description1, 2, "l")); // diff duration
        list.add(new Task(noFilter, dateTime1, haveRecurrence, description1, 1, "l")); // diff filter
        list.add(new Task(haveFilter, dateTime1, haveRecurrence, description2, 1, "l")); // diff description
        list.add(new Task(haveFilter, dateTime2, haveRecurrence, description1, 1, "l")); // diff datetime
        list.add(new Task(haveFilter, dateTime1, noRecurrence, description1, 1, "l")); // diff recurrence

        return list;
    }

    @Test
    public void execute_allUndoTypes_success() throws DukeException, IOException, ParseException {
        TaskList list = createTaskList();
        UndoStack undoStack = new UndoStack();
        UndoCommand undoCommand = new UndoCommand(undoStack);
        Optional<String> haveFilter = Optional.of("filter");
        Optional<String> noFilter = Optional.empty();
        Command testCommand;
        Task expectedTask;
        Task actualTask;
        Task prevTask;

        // undo AddCommand
        testCommand = new AddCommand(noFilter, Optional.empty(), Optional.empty(), "undo this",
                "task", 0, "l");
        expectedTask = list.get(noFilter, 5);
        testCommand.savePrevState(list, undoStack);
        testCommand.execute(list, ui, storage);
        prevTask = list.get(noFilter, 6);
        undoCommand.execute(list, ui, storage);
        actualTask = list.get(noFilter, 5);
        assertEquals(expectedTask, actualTask);
        assertNotEquals(prevTask, actualTask);

        // undo DeleteCommand
        testCommand = new DeleteCommand(noFilter, "4");
        expectedTask = list.get(noFilter, 4);
        testCommand.savePrevState(list, undoStack);
        testCommand.execute(list, ui, storage);
        prevTask = list.get(noFilter, 4);
        undoCommand.execute(list, ui, storage);
        actualTask = list.get(noFilter, 4);
        assertEquals(expectedTask, actualTask);
        assertNotEquals(prevTask, actualTask);


        // undo EditCommand
        testCommand = new EditCommandParser().parse(noFilter, "4 -priority m");
        expectedTask = list.get(noFilter, 4);
        testCommand.savePrevState(list, undoStack);
        testCommand.execute(list, ui, storage);
        prevTask = new Task(list.get(noFilter, 4));
        undoCommand.execute(list, ui, storage);
        actualTask = list.get(noFilter, 4);
        assertEquals(expectedTask, actualTask);
        assertNotEquals(prevTask, actualTask);

        // undo DoneCommand
        testCommand = new DoneCommand(noFilter, "4");
        expectedTask = list.get(noFilter, 4);
        testCommand.savePrevState(list, undoStack);
        testCommand.execute(list, ui, storage);
        prevTask = new Task(list.get(noFilter, 4));
        undoCommand.execute(list, ui, storage);
        actualTask = list.get(noFilter, 4);
        assertEquals(expectedTask, actualTask);
        assertNotEquals(prevTask, actualTask);

        // undo ClearCommand with filter
        testCommand = new ClearCommand(haveFilter);
        int expectedListSize = list.size();
        testCommand.savePrevState(list, undoStack);
        testCommand.execute(list, ui, storage);
        int previousListSize = list.size();
        undoCommand.execute(list, ui, storage);
        int actualListSize = list.size();
        assertEquals(expectedListSize, actualListSize);
        assertNotEquals(previousListSize, actualListSize);

        // undo ClearCommand without filter
        testCommand = new ClearCommand(noFilter);
        expectedListSize = list.size();
        testCommand.savePrevState(list, undoStack);
        testCommand.execute(list, ui, storage);
        previousListSize = list.size();
        undoCommand.execute(list, ui, storage);
        actualListSize = list.size();
        assertEquals(expectedListSize, actualListSize);
        assertNotEquals(previousListSize, actualListSize);
    }
}
