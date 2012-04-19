package no.progark.a18.towerdefence.level;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.logic.Scene;

/**
 * The first level for the TowerDefence game
 */
public class FirstLevel extends Scene {
	private final TowerDefenceActivity TDA;
	
	FirstLevel(TowerDefenceActivity tda){
		this.TDA = tda;
	}
	
	void createLevel(){
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("sprites/");
		
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(
				TDA.getTextureManager(), 32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		ITextureRegion faceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bitmapTextureAtlas, TDA, "face_box.png", 0, 0);
		TDA.getEngine().getTextureManager().loadTexture(bitmapTextureAtlas);
		
		
	}

	@Override
	public void loadResourses(BaseGameActivity tda) {
		// TODO Auto-generated method stub
		
	}

}
