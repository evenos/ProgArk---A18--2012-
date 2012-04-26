package no.progark.a18.towerdefence.gameContent;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

public class Tower extends Sprite {
	private static final String TAG = Tower.class.getName();
	private int x, y;
	private int damage;
	private int range;
	private Cell[][] board;

	public Tower(int x, int y, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			Cell[][] creepBoard) {
		super(0f, 0f, pWidth, pHeight, pTextureRegion,
				pVertexBufferObjectManager);
		this.x = x;
		this.y = y;
		this.board = creepBoard;
		damage = 5;
		range = 1;

		registerUpdateHandler(new AtackHandeler(1f));
	}

	private class AtackHandeler implements IUpdateHandler {
		private float speed;
		private float pauseSoFare;

		public AtackHandeler(float speed) {
			this.speed = speed;
			this.pauseSoFare = speed;
		}

		public void onUpdate(float pSecondsElapsed) {
			if (pauseSoFare + pSecondsElapsed < speed) {
				pauseSoFare += pSecondsElapsed;
				return;
			}
			for (int row = y - range; row <= y + range; row++) {
				if (row < 0 || row >= board.length)
					continue;
				for (int coll = x - range; coll <= x + range; coll++) {
					if (coll < 0 || coll >= board[row].length)
						continue;
					Creep creep = board[row][coll].peakCreep();
					if (creep == null)
						continue;
					creep.setHp(creep.getHp() - damage);
					pauseSoFare = 0;
				}
			}
		}

		public void reset() {
			// TODO Auto-generated method stub

		}
	}
}
