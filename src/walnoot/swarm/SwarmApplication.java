package walnoot.swarm;

import walnoot.swarm.screens.LoadingScreen;
import walnoot.swarm.screens.UpdateScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;

public class SwarmApplication extends Game {
	public static final float UPDATES_PER_SECOND = 60f;
	public static final float SECONDS_PER_UPDATE = 1f / UPDATES_PER_SECOND;
	
	private UpdateScreen updateScreen;
	private float unprocessedSeconds;
	
	private InputMultiplexer inputs = new InputMultiplexer();
	
	private FPSLogger logger = new FPSLogger();
	
	@Override
	public void create() {
		setScreen(new LoadingScreen());
		
		Gdx.input.setInputProcessor(inputs);
	}
	
	@Override
	public void render() {
		logger.log();
		
		unprocessedSeconds += Gdx.graphics.getDeltaTime();
		
		while (unprocessedSeconds > SECONDS_PER_UPDATE) {
			unprocessedSeconds -= SECONDS_PER_UPDATE;
			
			updateScreen.update();
		}
		
		super.render();
	}
	
	@Override
	public void setScreen(Screen screen) {
		if (screen instanceof UpdateScreen) {
			updateScreen = (UpdateScreen) screen;
			
			updateScreen.game = this;
			
			inputs.clear();
			inputs.addProcessor(updateScreen);
		} else {
			throw new IllegalArgumentException();
		}
		
		super.setScreen(screen);
	}
	
	public void addInputProcessor(InputProcessor input) {
		inputs.addProcessor(0, input);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		Gdx.app.getPreferences(Util.PREFERENCE).flush();
	}
}
