package walnoot.swarm.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SwatterEntity extends SpriteEntity {
	private static final Vector2 HIT_POSITION = new Vector2(0.75f, -0.5f);
	private static final Vector2 TMP = new Vector2();
	private static final float HIT_RADIUS = 1f;
	
	private Vector2 hitPos;
	
	public SwatterEntity(boolean flip) {
		super("swatter", 4f, 4f, 0.4f, MathUtils.random());
		
		hitPos = new Vector2(HIT_POSITION);
		if (flip) {
			hitPos.x = -hitPos.x;
			flip(true, false);
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		for (int i = 0; i < world.getEntities().size; i++) {
			Entity entity = world.getEntities().get(i);
			
			if (entity instanceof FlyEntity) {
				if (TMP.set(pos).add(hitPos).dst2(entity.pos) < HIT_RADIUS * HIT_RADIUS) entity.remove();
			}
		}
	}
	
	@Override
	public boolean isPersistent() {
		return true;
	}
	
	@Override
	protected int getRenderLayer() {
		return 1;
	}
}
