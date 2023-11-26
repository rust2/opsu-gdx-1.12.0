package kww.opsu.logging;

/** Logger interface. Has 5 levels of logging */
public interface ILogger {
    /** Sets whether logs should be written to file */
    void setSaveToFile(boolean value);

    void trace(String tag, String message);
    void trace(String tag, String message, Throwable exception);

    void debug(String tag, String message);
    void debug(String tag, String message, Throwable exception);

    void info(String tag, String message);
    void info(String tag, String message, Throwable exception);

    void warn(String tag, String message);
    void warn(String tag, String message, Throwable exception);

    void error(String tag, String message);
    void error(String tag, String message, Throwable exception);
}