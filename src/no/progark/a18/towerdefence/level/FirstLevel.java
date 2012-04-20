package no.progark.a18.towerdefence.level;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.logic.Scene;

/**
 * The first level for the TowerDefence game
 */
public class FirstLevel extends Scene {
	private final TowerDefenceActivity TDA;
	private Font exitFont;
	private BitmapTextureAtlas fontTexture;
	private float displayHeight, displayWidth;
	
	FirstLevel(TowerDefenceActivity tda){
		this.TDA = tda;
		setBackground(new Background(Color.BLUE));
		displayHeight = tda.getDisplayHeight();
		displayWidth = tda.getDisplayWidth();
		loadResourses(tda);
		createLevel();
		
	}
	
	void createLevel(){
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(
				TDA.getTextureManager(), 32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		ITextureRegion faceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bitmapTextureAtlas, TDA, "face_box.png", 0, 0);
		
		TDA.getEngine().getTextureManager().loadTexture(bitmapTextureAtlas);
	}

	@Override
	public void loadResourses(BaseGameActivity tda) {
		this.fontTexture = new BitmapTextureAtlas(tda.getTextureManager(), 256,
				256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.exitFont = FontFactory.createFromAsset(tda.getFontManager(),
				this.fontTexture, tda.getAssets(), "Sabatica-regular.ttf", 48,
				true, Color.GREEN.getABGRPackedInt());
		tda.getEngine().getTextureManager().loadTexture(this.fontTexture);
		tda.getFontManager().loadFont(this.exitFont);
		
		/*
		 * Lage bilde
		 */
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(
				TDA.getTextureManager(), 32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		ITextureRegion faceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bitmapTextureAtlas, TDA, "face_box.png", 0, 0);
		
		TDA.getEngine().getTextureManager().loadTexture(bitmapTextureAtlas);
		
		
		/*
		 * Calculate the coordinates for the face, so its centered on the
		 * camera.
		 */
		final float centerX = (displayWidth - faceTextureRegion.getWidth()) / 2;
		final float centerY = (displayHeight - faceTextureRegion.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		final Sprite face = new Sprite(centerX, centerY, faceTextureRegion,
				tda.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
						pSceneTouchEvent.getY() - this.getHeight() / 2);
				return true;
			}
		};
		face.setScale(4f);
		this.attachChild(face);
		
	}
	
	

}
