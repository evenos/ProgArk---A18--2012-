package no.progark.a18.towerdefence.level;

import java.util.ArrayList;
import java.util.List;

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
		setBackground(new Background(Color.GREEN));
		displayHeight = tda.getDisplayHeight();
		displayWidth = tda.getDisplayWidth();
		createLevel();
		loadResourses(tda);
		
		
	}
	
	void createLevel(){
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("sprites/");
		
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(
				TDA.getTextureManager(), 32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		
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
				.createFromAsset(bitmapTextureAtlas, TDA, "brown.png", 0, 0);
		
		TDA.getEngine().getTextureManager().loadTexture(bitmapTextureAtlas);
		
	

		/* Create the road and add it to the scene. */
		
		List<Sprite> vei = new ArrayList<Sprite>();
		List<Sprite> vei2 = new ArrayList<Sprite>();
		int innrykk =48;
		
		//Legger til den ¿verste raden med vei
		for(int i=0; i<7; i++){
			vei.add(new Sprite(innrykk, 48, faceTextureRegion,	tda.getVertexBufferObjectManager()) );
			innrykk+=faceTextureRegion.getWidth()*4;
			vei.get(i).setScale(4f);
			this.attachChild(vei.get(i));
		}
		
		innrykk=48;
		int temp=0;
		//Legger til den nederste raden med vei
		for(int i=0; i<7; i++){
			vei2.add(new Sprite(innrykk, displayHeight-80, faceTextureRegion,	tda.getVertexBufferObjectManager()) );
			innrykk+=faceTextureRegion.getWidth()*4;
			vei2.get(i).setScale(4f);
			this.attachChild(vei2.get(i));
			if(i==4)
				temp=innrykk;
		}
		
		//Legger til vei pŒ h¿yre siden
		List<Sprite> vei3 = new ArrayList<Sprite>();
		
		float temp2=faceTextureRegion.getWidth()*4;
		
		vei3.add(new Sprite(temp, displayHeight -(80+temp2), faceTextureRegion, tda.getVertexBufferObjectManager()));
		vei3.get(0).setScale(4f);
		this.attachChild(vei3.get(0));
		
		vei3.add(new Sprite(temp+(faceTextureRegion.getWidth()*4), displayHeight -(80+temp2), faceTextureRegion, tda.getVertexBufferObjectManager()));
		vei3.get(1).setScale(4f);
		this.attachChild(vei3.get(1));
		
		temp2=temp2*2;
		vei3.add(new Sprite(temp, displayHeight -(80+temp2), faceTextureRegion, tda.getVertexBufferObjectManager()));
		vei3.get(2).setScale(4f);
		this.attachChild(vei3.get(2));
		
		
		vei3.add(new Sprite(temp+(faceTextureRegion.getWidth()*4), displayHeight -(80+temp2), faceTextureRegion, tda.getVertexBufferObjectManager()));
		vei3.get(3).setScale(4f);
		this.attachChild(vei3.get(3));
		
		
		
	}
	
	

}
