package logging;

import java.util.logging.LogRecord;

/**
 * Created by Baljit Singh Sarai on 01.03.16.
 * @author Baljit Singh Sarai
 */
public class ConsoleFormatter extends java.util.logging.Formatter {
    public ConsoleFormatter() { super(); }
    private static final String lineSep = System.getProperty("line.separator");

    @Override
    public String format(final LogRecord record) {
        String loggerName = record.getLoggerName();
        if(loggerName == null) {
            loggerName = "root";
        }
        StringBuilder output = new StringBuilder()
                .append(record.getMessage())
                .append(lineSep);
        return output.toString();
    }


}
