package duke.extensions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import duke.exception.DukeException;

class RecurrenceTest {
    /**
     * Test for correct boolean value returned for isTimeToReset()
     *
     * @throws DukeException
     */
    @Test
    void testIsTimeToReset_daily() throws DukeException {
        Recurrence r = new Recurrence(Optional.of("daily"));
        assertTrue(!r.isTimeToReset());
    }

    /**
     * Test for correct boolean value returned for isTimeToReset()
     *
     * @throws DukeException
     */
    @Test
    void testIsTimeToReset_weekly() throws DukeException {
        Recurrence r = new Recurrence(Optional.of("weekly"));
        assertTrue(!r.isTimeToReset());
    }

}
