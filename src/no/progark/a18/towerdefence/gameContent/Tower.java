package no.progark.a18.towerdefence.gameContent;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Tower extends Sprite {
	
	private static final String tag = Tower.class.getName();
	private int gridPosX;
	private int gridPosY;
	private int damage;
	private int range;
	private Cell[][] board;

	public Tower(	int gridPosX, int gridPosY,
					float pWidth, float pHeight,
					ITextureRegion pTextureRegion,
					VertexBufferObjectManager pVertexBufferObjectManager,
					Cell[][] creepBoard) {
		
		super(0f, 0f, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		this.gridPosX = gridPosX;
		this.gridPosY = gridPosY;
		this.board = creepBoard;
		damage = 5;
		range = 1;

		registerUpdateHandler(new AttackHandler(1f));
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
					creep.setHp(creep.getHp() - damage);
					pauseSoFar = 0;
				}
			}
		}

		public void reset() { }
	}
}
