package no.progark.a18.towerdefence.gameContent;

import java.util.LinkedList;
import java.util.List;

import no.progark.a18.towerdefence.level.CreepKilledListener;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class Creep extends TowerDefenceSprite {
	private List<CreepKilledListener> creepKilledListeners = new LinkedList<CreepKilledListener>();
	
	private int hp = 20;
	private int scooreValue = 100;
	private int goldValue = 1;
	public int getGoldValue() {
		return goldValue;
	}

	public void setGoldValue(int goldValue) {
		this.goldValue = goldValue;
	}

	public int getScooreValue() {
		return scooreValue;
	}

	public void setScooreValue(int scooreValue) {
		this.scooreValue = scooreValue;
	}

	public Creep(	float posX, float posY,
					float pWidth, float pHeight,
					ITextureRegion pTextureRegion,
					VertexBufferObjectManager pVertexBufferObjectManager,
					CreepKilledListener killListener) {
		
		super(posX, posY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		this.creepKilledListeners.add(killListener);
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		if(hp <= 0)
			fireCreepKilled();
		this.hp = hp;
	}
	
	public void fireCreepKilled(){
		for(CreepKilledListener ckl : creepKilledListeners)
			ckl.creepKilled(this);
	}
	
	public void registerCreepKilledListener(CreepKilledListener ckl){
		creepKilledListeners.add(ckl);
	}
	
	public boolean removeCreepKilledListener(CreepKilledListener ckl){
		return creepKilledListeners.remove(ckl);
	}
}
