package no.progark.a18.towerdefence.logic;

import no.progark.a18.towerdefence.R;
import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.level.LevelFactory;

import org.andengine.entity.scene.Scene;
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

/**
 * The main menu of the game.
 */
public class MainMenu extends Scene {
	private final static String TAG = MainMenu.class.getName();
	private final TowerDefenceActivity TDA;
	private final Background BACKGROUND = new Background(Color.WHITE);

	private Font exitFont;
	private BitmapTextureAtlas fontTexture;

	private Text exit;
	private Text level1, level2;	

	public MainMenu(final TowerDefenceActivity tda) {
		super();
		this.TDA = tda;
		setBackground(BACKGROUND);
		
		LevelFactory.setTowerDefenceActivity(TDA);

		loadResourses(TDA);
		addText(TDA);
	}

	private void addText(final BaseGameActivity tda) {
		//Exit text
		exit = new Text(50, 50, this.exitFont, TDA.getResources().getString(
				R.string.exit), tda.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Log.d(TAG, "Exit button, finishing activity");
				TDA.finish();
				return true;
			}
		};
		this.registerTouchArea(exit);
		this.attachChild(exit);
		
		//Level 1 text
		level1 = new Text(50, 200, this.exitFont, TDA.getResources().getString(
				R.string.playLevelOne), tda.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Log.d(TAG, "Level1 button, pushing level1");
				TDA.pushState(LevelFactory.getLevel("1"));
				return true;
			}
		};
		this.registerTouchArea(level1);
		this.attachChild(level1);
		
		//Level 2 text
		level2 = new Text(50, 250, this.exitFont, TDA.getResources().getString(R.string.PlayLevelTwo), tda.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Log.d(TAG, "Level1 button, pushing level1");
				TDA.pushState(LevelFactory.getLevel("2"));
				return true;
			}
		};
		this.registerTouchArea(level2);
		this.attachChild(level2);
	}
			
	public void loadResourses(final BaseGameActivity tda) {
		this.fontTexture = new BitmapTextureAtlas(tda.getTextureManager(), 256,
				256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.exitFont = FontFactory.createFromAsset(tda.getFontManager(),
				this.fontTexture, tda.getAssets(), "Sabatica-regular.ttf", 48,
				true, Color.BLACK.getABGRPackedInt());
		tda.getEngine().getTextureManager().loadTexture(this.fontTexture);
		tda.getFontManager().loadFont(this.exitFont);

	}

}
