package kww.opsu.logging;

/**
 * Logger that just prints messages to sysout. <br>
 * Full copy of com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationLogger
 */
public final class StubLogger implements ILogger {
    private static final StubLogger instance = new StubLogger();

    /** No need to make new instance of this class. Use getter */
    private StubLogger() {}

    public static StubLogger get() {
        return instance;
    }

    @Override
    public void setSaveToFile(boolean value) { /* not intended to be implemented */ }

    @Override
    public void trace(String tag, String message) {
        System.out.println("[" + tag + "] " + message);
    }

    @Override
    public void trace(String tag, String message, Throwable exception) {
        System.out.println("[" + tag + "] " + message);
        exception.printStackTrace();
    }

    @Override
    public void debug(String tag, String message) {
        System.out.println("[" + tag + "] " + message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        System.out.println("[" + tag + "] " + message);
        exception.printStackTrace();
    }

    @Override
    public void info(String tag, String message) {
        System.out.println("[" + tag + "] " + message);
    }

    @Override
    public void info(String tag, String message, Throwable exception) {
        System.out.println("[" + tag + "] " + message);
        exception.printStackTrace();
    }

    @Override
    public void warn(String tag, String message) {
        System.out.println("[" + tag + "] " + message);
    }

    @Override
    public void warn(String tag, String message, Throwable exception) {
        System.out.println("[" + tag + "] " + message);
        exception.printStackTrace();
    }

    @Override
    public void error(String tag, String message) {
        System.out.println("[" + tag + "] " + message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        System.out.println("[" + tag + "] " + message);
        exception.printStackTrace();
    }
}
