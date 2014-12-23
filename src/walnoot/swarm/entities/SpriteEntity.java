package walnoot.swarm.entities;

import walnoot.swarm.Animation;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class SpriteEntity extends Entity {
	private final float width, height;
	protected Animation animation;
	
	public SpriteEntity(String spriteName, float animationTime) {
		this(spriteName, 1f, 1f, animationTime, 0f);
	}
	
	public SpriteEntity(String spriteName, float animationTime, float startTime) {
		this(spriteName, 1f, 1f, animationTime, startTime);
	}
	
	public SpriteEntity(String spriteName, float width, float height, float animationTime, float startTime) {
		this.width = width;
		this.height = height;
		
		animation = new Animation(spriteName, animationTime, startTime);
		
		for (Sprite sprite : animation.getSprites()) {
			sprite.setSize(width, height);
			sprite.setOrigin(width * 0.5f, height * 0.5f);
		}
	}
	
	@Override
	public void update() {
		animation.update();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		Sprite sprite = animation.getSprite();
		
		sprite.setPosition(pos.x - width * 0.5f, pos.y - height * 0.5f);
		sprite.draw(batch);
	}
	
	public void flip(boolean x, boolean y) {
		for (Sprite s : animation.getSprites()) {
			s.flip(x, y);
		}
	}
}
