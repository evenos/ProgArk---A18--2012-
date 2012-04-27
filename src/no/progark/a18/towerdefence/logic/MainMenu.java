package no.progark.a18.towerdefence.logic;

import java.util.ArrayList;
import java.util.List;

import no.progark.a18.towerdefence.R;
import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.level.Level;
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
	private final Background BACKGROUND = new Background(new Color(0.9f, 0.9f, 0.9f, 1f));
	private float screenWidth, screenHeight;

	private Font exitFont;
	private BitmapTextureAtlas fontTexture;

	private Text exit;
	private Text staticLevel;
	private Text towerDefenceText;
	private Text chooselevel;
	private List<Text> levelTexts = new ArrayList<Text>();

	public MainMenu(final TowerDefenceActivity tda) {
		super();
		this.TDA = tda;
		setBackground(BACKGROUND);
		screenWidth = TDA.getDisplayWidth();
		screenHeight = TDA.getDisplayHeight();
		
		LevelFactory.setTowerDefenceActivity(TDA);

		loadResourses(TDA);
		addText(TDA);
	}

	private void addText(final BaseGameActivity tda) {
		//Exit text
		exit = new Text(0, 0, this.exitFont, TDA.getResources().getString(
				R.string.exit), tda.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Log.d(TAG, "Exit button, finishing activity");
				TDA.finish();
				return true;
			}
		};
		exit.setPosition(screenWidth-exit.getWidth()-5, screenHeight-exit.getHeight());
		this.registerTouchArea(exit);
		this.attachChild(exit);
		
		//Game name
		towerDefenceText = new Text(0, 0, this.exitFont, TDA.getResources().getString(
				R.string.gameName), tda.getVertexBufferObjectManager());
		towerDefenceText.setScale(screenWidth*0.3f/towerDefenceText.getWidth());
		towerDefenceText.setPosition(screenWidth-(towerDefenceText.getX()+(towerDefenceText.getWidth()*towerDefenceText.getScaleX())), (screenHeight-towerDefenceText.getHeight())/2);
		towerDefenceText.setColor(Color.BLUE);
		attachChild(towerDefenceText);
		
		//Chose level:
		chooselevel = new Text(5, 5, this.exitFont, TDA.getResources().getString(
				R.string.chooseLevel), tda.getVertexBufferObjectManager());
		attachChild(chooselevel);
		
		//StaticLevel text
		staticLevel = new Text(5, chooselevel.getX()+chooselevel.getHeight()+20, this.exitFont, TDA.getResources().getString(
				R.string.staticLevel), tda.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Log.d(TAG, "Level1 button, pushing level1");
				TDA.pushState(LevelFactory.getLevel("static"));
				return true;
			}
		};
		levelTexts.add(staticLevel);
		this.registerTouchArea(staticLevel);
		this.attachChild(staticLevel);
		
		//Dynamic levels
		final String [] levels = TDA.getResources().getStringArray(R.array.levels);
		float height = staticLevel.getY()+staticLevel.getHeight();
		for(int level = 0; level < levels.length; level++){
			final int lvl = level;
			Text dynamicLevel = new Text(5, height, this.exitFont, TDA.getResources().getString(R.string.playLevel)+" "+(level+1), tda.getVertexBufferObjectManager()){
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					Log.d(TAG, "Level1 button, pushing level1");
					TDA.pushState(new Level(TDA, levels[lvl]));
					return true;
				}
			};
			height = dynamicLevel.getY() + dynamicLevel.getHeight();
			levelTexts.add(dynamicLevel);
			this.registerTouchArea(dynamicLevel);
			this.attachChild(dynamicLevel);
		}
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
