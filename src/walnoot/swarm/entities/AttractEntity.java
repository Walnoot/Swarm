package walnoot.swarm.entities;

import walnoot.swarm.SwarmApplication;

public class AttractEntity extends SpriteEntity {
	private static final int REMOVAL_DELAY = (int) (2f * SwarmApplication.UPDATES_PER_SECOND);
	private boolean removing;
	private int timer;
	
	public AttractEntity() {
		super("attract", 1f);
	}
	
	@Override
	public void update() {
		super.update();
		
		if (removing) timer++;
		if (timer == REMOVAL_DELAY) world.removeEntity(this);
	}
	
	public void markRemoval() {
		removing = true;
	}
	
	@Override
	protected int getRenderLayer() {
		return 2;
	}
}
