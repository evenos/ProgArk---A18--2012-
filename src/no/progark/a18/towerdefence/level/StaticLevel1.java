package no.progark.a18.towerdefence.level;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.util.Log;

import no.progark.a18.towerdefence.R;
import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.gameContent.Creep;
import no.progark.a18.towerdefence.gameContent.TowerdefenceSprite;

/**
 * A static scene mainly for testing purposes.
 */
class StaticLevel1 extends TowerDefenceScene {
	private final static String TAG = StaticLevel1.class.getName();
	private final TowerDefenceActivity TDA;

	private int scale;
	
	private Sprite[][] backgroundTiles;

	private Font exitFont;
	private BitmapTextureAtlas fontTexture;
	
	private ITextureRegion creepTextureregion;
	private ITextureRegion brownTextureRegion;
	private ITextureRegion greenTextureRegion;
	
	private Creep creep;
	private Text exitToMain;



	/**
	 * A static scene mainly for testing purposes.
	 * 
	 * @param tda
	 *            the games {@linkplain BaseGameActivity}
	 */
	StaticLevel1(TowerDefenceActivity tda) {
		super();
		this.TDA = tda;
		loadResourses(TDA);

		setBackground(new Background(Color.RED));
		
		addBackground();
		addText();
		addCreeps();
	}

	private void addBackground() {
		backgroundTiles = new Sprite[7][15];
		
		//Add brown columns
		for(int x = 0; x < backgroundTiles.length; x += 2){
			for(int y = 0; y < backgroundTiles[x].length; y++){
				backgroundTiles[x][y] = new Sprite(32*y+16, 32*x+16, brownTextureRegion, TDA.getVertexBufferObjectManager());
				attachChild(backgroundTiles[x][y]);
			}
		}
		//Add brown rows
		for(int x = 1; x < backgroundTiles.length; x += 4){
			backgroundTiles[x][0] = new Sprite(16, 32*x+16, brownTextureRegion, TDA.getVertexBufferObjectManager());
			attachChild(backgroundTiles[x][0]);
		}
		for(int x = 3; x < backgroundTiles.length; x += 4){
			backgroundTiles[x][backgroundTiles[x].length-1] = new Sprite(32 * (backgroundTiles[x].length-1) +16, 32*x+16, brownTextureRegion, TDA.getVertexBufferObjectManager());
			attachChild(backgroundTiles[x][backgroundTiles[x].length-1]);
		}
		
		//Add green to the rest of the board
		for(int x = 0; x < backgroundTiles.length; x++){
			for(int y = 0; y < backgroundTiles[x].length; y++){
				if(backgroundTiles[x][y] == null){
					backgroundTiles[x][y] = new Sprite(32 * y +16, 32*x+16, greenTextureRegion, TDA.getVertexBufferObjectManager());
					attachChild(backgroundTiles[x][y]);
				}
			}
		}
		
	}

	private void addCreeps() {
		creep = new Creep(16f, 16f, 32f, 32f, creepTextureregion, TDA.getVertexBufferObjectManager(), this);
		creep.setSpeed(50f, 0f);
		attachChild(creep);
		Log.d(TAG, "Added creep sprite");
	}

	private void addText() {

		// Level 1 text
		exitToMain = new Text(200, 50, this.exitFont, TDA.getResources()
				.getString(R.string.returnToMain),
				TDA.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Log.d(TAG, "Exiting StaticLevel1");
				TDA.popState();
				return true;
			}
		};
		this.registerTouchArea(exitToMain);
		this.attachChild(exitToMain);
	}

	public void loadResourses(final BaseGameActivity tda) {
		// Load font texture
		this.fontTexture = new BitmapTextureAtlas(tda.getTextureManager(), 256,
				256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.exitFont = FontFactory.createFromAsset(tda.getFontManager(),
				this.fontTexture, tda.getAssets(), "Sabatica-regular.ttf", 48,
				true, Color.BLACK.getABGRPackedInt());
		tda.getEngine().getTextureManager().loadTexture(this.fontTexture);
		tda.getFontManager().loadFont(this.exitFont);
		
		// creep texture 				
		 creepTextureregion = loadTexture("gfx/", "creep1.png", 32, 32);
				
		//load brown background images
		brownTextureRegion = loadTexture("sprites/", "brown.png", 32, 32);
		
		//load green background image
		greenTextureRegion = loadTexture("sprites/", "green.png", 32, 32);
		
	}

	private ITextureRegion loadTexture(String path, String fileName, int x, int y) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(path);
		
		BitmapTextureAtlas brownTexture = new BitmapTextureAtlas(
				TDA.getTextureManager(), x, y,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		 ITextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(brownTexture, TDA, fileName, 0, 0);
		
		TDA.getEngine().getTextureManager().loadTexture(brownTexture);
		
		Log.d(TAG, "Loaded " + fileName + " texture");
		
		return textureRegion;
	}

	public void reatchedtTargt(TowerdefenceSprite sprite) {
		System.out.println("Weeeehoooooo");

	}

}
