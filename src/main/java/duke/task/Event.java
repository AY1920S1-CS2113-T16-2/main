package duke.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Subclass of duke.task.Task
 * Describes specific type of task which can be classified as an event
 */
public class Event extends Task {

    /**
     * Constructor for duke.task.Event
     * Takes in a String description like superclass duke.task.Task
     * Takes in another parameter Date to store when the duke.task.Event should be held at
     * Allows reminding of user for when the duke.task.Event is going to be
     *
     * @param description the description of the event
     * @param          the date and time at which the event will be held
     */
    public Event(Optional<String> filter, Optional<LocalDateTime> dateTime, Optional<String> recurrencePeriod, String description, int duration) {
        super(filter, dateTime, recurrencePeriod, description, duration);
        this.key = "[E]";
    }
}
