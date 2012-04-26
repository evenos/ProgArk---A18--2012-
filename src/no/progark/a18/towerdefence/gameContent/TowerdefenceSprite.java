package no.progark.a18.towerdefence.gameContent;

import no.progark.a18.towerdefence.level.ReatchedTargetListener;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class TowerDefenceSprite extends Sprite {
	
	protected ReatchedTargetListener reachedTargetListener;
	private float speedX;
	private float speedY;
	
	public TowerDefenceSprite(	float posX, float posY,
								float pWidth, float pHeight,
								ITextureRegion pTextureRegion,
								VertexBufferObjectManager pVertexBufferObjectManager,
								ReatchedTargetListener reachedTargetListener) {
		
		super(posX, posY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		this.reachedTargetListener = reachedTargetListener;
		
		registerUpdateHandler(new TowerDefenceSpriteUpdateHandler());
	}
	
	public class TowerDefenceSpriteUpdateHandler implements IUpdateHandler {
		
		private long lastUpdate;
		
		public TowerDefenceSpriteUpdateHandler() {
			lastUpdate = System.currentTimeMillis();
		}

		public void onUpdate(float pSecondsElapsed) {
			long delta = System.currentTimeMillis() - lastUpdate;
			lastUpdate = System.currentTimeMillis();
			
			setX(getX() + (getSpeedX()/(1000- delta)*3));
			setY(getY() + (getSpeedY()/(1000- delta)*3));
		}

		public void reset() { }
	}

	public float getSpeedX() {
		return speedX;
	}

	public float getSpeedY() {
		return speedY;
	}
	
	public void setSpeed(float speedX, float speedY){
		this.speedX = speedX;
		this.speedY = speedY;
	}
}
