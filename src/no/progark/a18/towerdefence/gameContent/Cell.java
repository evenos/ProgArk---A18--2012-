package no.progark.a18.towerdefence.gameContent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import no.progark.a18.towerdefence.level.CreepEnteredCellListener;
import no.progark.a18.towerdefence.level.Direction;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Cell extends Sprite{
	
	private boolean isRoad;
	private Direction directionToNextRoad;
	private boolean isGoal;
	private List<Creep> creeps;
	private TouchListener touchListener;
	private int gridPosX, gridPosY;
	private List<CreepEnteredCellListener> creepEnteredCellListeners = new LinkedList<CreepEnteredCellListener>();

	public Cell(float posX, float posY,
				float pWidth, float pHeight,
				int gridPosX, int gridPosY,
				ITextureRegion pTextureRegion,
				VertexBufferObjectManager pVertexBufferObjectManager,
				boolean isRoad, boolean isGoal) {
		
		super(posX, posY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		
		this.isRoad = isRoad;
		this.isGoal = isGoal;
		this.creeps = new ArrayList<Creep>();
		this.gridPosX = gridPosX;
		this.gridPosY = gridPosY;
		
	}
	
	public Cell(float posX, float posY,
				float pWidth, float pHeight,
				int gridPosX, int gridPosY,
				ITextureRegion pTextureRegion,
				VertexBufferObjectManager pVertexBufferObjectManager,
				boolean isRoad) {
		
		this(posX, posY, pWidth, pHeight, gridPosX, gridPosY, pTextureRegion, pVertexBufferObjectManager, isRoad, false);
	}
	
	public int getGridPosX() {
		return gridPosX;
	}

	public void setGridPosX(int gridPosX) {
		this.gridPosX = gridPosX;
	}

	public int getGridPosY() {
		return gridPosY;
	}

	public void setGridPosY(int gridPosY) {
		this.gridPosY = gridPosY;
	}

	public void setTouchListener(TouchListener touchListener) {
		this.touchListener = touchListener;
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
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if(touchListener == null) return false;
		return touchListener.handleTouch(this);
	}

	public void addCreep(Creep creep){
		creeps.add(creep);
		fireCreepEnteredCellListener(creep);
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
	
	public void fireCreepEnteredCellListener(Creep creep){
		for(CreepEnteredCellListener cecl: creepEnteredCellListeners )
			cecl.creepEnteredCell(creep, this);
	}

	public void registerCreepEnteredCellListener(CreepEnteredCellListener cecl) {
		creepEnteredCellListeners.add(cecl);
	}
	
	public boolean removeCreepEnteredCellListener(CreepEnteredCellListener cecl) {
		return creepEnteredCellListeners.remove(cecl);
	}
}
