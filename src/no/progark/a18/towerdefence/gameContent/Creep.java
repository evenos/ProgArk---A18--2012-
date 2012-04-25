package no.progark.a18.towerdefence.gameContent;

import no.progark.a18.towerdefence.level.ReatchedTargetListener;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Creep extends TowerdefenceSprite {

	public Creep(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ReatchedTargetListener reatchedTargetListner) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager,
				reatchedTargetListner);
	}




}
