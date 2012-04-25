package no.progark.a18.towerdefence.level;

import org.andengine.opengl.texture.region.TextureRegion;

public class BoardCell {
	private TextureRegion background;
	
	public BoardCell(TextureRegion background) {
		this.background = background;	
	}

	public TextureRegion getBackground() {
		return background;
	}

	public void setBackground(TextureRegion background) {
		this.background = background;
	}

}
