package no.progark.a18.towerdefence.level;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.util.Log;

import no.progark.a18.towerdefence.R;
import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.logic.Scene;

/**
 * A static scene mainly for testing purposes.
 */
class StaticLevel1 extends Scene {
	private final static String TAG = StaticLevel1.class.getName();

	private final TowerDefenceActivity TDA;
	private Font exitFont;
	private BitmapTextureAtlas fontTexture;

	private Text exitToMain;
	
	/**
	 * A static scene mainly for testing purposes.
	 *@param tda the games {@linkplain BaseGameActivity}
	 */
	StaticLevel1(TowerDefenceActivity tda){
		this.TDA = tda;
		setBackground(new Background(Color.RED));
		
		loadResourses(TDA);
		addText(TDA);
	}

	private void addText(final BaseGameActivity tda) {
		
		//Level 1 text
		exitToMain = new Text(200, 50, this.exitFont, TDA.getResources().getString(
				R.string.returnToMain), tda.getVertexBufferObjectManager()){
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
		this.fontTexture = new BitmapTextureAtlas(tda.getTextureManager(), 256,
				256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.exitFont = FontFactory.createFromAsset(tda.getFontManager(),
				this.fontTexture, tda.getAssets(), "Sabatica-regular.ttf", 48,
				true, Color.GREEN.getABGRPackedInt());
		tda.getEngine().getTextureManager().loadTexture(this.fontTexture);
		tda.getFontManager().loadFont(this.exitFont);

	}

}
