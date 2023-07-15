package fluddokt.opsu.fake;

import itdelatrisu.opsu.Opsu;

public abstract class BasicGameState extends GameState {

    //kww: pulled them from children classes
    protected GameContainer container;
    protected Opsu game;
    protected Input input;
    protected final int state;

    public int getID()
    {
        return state;
    }

    public boolean inited = false;

    /* kww: Every state must listen for resize event */
    {
        //Listeners.registerResizable(this); // todo
    }

    // Should only be used by anonymous class!
    public BasicGameState()
    {
        // No need to use any functionality from anonymous class.
        // No NullPointerException here.
        this(Opsu.STATE_NULL, null);
    }

    // in your "state" call super(state, game) to avoid any problems
    public BasicGameState(int state, Opsu game)
    {
        this.state = state;
        this.game = game;

        try {
            // Instance getter is not available during its construction
            this.container = Opsu.getOpsu().getContainer();
            System.out.println("Set container for " + this.getClass());
        }
        catch (NullPointerException e)
        {
            System.out.println("Ignored setting container for " + this.getClass());
        }
    }

    public void init() throws SlickException {}

    // Anyway this Graphics is static and does not need to be passed every frame...
    public void render(Graphics g) throws SlickException {}

    public void update(int delta) throws SlickException {}

    public void mousePressed(int button, int x, int y) {}

    public void mouseReleased(int button, int x, int y) {}

    public void mouseClicked(int button, int x, int y, int clickCount) {}

    public void mouseDragged(int oldx, int oldy, int newx, int newy) {}

    public void mouseWheelMoved(int newValue) {}

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {}

    public void keyPressed(int key, char c) {}

    public void keyReleased(int key, char c) {}

    public void enter() throws SlickException {}

    public void leave() throws SlickException {}
}
