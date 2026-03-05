package com.csci2020.backend;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.logging.*;

/**
 * Class to generate a logger to output debug information to a file
 */
public class Logging {
    /**
     * Create a new logger
     * @param name Name of the logger to use.
     * @param filepath Path to write to
     * @return A logger
     */
    public static Logger createLogger(String name, Path filepath){
        Logger logger = Logger.getLogger(name);
        try {
            FileHandler fh = new FileHandler(filepath.toString());
            Formatter formatter = new Formatter() {
                @Override
                public String format(LogRecord logRecord) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format("[%s] %s: %s%n", new Date(logRecord.getMillis()), logRecord.getLevel().getName(), logRecord.getMessage()));
                    Throwable error = logRecord.getThrown();
                    if(error != null){
                        sb.append('\t');
                        sb.append(error.getClass().getName());
                        sb.append(": ");
                        sb.append(error.getMessage());
                        sb.append('\n');
                        for(StackTraceElement element : error.getStackTrace()){
                            sb.repeat('\t',2);
                            sb.append(element);
                            sb.append('\n');
                        }
                    }
                    return sb.toString();
                }
            };
            fh.setFormatter(formatter);
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.FINEST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return logger;
    }
}
