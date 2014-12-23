package walnoot.swarm;

import walnoot.swarm.entities.AttractEntity;
import walnoot.swarm.entities.BladeEntity;
import walnoot.swarm.entities.Entity;
import walnoot.swarm.entities.FlyEntity;
import walnoot.swarm.entities.SwatterEntity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class World {
	public Vector2 cumFlyPos = new Vector2();
	public Vector2 avgFlyPos = new Vector2();
	public int numFlies;
	
	private String name;
	private Sprite goalSprite;
	private Sprite startSprite;
	private Rectangle goal;
	private Vector2 start;
	
	private DelayedRemovalArray<Entity> entities = new DelayedRemovalArray<Entity>();
	private int requiredFlies, numFinishedFlies, maxFliesFinished;
	private int startFlies;
	private boolean started;
	
	public World(Json json, JsonValue jsonValue) {
		name = jsonValue.getString("name", "");
		
		goal = json.readValue(Rectangle.class, jsonValue.get("goal"));
		start = json.readValue(Vector2.class, jsonValue.get("start"));
		if (start == null) start = new Vector2();
		requiredFlies = jsonValue.getInt("requiredFlies", 10);
		startFlies = jsonValue.getInt("startFlies", 100);
		
		goalSprite = Util.atlas.createSprite("goal");
		goalSprite.setBounds(goal.x, goal.y, goal.width, goal.height);
		
		startSprite = Util.atlas.createSprite("repel");
		startSprite.setSize(1f, 1f);
		startSprite.setPosition(start.x - 0.5f, start.y - 0.5f);
		
		JsonValue swatters = jsonValue.get("swatters");
		if (swatters != null) addObstacles(swatters, true);
		
		JsonValue blades = jsonValue.get("blades");
		if (blades != null) addObstacles(blades, false);
	}
	
	private void addObstacles(JsonValue swatters, boolean isSwatter) {
		JsonValue currentValue = swatters.child;
		while (currentValue != null) {
			if (isSwatter) {
				boolean flip = currentValue.get(2) == null ? false : currentValue.getBoolean(2);
				
				SwatterEntity swatter = new SwatterEntity(flip);
				swatter.pos.set(currentValue.getInt(0), currentValue.getInt(1));
				addEntity(swatter);
			} else {
				float width = currentValue.get(2) == null ? 0f : currentValue.getFloat(2);
				float height = currentValue.get(3) == null ? 0f : currentValue.getFloat(3);
				float speed = currentValue.get(4) == null ? 0f : currentValue.getFloat(4);
				
				BladeEntity blade =
					new BladeEntity(currentValue.getFloat(0), currentValue.getFloat(1), width, height, speed);
				addEntity(blade);
			}
			
			currentValue = currentValue.next;
		}
	}
	
	public void render(SpriteBatch batch) {
		goalSprite.draw(batch);
		if (!started) startSprite.draw(batch);
		
		for (Entity e : entities) {
			e.render(batch);
		}
	}
	
	public void update() {
		entities.begin();
		
		int numFinishedFlies = 0;
		
		for (Entity e : entities) {
			e.update();
			
			if (e instanceof FlyEntity && goal.contains(e.pos)) numFinishedFlies++;
		}
		
		for (Entity e : entities) {
			e.prevPos.set(e.pos);
		}
		
		this.numFinishedFlies = numFinishedFlies;
		maxFliesFinished = Math.max(numFinishedFlies, maxFliesFinished);
		
		entities.end();
		
		entities.sort();
		
		if (numFlies > 0) {
			avgFlyPos.set(cumFlyPos).div(numFlies);
			cumFlyPos.set(0f, 0f);
			numFlies = 0;
		}
	}
	
	public void start() {
		if (!started) {
			for (int i = 0; i < startFlies; i++) {
				FlyEntity entity = new FlyEntity();
				entity.pos.set(MathUtils.random(-3f, 3f) + start.x, MathUtils.random(-3f, 3f) + start.y);
				addEntity(entity);
			}
		}
		
		started = true;
	}
	
	public void reset() {
		started = false;
		
		entities.begin();
		for (Entity e : entities) {
			if (!e.isPersistent()) removeEntity(e);
		}
		entities.end();
		
		maxFliesFinished = 0;
		numFinishedFlies = 0;
	}
	
	public void addAttractEntity(float x, float y) {
		if (!started) {
			AttractEntity attractEntity = new AttractEntity();
			attractEntity.pos.set(x, y);
			addEntity(attractEntity);
		}
	}
	
	public void destroyAttractEntity(float x, float y) {
		if (!started) {
			entities.begin();
			for (Entity e : entities) {
				if (e instanceof AttractEntity && e.pos.dst2(x, y) < 1f) e.remove();
			}
			entities.end();
		}
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
		
		e.world = this;
	}
	
	public void removeEntity(Entity e) {
		entities.removeValue(e, true);
	}
	
	public int getNumFinishedFlies() {
		return numFinishedFlies;
	}
	
	public int getStartFlies() {
		return startFlies;
	}
	
	public int getMaxFliesFinished() {
		return maxFliesFinished;
	}
	
	public int getRequiredFlies() {
		return requiredFlies;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isFinished() {
		return maxFliesFinished >= requiredFlies;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public DelayedRemovalArray<Entity> getEntities() {
		return entities;
	}
}
