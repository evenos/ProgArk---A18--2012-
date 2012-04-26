package no.progark.a18.towerdefence.gameContent;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Cell extends Sprite {
	
	private boolean isRoad;
	private Direction directionToNextRoad;
	private boolean isGoal;
	private List<Creep> creeps;

	public Cell(float posX, float posY,
				float pWidth, float pHeight,
				ITextureRegion pTextureRegion,
				VertexBufferObjectManager pVertexBufferObjectManager,
				boolean isRoad, boolean isGoal) {
		
		super(posX, posY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		
		this.isRoad = isRoad;
		this.isGoal = isGoal;
		this.creeps = new ArrayList<Creep>();
	}
	
	public Cell(float posX, float posY,
				float pWidth, float pHeight,
				ITextureRegion pTextureRegion,
				VertexBufferObjectManager pVertexBufferObjectManager,
				boolean isRoad) {
		
		this(posX, posY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, isRoad, false);
	}

	public Direction getDirectionToNextRoad() {
		return directionToNextRoad;
	}

	public void setDirectionToNextRoad(Direction dirToNextRoad) {
		this.directionToNextRoad = dirToNextRoad;
	}

	public boolean isRoad() {
		return isRoad;
	}

	public boolean isGoal() {
		return isGoal;
	}
	
	public void addCreep(Creep creep){
		creeps.add(creep);
	}
	
	public void removeCreep(Creep creep){
		creeps.remove(creep);
	}
	
	public Creep peekCreep(){
		return creeps.isEmpty() ? null : creeps.get(0);
	}
	
	public boolean containsCreep(Creep creep){
		return creeps.contains(creep);
	}
}
