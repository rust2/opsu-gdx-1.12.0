package fluddokt.opsu.android;

import android.util.Log;
import fluddokt.opsu.fake.DefaultLogSystem;
import kww.opsu.logging.ILogger;

/**
 * Default implementation of {@link ILogger} on android.
 * Uses logcat and may write to log file
 */
// todo: make some sort of caching for android because
//  if too much write operations happens,
//  it pretty much degrades smartphone's memory
public class AndroidLogger implements ILogger {
    /** Whether logs should be written to file */
    private boolean saveToFile;

    @Override
    public void setSaveToFile(boolean value) {
        saveToFile = value;
    }

    @Override
    public void trace(String tag, String message) {
        Log.v(tag, message);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
        }
    }

    @Override
    public void trace(String tag, String message, Throwable exception) {
        Log.v(tag, message, exception);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
            exception.printStackTrace(DefaultLogSystem.out);
        }
    }

    @Override
    public void debug(String tag, String message) {
        Log.d(tag, message);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
        }
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        Log.d(tag, message, exception);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
            exception.printStackTrace(DefaultLogSystem.out);
        }
    }

    @Override
    public void info(String tag, String message) {
        Log.i(tag, message);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
        }
    }

    @Override
    public void info(String tag, String message, Throwable exception) {
        Log.i(tag, message, exception);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
            exception.printStackTrace(DefaultLogSystem.out);
        }
    }

    @Override
    public void warn(String tag, String message) {
        Log.w(tag, message);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
        }
    }

    @Override
    public void warn(String tag, String message, Throwable exception) {
        Log.w(tag, message, exception);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
            exception.printStackTrace(DefaultLogSystem.out);
        }
    }

    @Override
    public void error(String tag, String message) {
        Log.e(tag, message);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
        }
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        Log.e(tag, message, exception);

        if (saveToFile && DefaultLogSystem.out != null) {
            DefaultLogSystem.out.println(tag + ": " + message);
            exception.printStackTrace(DefaultLogSystem.out);
        }
    }
}