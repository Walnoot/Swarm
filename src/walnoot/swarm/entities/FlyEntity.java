package walnoot.swarm.entities;

import walnoot.swarm.SwarmApplication;
import walnoot.swarm.Util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class FlyEntity extends SpriteEntity {
	private static final float AVOIDANCE_RADIUS = 0.5f;
	private static final float AVOIDANCE_STRENGTH = 3f;
	private static final float COHESION_STRENGTH = .004f;
	private static final float ATTRACT_STRENGTH = 5f;
	private static final float ATTRACT_RADIUS = 5f;
	
	private static final Vector2 TMP = new Vector2();
	
	public FlyEntity() {
		super("fly", .2f, MathUtils.random());
	}
	
	@Override
	public void update() {
		super.update();
		
		pos.x += MathUtils.random(-1f, 1f) * SwarmApplication.SECONDS_PER_UPDATE * 2f;
		pos.y += MathUtils.random(-1f, 1f) * SwarmApplication.SECONDS_PER_UPDATE * 2f;
		
		//O(n^2) :/
		float closestAttractDist2 = Float.MAX_VALUE;
		AttractEntity closestAttract = null;
		
		for (int i = 0; i < world.getEntities().size; i++) {
			Entity e = world.getEntities().get(i);
			
			if (e instanceof FlyEntity) {
				if (TMP.set(pos).sub(e.pos).len2() < AVOIDANCE_RADIUS * AVOIDANCE_RADIUS)
					pos.add(TMP.nor().scl(SwarmApplication.SECONDS_PER_UPDATE * AVOIDANCE_STRENGTH));
			} else if (e instanceof AttractEntity) {
				float dist = TMP.set(pos).dst2(e.pos);
				if (dist < closestAttractDist2) {
					closestAttractDist2 = dist;
					closestAttract = (AttractEntity) e;
				}
			}
		}
		
		if (closestAttract != null) {
			float strength = Math.min(closestAttractDist2, ATTRACT_RADIUS) / ATTRACT_RADIUS * ATTRACT_STRENGTH;
			
			pos.add(TMP.set(closestAttract.pos).sub(pos).nor().scl(SwarmApplication.SECONDS_PER_UPDATE * strength));
			
			if (closestAttractDist2 < 1f) closestAttract.markRemoval();
		}
		
		pos.add(TMP.set(world.avgFlyPos).sub(pos).nor()
				.scl(SwarmApplication.SECONDS_PER_UPDATE * COHESION_STRENGTH * world.avgFlyPos.dst2(pos)));
		
		world.cumFlyPos.add(pos);
		world.numFlies++;
	}
	
	@Override
	protected void onRemove() {
		world.addEntity(new BloodEntity(pos));
		
		Util.playSound("kill");
	}
}
