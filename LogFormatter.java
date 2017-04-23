import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public final class LogFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        return record.getLevel()+"::"+new Date(record.getMillis())+"::"+record.getMessage()+"\n";
    }
}