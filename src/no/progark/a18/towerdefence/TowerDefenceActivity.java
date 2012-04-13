package no.progark.a18.towerdefence;

import java.util.LinkedList;
import java.util.List;

import no.progark.a18.towerdefence.logic.MainMenu;
import no.progark.a18.towerdefence.logic.State;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.util.Log;
import android.view.Display;

public class TowerDefenceActivity extends SimpleBaseGameActivity {
	
	/*
	 * DETTE ER EN TEST, HILSEN NJAAL
	 */
	
	/** Debug tag */
	private final static String TAG = "no.ProgArk.A18.TowerDefence";
	
	/**The height of the devise display*/
	private float displayHeight;
	/**The width of the devise display*/
	private float displayWidth;
	/**Texture region for smily face*/
	private ITextureRegion faceTextureRegion;
	/**The stack of states that manages the states of the game*/
	private List<State> states = new LinkedList<State>();
	
	//liten kommentar

	public EngineOptions onCreateEngineOptions() {
		final Display mDisplay = getWindowManager().getDefaultDisplay();
		displayHeight = mDisplay.getHeight();
		displayWidth = mDisplay.getWidth();

		final Camera camera = new Camera(0, 0, displayWidth, displayHeight);

		Log.d(TAG, "New camera dim[w:" + displayWidth + 
				" h:" + displayHeight + "]");
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(displayWidth, displayHeight), camera);
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("font/");
		
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(
				getTextureManager(), 32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		faceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bitmapTextureAtlas, this, "face_box.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(bitmapTextureAtlas);
	}

	@Override
	protected Scene onCreateScene() {
		getEngine().registerUpdateHandler(new FPSLogger());

		final no.progark.a18.towerdefence.logic.Scene menu = new MainMenu(this);

		/*
		 * Calculate the coordinates for the face, so its centered on the
		 * camera.
		 */
		final float centerX = (displayWidth - faceTextureRegion.getWidth()) / 2;
		final float centerY = (displayHeight - faceTextureRegion.getHeight()) / 2;

		/* Create the face and add it to the scene. */
		final Sprite face = new Sprite(centerX, centerY, faceTextureRegion,
				getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
						pSceneTouchEvent.getY() - this.getHeight() / 2);
				return true;
			}
		};
		face.setScale(4f);
		menu.attachChild(face);
		menu.registerTouchArea(face);

		states.add(menu);
		
		return menu;
	}
	
	public void pushState(State state){
		if(states != null){
			states.add(state);
			getEngine().setScene(state);
		}
	}
	
	public State popState(){
		State scene = states.isEmpty() ? null : states.remove(0);
		
		if(states != null)
			getEngine().setScene(scene);
		
		return scene;
	}
	
	public State peekState(){
		return states.isEmpty() ? null : states.get(0);
	}

}