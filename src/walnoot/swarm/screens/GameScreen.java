package walnoot.swarm.screens;

import walnoot.swarm.Util;
import walnoot.swarm.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class GameScreen extends UpdateScreen {
	private static final Vector3 TMP = new Vector3();
	
	private World world;
	private OrthographicCamera camera = new OrthographicCamera();
	private SpriteBatch batch = new SpriteBatch();
	
	private Stage stage;
	private Label counterLabel, infoLabel, nameLabel, hintLabel;
	private TextButton button;
	
	private Json json;
	private JsonValue jsonValue;
	
	private String level;
	
	private Sprite backgroundSprite;
	
	public GameScreen(String level) {
		this.level = level;
	}
	
	@Override
	public void show() {
		Texture background = Util.assets.get("background.png", Texture.class);
		background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		backgroundSprite = new Sprite(background);
		
		json = new Json();
		jsonValue = new JsonReader().parse(Gdx.files.internal(level + ".json"));
		
		world = new World(json, jsonValue);
		
		stage = new Stage();
		game.addInputProcessor(stage);
		
		Table table = new Table();
		stage.addActor(table);
		table.pad(4f).setFillParent(true);
		
		nameLabel = new Label(world.getName(), Util.skin);
		table.add(nameLabel).expand().top().left();
		
		hintLabel = new Label(null, Util.skin);
		table.add(hintLabel).expand().colspan(2).top().right().row();
		
		infoLabel = new Label("Place bait", Util.skin);
		table.add(infoLabel).expandX().height(50f);
		
		counterLabel = new Label(null, Util.skin);
		table.add(counterLabel).expandX().height(50f);
		
		button = new TextButton("Start", Util.skin);
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				TextButton button = (TextButton) actor;
				
				if (world.isFinished()) {
					nextLevel();
				} else if (world.isStarted()) {
					reset();
					button.setText("Start");
				} else {
					world.start();
					button.setText("Reset");
				}
			}
		});
		button.addListener(Util.CLICK_LISTENER);
		table.add(button).expandX().fill();
	}
	
	private void reset() {
		world.reset();
		
		hintLabel.setText(jsonValue.getString("hint", null));
	}
	
	private void nextLevel() {
		Util.playSound("nextlevel");
		
		hintLabel.setText(null);
		
		level = jsonValue.getString("nextLevel", null);
		
		if (level != null) {
			jsonValue = new JsonReader().parse(Gdx.files.internal(level + ".json"));
			
			world = new World(json, jsonValue);
			
			nameLabel.setText(world.getName());
			button.setText("Start");
			
			Gdx.app.getPreferences(Util.PREFERENCE).putString(Util.PREF_CURRENT_LEVEL, level);
		} else {
			game.setScreen(new EndScreen());
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		backgroundSprite.draw(batch);
		world.render(batch);
		batch.end();
		
		stage.act();
		stage.draw();
	}
	
	@Override
	public void update() {
		world.update();
		
		counterLabel.setText(String.format("%d/%d flies", world.getMaxFliesFinished(), world.getRequiredFlies()));
		infoLabel.setText(world.isStarted() ? "Get the flies to the goal" : "Place Bait by clicking");
		if (world.isFinished()) button.setText("Continue");
	}
	
	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = 2f * width / height;
		camera.viewportHeight = 2f;
		camera.zoom = 16f;
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		stage.setViewport(width, height);
		
		float bWidth = camera.viewportWidth * camera.zoom;
		float bHeight = camera.viewportHeight * camera.zoom;
		
		backgroundSprite.setRegion(0f, 0f, bWidth * 0.5f, bHeight * 0.5f);
		backgroundSprite.setSize(bWidth, bHeight);
		backgroundSprite.setPosition(-bWidth * 0.5f, -bHeight * 0.5f);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		camera.unproject(TMP.set(screenX, screenY, 0f));
		
		if (button == Buttons.LEFT) world.addAttractEntity(TMP.x, TMP.y);
		else if (button == Buttons.RIGHT) world.destroyAttractEntity(TMP.x, TMP.y);
		
		return true;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER) world.start();
		
		return true;
	}
}
