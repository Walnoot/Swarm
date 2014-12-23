package walnoot.swarm.screens;

import walnoot.swarm.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class LoadingScreen extends UpdateScreen {
	private static final String ATLAS_NAME = "pack.atlas";
	private AssetManager manager;
	
	public LoadingScreen() {
		manager = new AssetManager();
		Util.assets = manager;
		
		manager.load(ATLAS_NAME, TextureAtlas.class);
		manager.load("background.png", Texture.class);
		manager.load("kill.wav", Sound.class);
		manager.load("nextlevel.wav", Sound.class);
		manager.load("click.wav", Sound.class);
	}
	
	@Override
	public void render(float delta) {
		float progress = manager.getProgress();
		
		Gdx.gl20.glClearColor(progress, progress, progress, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (manager.update()) {
			Util.atlas = manager.get(ATLAS_NAME, TextureAtlas.class);
			Util.loadSkin();
			
			game.setScreen(new MainMenu());
		}
	}
}
