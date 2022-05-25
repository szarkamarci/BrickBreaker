package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

public class JsonAppender {
    final static private Logger logger = LogManager.getLogger(JsonAppender.class.getName());

    /**
     *Method that appends scores in json file.
     * @param score current game score
     */
    public static void append(int score)
    {
        Statistics stats = new Statistics();
        StatisticsStruct newEntry = new StatisticsStruct();
        newEntry.setScore(score);
        Instant instant = Instant.now();
        newEntry.setTimeStarted(Date.from(instant));
        ObjectMapper mapper = new ObjectMapper();
        List<StatisticsStruct> history = stats.getAll();
        history.add(newEntry);
        logger.debug("entry appended {} {}", score, instant);
        try {
            mapper.writeValue(stats.getPath(), history);
        } catch (IOException ioe) {
            logger.error("json not found", ioe);
        }
    }
}
