package duke.parser;

import duke.command.*;
import duke.exception.DukeException;

/**
 * duke.parser.Parser class that deals with making sense of user commands
 */
public class Parser {
    /**
     * Returns a duke.command.Command object which will be specific to the keywords given in the user command
     * Segregates the different categories of user commands
     * Allows them to be dealt with specifically like how the should be with less confusion
     *
     * @param fullCommand the entire user command
     * @return duke.command.Command the class duke.command.Command will execute the user command
     * @throws DukeException in case of user input errors which duke.parser.Parser cannot recognise, the parser will return
     *                       specific error messages depending on the reason of the error
     */
    public static Command parse(String fullCommand) throws DukeException {
        String[] fcArray = fullCommand.split(" ", 2);
        String keyword = fcArray[0];

        switch (keyword) {
            case "bye":
                return new ExitCommand();
            case "list":
                return new ListCommand();
            case "delete":
                if (fcArray.length == 1) {
                    throw new DukeException("☹ OOPS!!! The description of delete cannot be empty.");
                }
                return new DeleteCommand(fcArray[1]);
            case "find":
                if (fcArray.length == 1) {
                    throw new DukeException("☹ OOPS!!! The description of find cannot be empty.");
                }
                return new FindCommand(fcArray[1]);
            case "done":
                if (fcArray.length == 1) {
                    throw new DukeException("☹ OOPS!!! The description of done cannot be empty.");
                }
                return new DoneCommand(fcArray[1]);
            case "set":
                if (fcArray.length == 1) {
                    throw new DukeException("☹ OOPS!!! The description of set cannot be empty.");
                }
                String[] editArray = fcArray[1].split(" ", 2);
                if (editArray.length == 1) {
                    throw new DukeException("☹ OOPS!!! You are missing the description of the edit.");
                }
                String editCommand = editArray[0];
                switch (editCommand) {
                    case "priority":
                        String[] pArray = editArray[1].split(" ");
                        if (pArray.length != 2) {
                            throw new DukeException("☹ OOPS!!! You are missing either the index of the priority level");
                        }
                        return new EditCommand(editCommand, pArray[0], pArray[1]);
                    default:
                        throw new DukeException("Sorry I don't know what you want me to do :-(");
                }
            case "todo":
            case "deadline":
            case "event":
                if (fcArray.length == 1) {
                    throw new DukeException("☹ OOPS!!! The description of " + keyword + " cannot be empty.");
                }
                return new AddCommand(fcArray[1], keyword);
            default:
                throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }
}
