package fluddokt.opsu.fake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

public class AppGameContainer extends GameContainer {

	public static int containerWidth, containerHeight;
	
	public AppGameContainer(StateBasedGame game) {
		super(game);
	}

	public AppGameContainer(StateBasedGame game, int width, int height, boolean fullscreen) {
		super(game);
	}

	public void setDisplayMode(int containerWidth, int containerHeight, boolean isFullscreen) throws SlickException {
		System.out.println("setDisplayMode :" + containerWidth + " " + containerHeight);
		AppGameContainer.containerWidth = containerWidth;
		AppGameContainer.containerHeight = containerHeight;

		//region oldcode
		/*
		Gdx.graphics.setDisplayMode(containerWidth, containerHeight, isFullscreen);
		*/
		//endregion
		// kww: FIXME: I don't know what to put here to make it work, hehe
		if (isFullscreen)
		{
			com.badlogic.gdx.Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
			Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
			if (!Gdx.graphics.setFullscreenMode(displayMode))
			{
				// switching to full-screen mode failed
				System.err.println("setDisplayMode: FAILED");
			}
		}
		else
		{
			Gdx.graphics.setWindowedMode(containerWidth, containerHeight);
		}

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

}
