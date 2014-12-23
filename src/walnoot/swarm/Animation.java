package walnoot.swarm;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class Animation {
	private final Array<Sprite> sprites;
	private final float animationTime;
	private float timer = 0f;
	private int index;
	
	public Animation(String name, float animationTime, float startTime) {
		this.animationTime = animationTime;
		sprites = Util.atlas.createSprites(name);
		timer = startTime;
	}
	
	public void update() {
		timer += SwarmApplication.SECONDS_PER_UPDATE;
		timer %= animationTime;
		
		index = (int) (timer / animationTime * sprites.size);
	}
	
	public Sprite getSprite() {
		return sprites.get(index);
	}
	
	public Array<Sprite> getSprites() {
		return sprites;
	}
}
