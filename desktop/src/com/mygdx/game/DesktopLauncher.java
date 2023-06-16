package com.mygdx.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import itdelatrisu.opsu.Opsu;
import itdelatrisu.opsu.OpsuConstants;

public class DesktopLauncher {
    public static void main(String[] args)
    {
        // todo: parse options before game start
        new LwjglApplication(new Opsu(args), getConfig());
    }

    private static LwjglApplicationConfiguration getConfig()
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.addIcon("res/icon32.png", Files.FileType.Internal);
        config.addIcon("res/icon16.png", Files.FileType.Internal);
        config.vSyncEnabled = false;
        // set window size limits: minwidth = 800, minheight = 600
        config.foregroundFPS = 240;
        config.backgroundFPS = 30;
        config.title = OpsuConstants.PROJECT_NAME;
        //config.samples = 2;
        //config.audioDeviceBufferCount=240; // ???

        return config;
    }
}
