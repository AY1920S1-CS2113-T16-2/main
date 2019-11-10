package duke.logic.parser;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import duke.exception.DukeException;
import duke.logic.command.AddCommand;
import duke.storage.Storage;
import duke.storage.UndoStack;
import duke.task.Task;
import duke.tasklist.TaskList;
import duke.ui.Ui;
import org.junit.jupiter.api.Test;

/**
 * Class to test for correct parameters parsed in to add command
 */
class AddCommandParserTest {
    private static final String FILE_PATH = "data/editCommandTest.json";

    private static final Ui ui = new Ui();
    private static final UndoStack undoStack = new UndoStack();
    private static final Storage storage = new Storage(FILE_PATH);

    @Test
    public void parse_testRecurrenceParsed_success() throws DukeException, IOException {
        TaskList l = new TaskList();
        AddCommandParser a = new AddCommandParser();
        Optional<String> noFilter = Optional.empty();
        String command = "task why am i doing this??? -r weekly";
        AddCommand aCom = a.parse(noFilter, command);
        aCom.execute(l, ui, storage);
        String expectedRecurrenceCode = "Weekly";
        Task actualTask = l.get(0);
        assertEquals(actualTask.getRecurrenceCode(), expectedRecurrenceCode);
    }

    @Test
    public void parse_testDurationParsed_success() throws DukeException, IOException {
        TaskList l = new TaskList();
        AddCommandParser a = new AddCommandParser();
        Optional<String> noFilter = Optional.empty();
        String command = "task why am i doing this??? -d 4";
        AddCommand aCom = a.parse(noFilter, command);
        aCom.execute(l, ui, storage);
        Task actualTask = l.get(0);
        assertTrue(actualTask.getDuration().equals("4"));
    }

    @Test
    public void parse_testDurationPriority_success() throws DukeException, IOException {
        TaskList l = new TaskList();
        AddCommandParser a = new AddCommandParser();
        Optional<String> noFilter = Optional.empty();
        String command = "task why am i doing this??? -p h";
        AddCommand aCom = a.parse(noFilter, command);
        aCom.execute(l, ui, storage);
        Task actualTask = l.get(0);
        assertTrue(actualTask.getPriorityLevel() == 2);
    }

    @Test
    public void parse_testDateTimeParsed_success() throws DukeException, IOException {
        TaskList l = new TaskList();
        AddCommandParser a = new AddCommandParser();
        Optional<String> noFilter = Optional.empty();
        String command = "task why am i doing this??? -t today 1000";
        AddCommand aCom = a.parse(noFilter, command);
        aCom.execute(l, ui, storage);
        LocalDateTime expectedDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.parse("1000", DateTimeFormatter.ofPattern("HHmm")));
        Task actualTask = l.get(0);
        assertEquals(actualTask.getDateTime(), expectedDateTime);
    }

    @Test
    public void parse_testWrongKeyword_failure() throws DukeException, IOException {
        TaskList l = new TaskList();
        AddCommandParser a = new AddCommandParser();
        Optional<String> noFilter = Optional.empty();
        String command = "task why am i doing this??? -goodriddance today 1000";
        Exception exception = assertThrows(DukeException.class, () ->
                a.parse(noFilter, command));
        assertEquals("I don't know which field you are trying to add!",
                exception.getMessage());
    }

    @Test
    public void parse_testWrongDurationNonNumerical_failure() throws DukeException, IOException {
        TaskList l = new TaskList();
        AddCommandParser a = new AddCommandParser();
        Optional<String> noFilter = Optional.empty();
        String command = "task why am i doing this??? -d s";
        Exception exception = assertThrows(DukeException.class, () ->
                a.parse(noFilter, command));
        assertEquals("Please enter a numerical field for the duration!",
                exception.getMessage());
    }

    @Test
    public void parse_testWrongPriorityNonNumerical_failure() throws DukeException, IOException {
        TaskList l = new TaskList();
        AddCommandParser a = new AddCommandParser();
        Optional<String> noFilter = Optional.empty();
        String command = "task why am i doing this??? -p s";
        Exception exception = assertThrows(DukeException.class, () ->
                a.parse(noFilter, command));
        assertEquals("Please enter a valid priority description!",
                exception.getMessage());
    }
}
