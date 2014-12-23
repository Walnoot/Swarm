package walnoot.swarm.entities;

import walnoot.swarm.SwarmApplication;
import walnoot.swarm.Util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BladeEntity extends SpriteEntity {
	private static final float SPIN_SPEED = 20f;
	private static final float HIT_RADIUS = 2f;
	private static final Vector2 TMP = new Vector2();
	
	private Vector2 path, startPos;
	private int timer;
	private final float speed;
	private boolean isBloody = false;
	
	public BladeEntity(float x, float y, float width, float height, float speed) {
		super("blade", 2 * HIT_RADIUS, 2 * HIT_RADIUS, 1f, 0f);
		
		startPos = new Vector2(x, y);
		path = new Vector2(width, height);
		this.speed = speed / MathUtils.PI2;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		animation.getSprite().rotate(SwarmApplication.SECONDS_PER_UPDATE * SPIN_SPEED);
		
		super.render(batch);
	}
	
	@Override
	public void update() {
		super.update();
		
		if (world.isStarted()) timer++;
		else timer = 0;
		
		pos.set(startPos).add(MathUtils.sin(timer * speed) * path.x, MathUtils.cos(timer * speed) * path.y);
		
		for (int i = 0; i < world.getEntities().size; i++) {
			Entity entity = world.getEntities().get(i);
			
			if (entity instanceof FlyEntity) {
				if (TMP.set(pos).dst2(entity.pos) < HIT_RADIUS * HIT_RADIUS) {
					entity.remove();
					
					if (!isBloody) {
						animation.getSprite().setRegion(Util.atlas.findRegion("blade_bloody"));
						isBloody = true;
					}
				}
			}
		}
	}
	
	@Override
	protected int getRenderLayer() {
		return 1;
	}
	
	@Override
	public boolean isPersistent() {
		return true;
	}
}
