package walnoot.swarm.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BloodEntity extends SpriteEntity {
	public BloodEntity(Vector2 pos) {
		super("blood", 1f);
		
		this.pos.set(pos);
		
		animation.getSprite().setRotation(MathUtils.random(360f));
	}
	
	@Override
	protected int getRenderLayer() {
		return -1;
	}
	
	@Override
	public boolean isPersistent() {
		return true;
	}
}
