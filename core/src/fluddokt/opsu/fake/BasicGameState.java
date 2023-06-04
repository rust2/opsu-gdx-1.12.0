package fluddokt.opsu.fake;

import com.badlogic.gdx.Gdx;
import itdelatrisu.opsu.Opsu;

public abstract class BasicGameState extends GameState {

    //kww: pulled them from children classes
    protected GameContainer container;
    protected GameOpsu gameOpsu; // more deeper access?
    protected StateBasedGame game;
    protected Input input;
    protected final int state;

    public int getID()
    {
        return state;
    }

    boolean inited = false;

	/* kww: Every state must listen for resize event */ {
		//Listeners.registerResizable(this);
        gameOpsu = ((GameOpsu) Gdx.app.getApplicationListener());
        //game = ((GameOpsu) Gdx.app.getApplicationListener()).opsu; // todo
	}

    // Should only be used by anonymous class!
    public BasicGameState() {
        this(Opsu.STATE_NULL);
    }

    // in your "state" call super(state) to avoid any problems
    public BasicGameState(int state) {
        this.state = state;
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {}

    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {}

    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {}

    public void mousePressed(int button, int x, int y) {}

    public void mouseReleased(int button, int x, int y) {}

    public void mouseClicked(int button, int x, int y, int clickCount) {}

    public void mouseDragged(int oldx, int oldy, int newx, int newy) {}

    public void mouseWheelMoved(int newValue) {}

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {}

    public void keyPressed(int key, char c) {}

    public void keyReleased(int key, char c) {}

    public void enter(GameContainer container, StateBasedGame game) throws SlickException {}

    public void leave(GameContainer container, StateBasedGame game) throws SlickException {}
}
