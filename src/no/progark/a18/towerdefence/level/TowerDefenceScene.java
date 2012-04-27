package no.progark.a18.towerdefence.level;

import no.progark.a18.towerdefence.R;
import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.gameContent.PlayerInfo;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.color.Color;

import android.util.Log;

public abstract class TowerDefenceScene extends Scene implements ReatchedTargetListener{
	protected final TowerDefenceActivity towerDefenceActivity;

	protected final static float bardRegionWidth = 0.8f;
	protected final static float menuRegionWidth = 1f-bardRegionWidth;
	
	protected float screenWidth;
	protected float screenHeight;
	
	protected float textureSize;

	protected int numRows, numColls;
	
	protected float boardScale;
	protected float menuScale;
	
	protected PlayerInfo playerInfo;
	
	private Font font;
	protected Text exitToMain, money, life, score;

	
	
	public TowerDefenceScene(TowerDefenceActivity TDA) {
		this.towerDefenceActivity = TDA;
		
		screenWidth = TDA.getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = TDA.getWindowManager().getDefaultDisplay().getHeight();
		
		textureSize = 32;
		
		numColls = 16;
		numRows = 11;
		
		boardScale= ((float)(screenWidth * bardRegionWidth)) / (numColls*textureSize); //Scale to fit board
		menuScale = ((float)(screenWidth * menuRegionWidth)) / (2f*textureSize); // Scale to add two buttons
		
		loadResourses();
		loadFonts();
		
		playerInfo = new PlayerInfo("easy", this);
		
		addText();
		
	}

	protected void loadFonts() {
		BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(towerDefenceActivity.getTextureManager(), 256,
				256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.font = FontFactory.createFromAsset(towerDefenceActivity.getFontManager(),
				fontTexture, towerDefenceActivity.getAssets(), "Sabatica-regular.ttf", 48,
				true, Color.BLACK.getABGRPackedInt());
		towerDefenceActivity.getEngine().getTextureManager().loadTexture(fontTexture);
		towerDefenceActivity.getFontManager().loadFont(this.font);
		
	}

	protected abstract void loadResourses();
	
	/**
	 * Updates the textfield for life amount
	 */
	public void updateLife(){
		money.setText("Life:"+playerInfo.getLife());
	}
	
	/**
	 * Updates the textfield for the gold field
	 */
	public void updateGold(){
		life.setText("Gold:"+playerInfo.getgold());
	}
	
	/**
	 * Updates the textfield for the score field
	 */
	public void updateScore(){
		score.setText("Score:"+playerInfo.getScore());
	}
	
	private void addText() {

		// Level 1 text
		exitToMain = new Text(200, 50, this.font, towerDefenceActivity.getResources()
				.getString(R.string.returnToMain),
				towerDefenceActivity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				towerDefenceActivity.popState();
				return true;
			}
		};
		

		//Adding the money text
		money = new Text(0, 50, this.font, "Gold:"+Integer.MIN_VALUE, towerDefenceActivity.getVertexBufferObjectManager());
		
		//Adding the life text
		life = new Text(0, 50, this.font, "Life:"+Integer.MIN_VALUE, towerDefenceActivity.getVertexBufferObjectManager());
		
		//Adding the life text
		score = new Text(0, 50, this.font, "Score:"+Integer.MIN_VALUE, towerDefenceActivity.getVertexBufferObjectManager());
	}

}
