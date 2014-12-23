package walnoot.swarm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Util {
	public static final String PREFERENCE = "Swarm";
	public static final String PREF_CURRENT_LEVEL = "currentLevel";
	
	public static AssetManager assets;
	public static TextureAtlas atlas;
	public static Skin skin;
	
	public static final ChangeListener CLICK_LISTENER = new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			playSound("click");
		}
	};
	
	public static void playSound(String name) {
		Sound sound = assets.get(name + ".wav", Sound.class);
		if (sound != null) sound.play();
	}
	
	public static void loadSkin() {
		skin = new Skin();
		skin.add("default", atlas);
		addPatch("border");
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Regular.ttf"));
		BitmapFont font = generator.generateFont(24);
		skin.add("default", font);
		
		skin.load(Gdx.files.internal("skin.json"));
	}
	
	private static void addPatch(String name) {
		AtlasRegion region = atlas.findRegion(name);
		int halfWidth = region.getRegionWidth() / 2 - 1;
		int halfHeight = region.getRegionHeight() / 2 - 1;
		
		skin.add(name, new NinePatch(region, halfWidth, halfWidth, halfHeight, halfHeight));
	}
}
