package logging;

import java.util.Date;
import java.util.logging.LogRecord;

/**
 * A formmater wrapper.
 * Created by Baljit Singh Sarai on 03.03.16.
 * @author Baljit Singh Sarai.
 */
public class GuiConsoleFormatter extends java.util.logging.Formatter {

    /**
     * Creates a GuiConsoleFormatter
     */
    public GuiConsoleFormatter() { super(); }

    private static final String lineSep = System.getProperty("line.separator");

    @Override
    public String format(final LogRecord record) {
        String loggerName = record.getLoggerName();
        if(loggerName == null) {
        loggerName = "root";
        }
        StringBuilder output = new StringBuilder()
        .append("[")
        .append(record.getLevel()).append('|')
        .append(new Date(record.getMillis()))
        .append("]: ")
        .append(" | ")
        .append(": ")
        .append(record.getMessage())
        .append(lineSep);
        return output.toString();
        }
}
