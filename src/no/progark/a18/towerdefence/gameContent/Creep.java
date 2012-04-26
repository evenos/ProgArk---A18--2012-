package no.progark.a18.towerdefence.gameContent;

import no.progark.a18.towerdefence.level.ReatchedTargetListener;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class Creep extends TowerdefenceSprite{
	private int hp = 20;
	private KillListener killlistener;

	public Creep(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ReatchedTargetListener reatchedTargetListner, KillListener killListener) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager,
				reatchedTargetListner);
		this.killlistener = killListener;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		if(hp <= 0)
			this.killlistener.wasKilled(this);
		this.hp = hp;
	}




}
