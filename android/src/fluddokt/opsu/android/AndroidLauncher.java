package fluddokt.opsu.android;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;

import fluddokt.ex.DeviceInfo;
import fluddokt.opsu.fake.File;
import fluddokt.opsu.fake.Log;
import itdelatrisu.opsu.Opsu;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.setLogger(new AndroidLogger());
        initialize(new Opsu(new String[0]), getConfig());
    }

    private static AndroidApplicationConfiguration getConfig()
    {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useWakelock = true;

        DeviceInfo.info = new DeviceInfo() {
            @Override
            public String getInfo()
            {
                return
                        "BOARD: " + Build.BOARD
                                + "\nFINGERPRINT: " + Build.FINGERPRINT
                                + "\nHOST: " + Build.HOST
                                + "\nMODEL: " + Build.MODEL
                                + "\nINCREMENTAL: " + Build.VERSION.INCREMENTAL
                                + "\nRELEASE: " + Build.VERSION.RELEASE
                                + "\n"
                        ;
            }

            @Override
            public File getDownloadDir()
            {
                return new File(new FileHandle(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
            }
        };

        return config;
    }
}
