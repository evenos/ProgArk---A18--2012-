package no.progark.a18.towerdefence.gameContent;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Cell extends Sprite {
	private boolean isRoad;
	private Direction dirToNextRoad;
	private boolean isFinish;
	private List<Creep> creeps;

	public Cell(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, boolean isRoad, boolean isFinish) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		
		this.isRoad = isRoad;
		this.isFinish = isFinish;
		this.creeps = new ArrayList<Creep>();
	}
	
	public Cell(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, boolean isRoad) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, isRoad, false);
	}

	public Direction getDirToNextRoad() {
		return dirToNextRoad;
	}

	public void setDirToNextRoad(Direction dirToNextRoad) {
		this.dirToNextRoad = dirToNextRoad;
	}

	public boolean isRoad() {
		return isRoad;
	}

	public boolean isFinish() {
		return isFinish;
	}
	
	public void addCreep(Creep creep){
		creeps.add(creep);
	}
	
	public void removeCreep(Creep creep){
		creeps.remove(creep);
	}
	
	public Creep peakCreep(){
		return creeps.isEmpty() ? null : creeps.get(0);
	}
	
	public boolean containsCreep(Creep creep){
		return creeps.contains(creep);
	}
}
