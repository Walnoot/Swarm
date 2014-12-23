package walnoot.swarm.screens;

import walnoot.swarm.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class EndScreen extends UpdateScreen {
	private Stage stage;
	
	@Override
	public void show() {
		stage = new Stage();
		game.addInputProcessor(stage);
		
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		table.add(new Label("Game Over. Thanks for playing!", Util.skin));
	}
	
	@Override
	public void render(float delta) {
		if (Gdx.input.isTouched()) game.setScreen(new MainMenu());
		
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
