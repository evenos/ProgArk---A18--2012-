package no.progark.a18.towerdefence.gameContent;

import no.progark.a18.towerdefence.TowerDefenceActivity;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class Tower extends Sprite {
	private int gridPosX;
	private int gridPosY;
	private int damage;
	private int range;
	private Cell[][] board;
	private final TowerDefenceActivity TDA;

	public Tower(	int gridPosX, int gridPosY,
					float pWidth, float pHeight,
					ITextureRegion pTextureRegion,
					VertexBufferObjectManager pVertexBufferObjectManager,
					Cell[][] creepBoard, TowerDefenceActivity tda) {
		
		super(0f, 0f, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		this.TDA = tda;
		this.gridPosX = gridPosX;
		this.gridPosY = gridPosY;
		this.board = creepBoard;
		damage = 5;
		range = 1;

		registerUpdateHandler(new AttackHandler(1f));
	}


	public int getgridPosX() {
		return gridPosY;
	}
	
	public void setGridPosX(int gridPosX){
		this.gridPosX = gridPosX;
	}

	public int getGridPosY() {
		return gridPosY;
	}
	
	public void setGridPosY(int gridPosY){
		this.gridPosY = gridPosY;
	}


	private class AttackHandler implements IUpdateHandler {
		
		private float speed;
		private float pauseSoFar;

		public AttackHandler(float speed) {
			this.speed = speed;
			this.pauseSoFar = speed;
		}

		public void onUpdate(float pSecondsElapsed) {
			if (pauseSoFar + pSecondsElapsed < speed) {
				pauseSoFar += pSecondsElapsed;
				return;
			}
			for (int row = gridPosY - range; row <= gridPosY + range; row++) {
				if (row < 0 || row >= board.length) {
					continue;
				}
				
				for (int coll = gridPosX - range; coll <= gridPosX + range; coll++) {
					if (coll < 0 || coll >= board[row].length) {
						continue;
					}
					
					Creep creep = board[row][coll].peekCreep();
					if (creep == null) {
						continue;
					}
					shoot(creep);
					creep.setHp(creep.getHp() - damage);
					pauseSoFar = 0;
				}
			}
		}
		public void shoot(final Creep other){
			
					final Line line = new Line(getX(), getY(), other.getX(), other.getY(), 10, TDA.getVertexBufferObjectManager());
					line.setColor(Color.PINK);
					line.setVisible(true);
					line.registerUpdateHandler(new IUpdateHandler(){
						long timeStarted = System.currentTimeMillis();

						public void onUpdate(float pSecondsElapsed) {
							if(System.currentTimeMillis() - timeStarted > 400)
								TDA.runOnUpdateThread(new Runnable() {
									
									public void run() {
										TDA.peekState().detachChild(line);
										
									}
								});
						}

						public void reset() {	}
						
					});
					
					TDA.peekState().attachChild(line);
					

		}

		public void reset() { }
	}
}
