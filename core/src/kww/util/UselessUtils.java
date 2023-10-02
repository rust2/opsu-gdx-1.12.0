package kww.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import itdelatrisu.opsu.ErrorHandler;
import itdelatrisu.opsu.audio.SoundController;
import itdelatrisu.opsu.audio.SoundEffect;
import itdelatrisu.opsu.ui.UI;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Deflater;

public class UselessUtils {
    // todo: tell me anyone if I made this worng way
    public static void takeScreenshot()
    {
        // create screen shot
        final Pixmap source = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // complete other stuff in separate thread, there's no hurry
        new Thread(() -> {
            ByteBuffer pixels = source.getPixels();

            // This loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
            int size = Gdx.graphics.getBackBufferWidth() * Gdx.graphics.getBackBufferHeight() * 4;
            for (int i = 3; i < size; i += 4)
            {
                pixels.put(i, (byte) 255);
            }

            // check for directory
            FileHandle screenshotDir = itdelatrisu.opsu.options.Options.getScreenshotDir().getFileHandle();
            if (!screenshotDir.isDirectory())
            {
                ErrorHandler.error("Failed to create screenshot at '" + screenshotDir.name() + "'.", null, false);
                return;
            }

            // create file name
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String name = "screenshot_" + date.format(new Date()) + ".png";

            // play screamer sound
            SoundController.playSound(SoundEffect.SHUTTER);
            UI.getNotificationManager().sendNotification("Screenshot saved");

            // save a picture
            savePixmapTo(screenshotDir.child(name).path(), source, true);

            // dispose disposable
            source.dispose();
        }).start();
    }

    public static void savePixmapTo(String absolutePath, Pixmap pixmapToSave, boolean flip)
    {
        System.out.println("Trying to dump Pixmap to '" + absolutePath + "'...");
        if (pixmapToSave == null)
        {
            System.err.println("\tFailed to proceed: NullPoinerException");
            return;
        }

        // complete other stuff in separate thread, there's no hurry
        new Thread(() -> PixmapIO.writePNG(new FileHandle(absolutePath), pixmapToSave, Deflater.DEFAULT_COMPRESSION, flip)).start();
    }
}
