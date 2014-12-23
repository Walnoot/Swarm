package walnoot.swarm.screens;

import walnoot.swarm.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenu extends UpdateScreen {
	private Stage stage;
	
	@Override
	public void show() {
		stage = new Stage();
		game.addInputProcessor(stage);
		
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		table.add(new Image(Util.atlas.findRegion("logo"))).pad(4f).row();
		
		final String level = Gdx.app.getPreferences(Util.PREFERENCE).getString(Util.PREF_CURRENT_LEVEL, "level1");
		
		TextButton newGameButton = new TextButton("New Game", Util.skin);
		newGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new GameScreen("level1"));
			}
		});
		newGameButton.addListener(Util.CLICK_LISTENER);
		table.add(newGameButton).fill().pad(4f).row();
		
		TextButton continueGameButton = new TextButton("Continue Game", Util.skin);
		continueGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new GameScreen(level));
			}
		});
		continueGameButton.addListener(Util.CLICK_LISTENER);
		table.add(continueGameButton).fill().pad(4f).row();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		stage.act();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height);
	}
}
