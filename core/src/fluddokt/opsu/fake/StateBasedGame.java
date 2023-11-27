package fluddokt.opsu.fake;

import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.TimeUtils;

import fluddokt.newdawn.slick.state.transition.Transition;
import fluddokt.opsu.fake.gui.GInputListener;
import itdelatrisu.opsu.Opsu;
import itdelatrisu.opsu.OpsuConstants;

public abstract class StateBasedGame implements InputProcessor {

    public GameContainer container;
    final static BasicGameState EMPTY_STATE = new EmptyGameState();
    BasicGameState currentState = EMPTY_STATE;
    BasicGameState nextState = null;
    BasicGameState oldState = null;
    HashMap<Integer, BasicGameState> bgs = new HashMap<>();
    LinkedList<BasicGameState> orderedbgs = new LinkedList<>();
    protected String windowTitle = OpsuConstants.PROJECT_NAME;
    LinkedList<GInputListener> inputListener = new LinkedList<>();
    boolean rightIsPressed;
    int touchX = 0;
    int touchY = 0;
    long touchTime;

    Transition enterT, leaveT;

    public StateBasedGame() {}

    public BasicGameState getState(int state)
    {
        return bgs.get(state);
    }

    public void enterState(int newState)
    {
        enterState(newState, null, null);
    }

    public void enterState(int newState, Transition leaveT, Transition enterT)
    {
        this.enterT = enterT;
        this.leaveT = leaveT;
        oldState = currentState;
        currentState = EMPTY_STATE;
        nextState = bgs.get(newState);
    }

    private boolean enterNextState() throws SlickException
    {
        if (nextState != null)
        {
            if (container == null)
            {
                throw new Error("");
            }
            oldState.leave();
            currentState = nextState;
            nextState = null;
            touchX = 0;
            touchY = 0;

            if (!currentState.inited)
            {
                currentState.init();
                currentState.inited = true;
            }
            currentState.enter();
            return true;
        }
        return false;
    }

    public int getCurrentStateID()
    {
        return currentState.getID();
    }

    public String getTitle()
    {
        return windowTitle;
    }

    // kww: Fuck you @fluddokt with your hardcoded redirection to splash state :D
    public void addState(BasicGameState state)
    {
        bgs.put(state.getID(), state);
        orderedbgs.add(state);

        if (state.getID() == Opsu.STATE_SPLASH)
            enterState(Opsu.STATE_SPLASH);
        // state.init(gc, this);
    }

    public void renderCurrentState() throws SlickException
    {
        int deltaTime = (int) (Gdx.graphics.getDeltaTime() * 1000);

        /// Update ///

        if (leaveT == null)
            enterNextState();

        if (leaveT != null)
        {
            if (leaveT.isComplete())
            {
                leaveT = null;
            }
            else
            {
                leaveT.update(container, deltaTime);
                //oldState.update(gc, this, deltaTime);
            }
        }
        else
        {
            if (enterT != null)
            {
                if (enterT.isComplete())
                {
                    enterT = null;
                }
                else
                {
                    enterT.update(container, deltaTime);
                }
            }
            if (currentState != null)
                currentState.update(deltaTime);
        }

        /// Render ///

        Graphics g = Graphics.getGraphics();

        if (leaveT != null)
        {
            leaveT.preRender(container, g);
            oldState.render(g);
            leaveT.postRender(container, g);
        }
        else if (enterT != null)
        {
            enterT.preRender(container, g);
            currentState.render(g);
            enterT.postRender(container, g);
        }
        else
        {
            currentState.render(g);
        }
    }

//	int lastEnteredState = 0;
//
//	public void renderCurrentState() throws SlickException {
//		int deltaTime = (int) (Gdx.graphics.getDeltaTime() * 1000);
//
//		if(lastEnteredState > 0){
//			if(deltaTime > 32) {
//				lastEnteredState--;
//			}
//			else
//				lastEnteredState = 0;
//		}
//
//		if (leaveT == null)
//			enterNextState();
//		{
//			if (leaveT != null) {
//				if (leaveT.isComplete()) {
//					leaveT = null;
//				} else {
//					leaveT.update(this, container, deltaTime);
//					//oldState.update(gc, this, deltaTime);
//				}
//			} else{
//				if (enterT != null) {
//					if (enterT.isComplete()) {
//						enterT = null;
//					} else {
//						enterT.update(this, container, deltaTime);
//					}
//				}
//				if (currentState != null && lastEnteredState == 0)
//					currentState.update(container, deltaTime);
//			}
//			Graphics g = Graphics.getGraphics();
//			if (leaveT != null) {
//				leaveT.preRender(this, container, g);
//				oldState.render(container, g);
//				leaveT.postRender(this, container, g);
//			} else if (enterT != null) {
//				enterT.preRender(this, container, g);
//				currentState.render(container, g);
//				enterT.postRender(this, container, g);
//			} else {
//				currentState.render(container, g);
//			}
//		}
//	}

    public void initStates() throws SlickException
    {
        initStatesList(container);
        for (BasicGameState state : orderedbgs)
        {
            if (!state.inited)
            {
                state.init();
                state.inited = true;
            }
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        // System.out.println("Key:"+keycode);
        for (GInputListener keylis : inputListener)
        {
            keylis.consumeEvent = false;
            keylis.keyPressed(keycode, (char) 0);
            if (keylis.consumeEvent)
                return true;
        }
        currentState.keyPressed(keycode, (char) 0);
        //com.badlogic.gdx.Input.Keys.toString(keycode).charAt(0));

        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        for (GInputListener keylis : inputListener)
        {
            keylis.consumeEvent = false;
            keylis.keyReleased(keycode, (char) 0);
            if (keylis.consumeEvent)
                return true;
        }
        currentState.keyReleased(keycode, (char) 0);
        //com.badlogic.gdx.Input.Keys.toString(keycode).charAt(0));

        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        for (GInputListener keylis : inputListener)
        {
            keylis.consumeEvent = false;
            keylis.keyType(character);
            if (keylis.consumeEvent)
                return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        try
        {
            if (pointer > 0)
            {
                if (rightIsPressed)
                {
                    mouseReleased(Input.MOUSE_RIGHT_BUTTON, oldx, oldy);
                }
                mousePressed(Input.MOUSE_RIGHT_BUTTON, oldx, oldy);
                container.getInput().setMouseRighButtontDown(true);
                rightIsPressed = true;
                touchX = oldx;
                touchY = oldy;
                touchTime = TimeUtils.millis();
            }
            else
            {
                mousePressed(button, screenX, screenY);
                oldx = screenX;
                oldy = screenY;
                touchX = screenX;
                touchY = screenY;
                touchTime = TimeUtils.millis();
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            Opsu.error("touchDown", e);
        }
        return false;
    }

    private void mousePressed(int button, int x, int y)
    {
        Input.x = x;
        Input.y = y;
        for (GInputListener keylis : inputListener)
        {
            keylis.consumeEvent = false;
            keylis.mousePressed(button, x, y);
            if (keylis.consumeEvent)
                return;
        }
        currentState.mousePressed(button, x, y);
    }

    private void mouseReleased(int button, int x, int y)
    {
        for (GInputListener keylis : inputListener)
        {
            keylis.consumeEvent = false;
            keylis.mouseReleased(button, x, y);
            if (keylis.consumeEvent)
                return;
        }
        currentState.mouseReleased(button, x, y);
    }

    private void mouseClicked(int button, int x, int y, int clickCount)
    {
        for (GInputListener keylis : inputListener)
        {
            keylis.consumeEvent = false;
            keylis.mouseClicked(button, x, y, clickCount);
            if (keylis.consumeEvent)
                return;
        }
        currentState.mouseClicked(button, x, y, clickCount);
    }

    private void mouseDragged(int oldx, int oldy, int newx, int newy)
    {
        for (GInputListener keylis : inputListener)
        {
            keylis.consumeEvent = false;
            keylis.mouseDragged(oldx, oldy, newx, newy);
            if (keylis.consumeEvent)
                return;
        }
        currentState.mouseDragged(oldx, oldy, newx, newy);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if (pointer > 0)
        {
            int dx = oldx - touchX;
            int dy = oldy - touchY;
            if (TimeUtils.timeSinceMillis(touchTime) < 500 && dx * dx + dy * dy < 10 * 10)
            {
                mouseClicked(Input.MOUSE_RIGHT_BUTTON, oldx, oldy, 1);
            }
            mouseReleased(Input.MOUSE_RIGHT_BUTTON, oldx, oldy);

            container.getInput().setMouseRighButtontDown(false);
            rightIsPressed = false;
        }
        else
        {
            int dx = screenX - touchX;
            int dy = screenY - touchY;
            if (TimeUtils.timeSinceMillis(touchTime) < 500 && dx * dx + dy * dy < 10 * 10)
            {
                mouseClicked(button, screenX, screenY, 1);
            }
            mouseReleased(button, screenX, screenY);

            oldx = screenX;
            oldy = screenY;
        }

        return false;
    }

    /**
     * @apiNote Handle touch cancel event.<br>To keep the old behaviour, we should call {@link com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)}
     * @since libGDX 1.12.0
     */
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button)
    {
        return touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        if (pointer == 0)
        {
            Input.x = screenX;
            Input.y = screenY;
            mouseDragged(oldx, oldy, screenX, screenY);
            oldx = screenX;
            oldy = screenY;
        }

        return false;
    }

    int oldx, oldy;

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        Input.x = screenX;
        Input.y = screenY;
        currentState.mouseMoved(oldx, oldy, screenX, screenX);
        oldx = screenX;
        oldy = screenY;

        return false;
    }

    /** @implNote only take vertical scrolling in account */
    @Override
    public boolean scrolled(float amountX, float amountY)
    {
        for (GInputListener listener : inputListener)
        {
            listener.consumeEvent = false;
            listener.mouseWheelMoved((int) -amountY * 120);
            if (listener.consumeEvent)
                return true;
        }
        currentState.mouseWheelMoved((int) -amountY);

        return false;
    }

    public boolean closeRequested()
    {
        return false;
    }

    public abstract void initStatesList(GameContainer container) /*throws SlickException*/;

    public GameContainer getContainer()
    {
        return container;
    }

    public void setContainer(GameContainer gameContainer)
    {
        container = gameContainer;
    }

    public void addInputListener(GInputListener listener)
    {
        if (!inputListener.contains(listener))
            inputListener.addFirst(listener);
    }

    public void removeInputListener(GInputListener listener)
    {
        inputListener.remove(listener);
    }
}
