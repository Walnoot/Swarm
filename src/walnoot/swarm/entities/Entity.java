package walnoot.swarm.entities;

import walnoot.swarm.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity implements Comparable<Entity> {
	public World world;
	public Vector2 pos = new Vector2();
	public Vector2 prevPos = new Vector2();
	
	public abstract void render(SpriteBatch batch);
	
	public abstract void update();
	
	@Override
	public int compareTo(Entity o) {
		if (getRenderLayer() > o.getRenderLayer()) return 1;
		else if (getRenderLayer() < o.getRenderLayer()) return -1;
		else return (int) Math.signum(o.pos.y - pos.y);
	}
	
	public void remove() {
		world.removeEntity(this);
		
		onRemove();
	}
	
	protected void onRemove() {
	}
	
	protected int getRenderLayer() {
		return 0;
	}
	
	/**
	 * @return Whether this entity stays when the screen is reset.
	 */
	public boolean isPersistent() {
		return false;
	}
}
