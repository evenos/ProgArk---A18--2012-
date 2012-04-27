package no.progark.a18.towerdefence.level;

import java.util.ArrayList;
import java.util.List;

import no.progark.a18.towerdefence.TowerDefenceActivity;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

/**
 * The first level for the TowerDefence game
 */
public class FirstLevel extends Scene {
	
	private final TowerDefenceActivity tda;
	private Font exitFont;
	private BitmapTextureAtlas fontTexture;

	private float displayHeight;
	@SuppressWarnings("unused")
	private float displayWidth;
	
	public FirstLevel(TowerDefenceActivity tda) {
		this.tda = tda;
		
		setBackground(new Background(Color.GREEN));
		displayHeight = tda.getDisplayHeight();
		displayWidth = tda.getDisplayWidth();
		
		createLevel();
		loadResources(tda);
	}
	
	public void createLevel(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("sprites/");
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(	tda.getTextureManager(), 32, 32,
																		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		tda.getEngine().getTextureManager().loadTexture(bitmapTextureAtlas);
	}

	
	public void loadResources(BaseGameActivity baseGameActivity) {
		fontTexture = new BitmapTextureAtlas(	baseGameActivity.getTextureManager(), 256,	256,
												TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		exitFont = FontFactory.createFromAsset(	baseGameActivity.getFontManager(),
												fontTexture, baseGameActivity.getAssets(), "Sabatica-regular.ttf", 48,
												true, Color.GREEN.getABGRPackedInt());
		
		baseGameActivity.getEngine().getTextureManager().loadTexture(fontTexture);
		baseGameActivity.getFontManager().loadFont(exitFont);
		
		
		/*
		 * Create picture
		 */
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(	tda.getTextureManager(), 32, 32,
																		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		ITextureRegion faceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, tda, "brown.png", 0, 0);
		tda.getEngine().getTextureManager().loadTexture(bitmapTextureAtlas);
		

		/* Create the road and add it to the scene. */
		
		List<Sprite> topRoad = new ArrayList<Sprite>();
		int indent = 48;
		
		//Adds the rop-most road
		for(int index = 0; index < 7; index++){
			topRoad.add(new Sprite(indent, indent, faceTextureRegion, baseGameActivity.getVertexBufferObjectManager()));
			indent += faceTextureRegion.getWidth() * 4;
			
			Sprite tempTopRoad = topRoad.get(index);
			
			tempTopRoad.setScale(4f);
			attachChild(tempTopRoad);
		}
		
		
		List<Sprite> bottomRoad = new ArrayList<Sprite>();
		indent = 48;
		int temp = 0;
		
		//Adds the bottom-most road
		for(int index = 0; index < 7; index++){
			bottomRoad.add(new Sprite(indent, displayHeight-80, faceTextureRegion, baseGameActivity.getVertexBufferObjectManager()));
			indent += faceTextureRegion.getWidth() * 4;
			
			Sprite tempBottomRoad = bottomRoad.get(index);
			tempBottomRoad.setScale(4f);
			
			attachChild(tempBottomRoad);
			if(index == 4) {
				temp=indent;
			}
		}
		
		//Adds road to the right
		List<Sprite> roadRight = new ArrayList<Sprite>();
		
		float offset = faceTextureRegion.getWidth() * 4;
		
		roadRight.add(new Sprite(temp, displayHeight -(80+offset), faceTextureRegion, baseGameActivity.getVertexBufferObjectManager()));
		roadRight.get(0).setScale(4f);
		this.attachChild(roadRight.get(0));
		
		roadRight.add(new Sprite(temp+(faceTextureRegion.getWidth()*4), displayHeight -(80+offset), faceTextureRegion, baseGameActivity.getVertexBufferObjectManager()));
		roadRight.get(1).setScale(4f);
		this.attachChild(roadRight.get(1));
		
		offset = offset * 2;
		roadRight.add(new Sprite(temp, displayHeight -(80+offset), faceTextureRegion, baseGameActivity.getVertexBufferObjectManager()));
		roadRight.get(2).setScale(4f);
		this.attachChild(roadRight.get(2));
		
		
		roadRight.add(new Sprite(temp+(faceTextureRegion.getWidth()*4), displayHeight -(80+offset), faceTextureRegion, baseGameActivity.getVertexBufferObjectManager()));
		roadRight.get(3).setScale(4f);
		this.attachChild(roadRight.get(3));		
		
	}
}
