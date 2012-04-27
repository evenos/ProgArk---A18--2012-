package no.progark.a18.towerdefence.gameContent;

import no.progark.a18.towerdefence.level.ReatchedTargetListener;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class Creep extends TowerDefenceSprite {
	
	private int hp = 20;
	private int scooreValue = 100;
	public int getScooreValue() {
		return scooreValue;
	}

	public void setScooreValue(int scooreValue) {
		this.scooreValue = scooreValue;
	}

	private KillListener killListener;

	public Creep(	float posX, float posY,
					float pWidth, float pHeight,
					ITextureRegion pTextureRegion,
					VertexBufferObjectManager pVertexBufferObjectManager,
					ReatchedTargetListener reatchedTargetListner,
					KillListener killListener) {
		
		super(posX, posY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, reatchedTargetListner);
		this.killListener = killListener;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		if(hp <= 0)
			killListener.wasKilled(this);
		this.hp = hp;
	}




}
