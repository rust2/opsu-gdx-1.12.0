/*
 * opsu! - an open-source osu! client
 * Copyright (C) 2014-2017 Jeffrey Han
 *
 * opsu! is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * opsu! is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with opsu!.  If not, see <http://www.gnu.org/licenses/>.
 */

package itdelatrisu.opsu;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import fluddokt.newdawn.slick.state.transition.EasedFadeOutTransition;
import fluddokt.newdawn.slick.state.transition.FadeInTransition;
import fluddokt.opsu.fake.DefaultLogSystem;
import fluddokt.opsu.fake.Desktop;
import fluddokt.opsu.fake.File;
import fluddokt.opsu.fake.FileOutputStream;
import fluddokt.opsu.fake.FileSystemLocation;
import fluddokt.opsu.fake.GameContainer;
import fluddokt.opsu.fake.Graphics;
import fluddokt.opsu.fake.Log;
import fluddokt.opsu.fake.ResourceLoader;
import fluddokt.opsu.fake.SlickException;
import fluddokt.opsu.fake.StateBasedGame;
import itdelatrisu.opsu.audio.MusicController;
import itdelatrisu.opsu.db.DBController;
import itdelatrisu.opsu.downloads.DownloadList;
import itdelatrisu.opsu.downloads.Updater;
import itdelatrisu.opsu.options.Options;
import itdelatrisu.opsu.states.ButtonMenu;
import itdelatrisu.opsu.states.CalibrateOffsetMenu;
import itdelatrisu.opsu.states.DownloadsMenu;
import itdelatrisu.opsu.states.Game;
import itdelatrisu.opsu.states.GamePauseMenu;
import itdelatrisu.opsu.states.GameRanking;
import itdelatrisu.opsu.states.MainMenu;
import itdelatrisu.opsu.states.SongMenu;
import itdelatrisu.opsu.states.Splash;
import itdelatrisu.opsu.ui.UI;

//import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/*
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EasedFadeOutTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.DefaultLogSystem;
import org.newdawn.slick.util.FileSystemLocation;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;
*/
/**
 * Main class.
 * <p>
 * Creates game container, adds all other states, and initializes song data.
 */
public class Opsu extends StateBasedGame implements ApplicationListener {
    /** Non-static game instance. */
    public static Opsu getOpsu()
    {
        return (Opsu) Gdx.app.getApplicationListener();
    }

    /** Hardcoded game version... */
    public final static String VERSION = "0.16.1a";

    /** Game states. */
    public static final int
            STATE_NULL              = 0,
            STATE_SPLASH            = 1,
            STATE_MAINMENU          = 2,
            STATE_BUTTONMENU        = 3,
            STATE_SONGMENU          = 4,
            STATE_GAME              = 5,
            STATE_GAMEPAUSEMENU     = 6,
            STATE_GAMERANKING       = 7,
            STATE_DOWNLOADSMENU     = 8,
            STATE_CALIBRATEOFFSET   = 9;

    private final String[] cmdLine;

    Stage stage;
    Table table;
    Skin skin;
    Label loadingLabel;

    private boolean inited = false;

    /**
     * Constructor.
     * @param args the program arguments, if any
     */
    public Opsu(String[] args) {
        //windowTitle = OpsuConstants.PROJECT_NAME;
        cmdLine = args;
    }

    @Override
    public void initStatesList(GameContainer container) {
        addState(new Splash(STATE_SPLASH, this));
        addState(new MainMenu(STATE_MAINMENU, this));
        addState(new ButtonMenu(STATE_BUTTONMENU, this));
        addState(new SongMenu(STATE_SONGMENU, this));
        addState(new Game(STATE_GAME, this));
        addState(new GamePauseMenu(STATE_GAMEPAUSEMENU, this));
        addState(new GameRanking(STATE_GAMERANKING, this));
        addState(new DownloadsMenu(STATE_DOWNLOADSMENU, this));
        addState(new CalibrateOffsetMenu(STATE_CALIBRATEOFFSET, this));
    }

    /**
     * Launches opsu!.
     */
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                ErrorHandler.error("** Uncaught Exception! **", e, true);
            }
        });

        // log all errors to a file
		Log.setVerbose(false);
		try
		{
			System.out.println("LOG FILE: " + Options.LOG_FILE);
			DefaultLogSystem.out = new PrintStream(new FileOutputStream(Options.LOG_FILE, false), true);
			DefaultLogSystem.out.println("Run date: " + new java.util.Date());
		}
		catch (FileNotFoundException e)
		{
			Log.error(e);
		}

        // parse configuration file
        Options.parseOptions();

        // set the resource paths
        ResourceLoader.addResourceLocation(new FileSystemLocation(new File("res/"), true));

		// initialize databases
        try {
            DBController.init();
        } catch (Exception e) {
            errorAndExit(e, "The databases could not be initialized.", true);
        }

		/*
		// check if just updated
		if (args.length >= 2)
			Updater.get().setUpdateInfo(args[0], args[1]);

		// check for updates
		Updater.get().getCurrentVersion();  // load this for the main menu
		if (!Options.isUpdaterDisabled()) {
			new Thread() {
				@Override
				public void run() {
					try {
						Updater.get().checkForUpdates();
					} catch (Exception e) {
						Log.warn("Check for updates failed.", e);
					}
				}
			}.start();
		}
		*/
    }

    @Override
    public void create()
    {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                ErrorHandler.error("** Uncaught Exception! A **", e, true);
            }
        });

        if (!Gdx.files.isExternalStorageAvailable())
        {
            if (Gdx.files.isLocalStorageAvailable())
            {
                error("External Storage is not available. \n"
                              + "Using Local Storage instead: " + Gdx.files.getLocalStoragePath(), null);
            }
            else
            {
                error("No storage is available ... ????", null);
            }
        }

        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Graphics.init();

        loadingLabel = new Label("Loading...", skin);
        table.addActor(loadingLabel);

        // Continue loading
        main(cmdLine);
    }

    @Override
    public void resize(int width, int height)
    {
        System.out.println("Game resize" + width + " " + height);

        //super.resize(width, height);
        Graphics.resize(width, height);
        //stage.getViewport().setCamera(Graphics.camera);
        stage.getViewport().update(width, height, true);
        table.invalidate();
        if(!inited)
            return;
        container.width = width;
        container.height = height;
    }

    boolean needInitilization = true;

    int delayLoad = 0; // ?
    int dialogCnt = 0; // ?

    @Override
    public void render()
    {
        //super.render();

        if (delayLoad > 2 && dialogCnt == 0) {
            try {
                if (needInitilization) {
                    initialize();
                } else {
                    ScreenUtils.clear(0, 0, 0, 1);
                    try {
                        if (container.exited) {
                            needInitilization = true;
                            delayLoad = 0;
                            table.addActor(loadingLabel);
                        }
                        else
                            renderCurrentState();
                    } catch (SlickException e) {
                        e.printStackTrace();
                        error("SlickErrorRender", e);
                    }
                    Graphics.checkMode(0);
                }
            } catch (Throwable e){
                e.printStackTrace();
                error("RenderError", e);
            }
        } else {
            if (delayLoad<=2)
                delayLoad++;
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        //table.debugAll();
    }

    @Override
    public void pause()
    {
        System.out.println("Game pause");
        if(!inited)
            return;
        //super.pause();
        container.loseFocus();
        try {
            renderCurrentState(); // is really needed?
        } catch (SlickException e) {
            e.printStackTrace();
        }
        container.lostFocus();
    }

    @Override
    public void resume()
    {
        System.out.println("Game resume");
        if(!inited)
            return;
        //super.resume();
        container.focus();
    }

    @Override
    public void dispose()
    {
        System.out.println("Game Dispose");
        if(!inited)
            return;

        for (GameImage img : GameImage.values()) {
            try {
                img.dispose();
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }

        container.closing();
        //super.dispose();
    }

    private void initialize()
    {
        if (Gdx.graphics.getWidth() > Gdx.graphics.getHeight()){
            initContainer();
            container.width = Gdx.graphics.getWidth();
            container.height = Gdx.graphics.getHeight();

            try
            {
                initStates();
            }
            catch (SlickException e)
            {
                e.printStackTrace();
                error("SlickErrorInit", e);
            }

            try
            {
                File dataDir = Options.DATA_DIR;
//                System.out.println("Data directory: " + dataDir + " " + dataDir.isExternal() + " " + dataDir.exists());
                System.out.println("Data directory: " + dataDir + "\t|\texternal:" + dataDir.isExternal() + "\t|\texist:" + dataDir.exists());
                if (dataDir.isExternal())
                {
                    File nomediafile = new File(dataDir, ".nomedia");
                    if (!nomediafile.exists())
                        new java.io.FileOutputStream(nomediafile.getIOFile()).close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            System.out.println("Local Dir:" + Gdx.files.getLocalStoragePath());
            Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
            inited = true;
            table.removeActor(loadingLabel);

            needInitilization = false;
        }
    }

    public static void error(String string, Throwable e) {
        Opsu.getOpsu().errorDialog(string, e);
    }

    private void errorDialog(final String string, final Throwable e) {
        dialogCnt++;
        String tbodyString = "X";
        if(e != null){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            tbodyString = sw.toString();
        }
        final String bodyString = tbodyString;
        Dialog dialog = new Dialog("ERROR", skin){
            final String title = string;
            final String body = bodyString;
            @Override
            protected void result(Object object) {
                System.out.println(object);

                if("CloseOpsu".equals(object)){
                    System.exit(0);
                }

                if("R".equals(object)){
                    try {
                        System.out.println("Reporting");
                        Desktop.getDesktop().browse(
                                ErrorHandler.getIssueURI(title, e, body)
                        );
                    }  catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if("S".equals(object)){

                }

                dialogCnt--;
                System.out.println("Dialog count:"+dialogCnt);
            }

        }.button("Ignore and Continue","S").button("Report on github","R").button("Close Opsu", "CloseOpsu");
        dialog.getContentTable().row();
        Label tex =new Label(string+"\n"+bodyString, skin);

        dialog.getContentTable().add(new ScrollPane(tex))
                .width(Gdx.graphics.getWidth())
                .height(Gdx.graphics.getHeight()-60);
        dialog.pack();
        table.addActor(dialog);

        table.validate();
    }

    public void initContainer() {
        // start the game
        try {
            Container app = new Container(Opsu.getOpsu()); // getter: Opsu.container

            // basic game settings
            Options.setDisplayMode(app);
            app.setForceExit(true);

			/*
			// run update if available
			if (Updater.get().getStatus() == Updater.Status.UPDATE_FINAL) {
				close();
				Updater.get().runUpdate();
				break;
			}
			*/
        } catch (Exception e) {
            errorAndExit(e, "An error occurred while creating the game container.", true);
        }
    }

    @Override
    public boolean closeRequested() {
        int id = this.getCurrentStateID();

        // intercept close requests in game-related states and return to song menu
        if (id == STATE_GAME || id == STATE_GAMEPAUSEMENU || id == STATE_GAMERANKING) {
            // start playing track at preview position
            SongMenu songMenu = (SongMenu) this.getState(Opsu.STATE_SONGMENU);
            if (id == STATE_GAMERANKING) {
                GameData data = ((GameRanking) this.getState(Opsu.STATE_GAMERANKING)).getGameData();
                if (data != null && data.isGameplay())
                    songMenu.resetTrackOnLoad();
            } else {
                if (id == STATE_GAME) {
                    MusicController.pause();
                    MusicController.setPitch(1.0f);
                    MusicController.resume();
                } else
                    songMenu.resetTrackOnLoad();
            }

            // reset game data
            if (UI.getCursor().isBeatmapSkinned())
                UI.getCursor().reset();
            songMenu.resetGameDataOnLoad();

            this.enterState(Opsu.STATE_SONGMENU, new EasedFadeOutTransition(), new FadeInTransition());
            return false;
        }

        // show confirmation dialog if any downloads are active
        if (DownloadList.get().hasActiveDownloads() &&
                UI.showExitConfirmation(DownloadList.EXIT_CONFIRMATION))
            return false;
        if (Updater.get().getStatus() == Updater.Status.UPDATE_DOWNLOADING &&
                UI.showExitConfirmation(Updater.EXIT_CONFIRMATION))
            return false;

        return true;
    }

    /**
     * Closes all resources.
     */
    public static void close() {
        // close databases
        DBController.closeConnections();

        // cancel all downloads
        DownloadList.get().cancelAllDownloads();
    }

    /**
     * Throws an error and exits the application with the given message.
     * @param e the exception that caused the crash
     * @param message the message to display
     * @param report whether to ask to report the error
     */
    private static void errorAndExit(Throwable e, String message, boolean report) {
        // JARs will not run properly inside directories containing '!'
        // http://bugs.java.com/view_bug.do?bug_id=4523159
        if (Utils.isJarRunning() && Utils.getRunningDirectory() != null &&
                Utils.getRunningDirectory().getAbsolutePath().indexOf('!') != -1)
            ErrorHandler.error(
                    "JARs cannot be run from some paths containing the '!' character. " +
                            "Please rename the file/directories and try again.\n\n" +
                            "Path: " + Utils.getRunningDirectory().getAbsolutePath(),
                    null,
                    false
            );
        else
            ErrorHandler.error(message, e, report);
        System.exit(1);
    }
}
