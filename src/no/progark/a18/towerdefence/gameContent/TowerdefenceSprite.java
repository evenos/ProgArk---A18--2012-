package no.progark.a18.towerdefence.gameContent;

import no.progark.a18.towerdefence.level.ReatchedTargetListener;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class TowerdefenceSprite extends Sprite{
	protected ReatchedTargetListener reatchedTargetListner;
	
	private float speedX, speedY;
	
	
	
	
	
	public TowerdefenceSprite(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, ReatchedTargetListener reatchedTargetListner) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		
		this.reatchedTargetListner = reatchedTargetListner;
		
		registerUpdateHandler(new IUpdateHandler(){
			
			
			long lastUpdate = System.currentTimeMillis();

			public void onUpdate(float pSecondsElapsed) {
				long delta = System.currentTimeMillis() - lastUpdate;
				lastUpdate = System.currentTimeMillis();
				
				setX(getX() + (getSpeedX()/(1000- delta)*3));
				setY(getY() + (getSpeedY()/(1000- delta)*3));
			}

			public void reset() {
				// TODO Auto-generated method stub
				
			}
			
		});
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
