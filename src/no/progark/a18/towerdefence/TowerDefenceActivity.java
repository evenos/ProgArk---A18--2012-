package no.progark.a18.towerdefence;

import java.util.LinkedList;
import java.util.List;

import no.progark.a18.towerdefence.logic.MainMenu;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.util.Log;
import android.view.Display;

public class TowerDefenceActivity extends SimpleBaseGameActivity {	
	/** Debug tag */
	private final static String TAG = "no.ProgArk.A18.TowerDefence";
	
	/**The height of the devise display*/
	private float displayHeight;
	public float getDisplayHeight() {
		return displayHeight;
	}


	public float getDisplayWidth() {
		return displayWidth;
	}

	/**The width of the devise display*/
	private float displayWidth;
	/**The stack of states that manages the states of the game*/
	private List<Scene> states = new LinkedList<Scene>();
	
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
	}

	@Override
	protected Scene onCreateScene() {
		getEngine().registerUpdateHandler(new FPSLogger());

		final Scene menu = new MainMenu(this);

		states.add(menu);
		
		return menu;
	}
	
	public void pushState(Scene state){
		if(states != null){
			states.add(state);
			getEngine().setScene(state);
		}
	}
	
	public Scene popState(){
		Scene scene = states.isEmpty() ? null : states.remove(0);
		
		if(states != null)
			getEngine().setScene(scene);
		
		return scene;
	}
	
	public Scene peekState(){
		return states.isEmpty() ? null : states.get(states.size()-1);
	}

}