package kww.opsu.logging;

import fluddokt.opsu.fake.DefaultLogSystem;

/** Default logging implementation that spits all messages to console and the output stream, if any */
public class DefaultLogger implements ILogger {
    /** Whether logs should be written to file */
    private boolean saveToFile;

    @Override
    public void setSaveToFile(boolean value) {
        saveToFile = value;
    }

    @Override
    public void trace(String tag, String message) {
        System.out.println("[" + tag + "] " + message);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
        }
    }

    @Override
    public void trace(String tag, String message, Throwable exception) {
        System.out.println("[" + tag + "] " + message);
        exception.printStackTrace();

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
            exception.printStackTrace(DefaultLogSystem.out);
        }
    }

    @Override
    public void debug(String tag, String message) {
        System.out.println("[" + tag + "] " + message);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
        }
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        System.out.println("[" + tag + "] " + message);
        exception.printStackTrace();

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
            exception.printStackTrace(DefaultLogSystem.out);
        }
    }

    @Override
    public void info(String tag, String message) {
        System.out.println("[" + tag + "] " + message);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
        }
    }

    @Override
    public void info(String tag, String message, Throwable exception) {
        System.out.println("[" + tag + "] " + message);
        exception.printStackTrace();

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
            exception.printStackTrace(DefaultLogSystem.out);
        }
    }

    @Override
    public void warn(String tag, String message) {
        System.out.println("[" + tag + "] " + message);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
        }
    }

    @Override
    public void warn(String tag, String message, Throwable exception) {
        System.out.println("[" + tag + "] " + message);
        exception.printStackTrace();

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
            exception.printStackTrace(DefaultLogSystem.out);
        }
    }

    @Override
    public void error(String tag, String message) {
        System.out.println("[" + tag + "] " + message);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
        }
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        System.out.println("[" + tag + "] " + message);
        exception.printStackTrace();

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
            exception.printStackTrace(DefaultLogSystem.out);
        }
    }
}