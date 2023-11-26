package fluddokt.opsu.fake;

import kww.opsu.logging.DefaultLogger;
import kww.opsu.logging.ILogger;

/** @see <a href="https://www.section.io/engineering-education/how-to-choose-levels-of-logging/">How to choose levels of logging</a> */
public class Log {
    /** Logger instance intended for usage inside of this class. Set by {@link Log#setLogger(ILogger)} */
    private static ILogger logger = new DefaultLogger();

    /** Logging levels. Falling back to android logging levels */
    public static final int VERBOSE = 2,
                            DEBUG = 3,
                            INFO = 4,
                            WARN = 5,
                            ERROR = 6,
                            ASSERT = 7;

    /** Current logging level */
    private static int loggingLevel = VERBOSE;

    /** Sets logger */
    public static void setLogger(ILogger in) {
        logger = in;
    }

    /** Sets logging level */
    //todo: allow changing logging level from settings
    public static void setLoggingLevel(int levelIn) {
        loggingLevel = levelIn;
    }

    public static boolean isLoggingAvailable(int level) {
        return !(level < loggingLevel || loggingLevel >= ASSERT);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Trace: intended to use instead of System.out.println()
    ///////////////////////////////////////////////////////////////////////////

    //alias for trace
    public static void log(String message) {
        if (isLoggingAvailable(VERBOSE))
            logger.trace("Trace", message);
    }

    //alias for trace
    public static void log(String prefix, String message) {
        if (isLoggingAvailable(VERBOSE))
            logger.trace("Trace", "[" + prefix + "] " + message);
    }

    public static void trace(String message) {
        if (isLoggingAvailable(VERBOSE))
            logger.trace("Trace", message);
    }

    public static void trace(String prefix, String message) {
        if (isLoggingAvailable(VERBOSE))
            logger.trace("Trace", "[" + prefix + "] " + message);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Debug
    ///////////////////////////////////////////////////////////////////////////

    public static void debug(String message) {
        if (isLoggingAvailable(DEBUG))
            logger.debug("Debug", message);
    }

    public static void debug(String prefix, String message) {
        if (isLoggingAvailable(DEBUG))
            logger.debug("Debug", "[" + prefix + "] " + message);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Info
    ///////////////////////////////////////////////////////////////////////////

    public static void info(String message) {
        if (isLoggingAvailable(INFO))
            logger.info("Info", message);
    }

    public static void info(String prefix, String message) {
        if (isLoggingAvailable(INFO))
            logger.info("Info", "[" + prefix + "] " + message);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Warn
    ///////////////////////////////////////////////////////////////////////////

    public static void warn(String message) {
        if (isLoggingAvailable(WARN))
            logger.warn("Warn", message);
    }

    public static void warn(String prefix, String message) {
        if (isLoggingAvailable(WARN))
            logger.warn("Warn", "[" + prefix + "] " + message);
    }

    public static void warn(String message, Throwable e) {
        if (isLoggingAvailable(WARN)) {
            if (e != null)
                logger.warn("Warn", message, e);
            else
                logger.warn("Warn", message);
        }
    }

    public static void warn(String prefix, String message, Throwable e) {
        if (isLoggingAvailable(WARN)) {
            if (e != null)
                logger.warn("Warn", "[" + prefix + "] " + message, e);
            else
                logger.warn("Warn", "[" + prefix + "] " + message);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Error
    ///////////////////////////////////////////////////////////////////////////

    public static void error(String message) {
        if (isLoggingAvailable(ERROR))
            logger.error("Error", message);
    }

    public static void error(String prefix, String message) {
        if (isLoggingAvailable(ERROR))
            logger.error("Error", "[" + prefix + "] " + message);
    }

    public static void error(Throwable e) {
        if (isLoggingAvailable(ERROR)) {
            error("", e);
        }
    }

    public static void error(String message, Throwable e) {
        if (isLoggingAvailable(ERROR)) {
            if (e != null)
                logger.error("Error", message, e);
            else
                logger.error("Error", message);
        }
    }

    public static void error(String prefix, String message, Throwable e) {
        if (isLoggingAvailable(ERROR)) {
            if (e != null)
                logger.error("Error", "[" + prefix + "] " + message, e);
            else
                logger.error("Error", "[" + prefix + "] " + message);
        }
    }

    public static void setVerbose(boolean b) {
        // TODO Auto-generated method stub
    }
}
