import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLogger {

    private static boolean logToFile;
    private static boolean logToConsole;
    private static boolean logMessage;
    private static boolean logWarning;
    private static boolean logError;
    private static boolean logToDatabase;
    private static Map dbParams;
    private static Logger logger = Logger.getLogger("Mylog");

    public JobLogger(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam, boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap)
    {
        logError        = logErrorParam;
        logMessage      = logMessageParam;
        logWarning      = logWarningParam;
        logToDatabase   = logToDatabaseParam;
        logToFile       = logToFileParam;
        logToConsole    = logToConsoleParam;
        dbParams        = dbParamsMap;
        if(logToFile){
            File logFile = new File(dbParams.get("logFileFolder") + "/logFile.txt");
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void LogMessage(String messageText, boolean message, boolean warning, boolean error) throws Exception {

        // TODO: Create level handler

        messageText.trim();

        if (messageText == null || messageText.length() == 0) {
            return;
        }

        if (!logToConsole && !logToFile && !logToDatabase) {
            throw new Exception("Invalid configuration");
        }

        if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
            throw new Exception("Error or Warning or Message must be specified");
        }

        if(logToFile) {
            FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");
            fh.setFormatter(new LogFormatter());
            logger.addHandler(fh);
            logger.log(Level.INFO, messageText);
        }

        if(logToConsole) {
            ConsoleHandler ch = new ConsoleHandler();
            ch.setFormatter(new LogFormatter());
            logger.addHandler(ch);
            logger.log(Level.INFO, messageText);
        }

        if(logToDatabase) {
            SqlHandler sqlHandler = new SqlHandler(dbParams, t);
            sqlHandler.setFormatter(new LogFormatter());
            logger.addHandler(sqlHandler);
            logger.log(Level.INFO, messageText);
        }

    }

}