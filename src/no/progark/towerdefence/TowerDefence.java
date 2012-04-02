package no.progark.towerdefence;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.ui.activity.BaseGameActivity;

//import android.os.Bundle;

public class TowerDefence extends BaseGameActivity {
	private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 480;
    
    private Camera mCamera;
    
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//    }

	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
        return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, 
        		new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	public void onLoadResources() {
		//Left empty
	}

	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		 
        final Scene scene = new Scene();
        scene.setBackground(new ColorBackground(0, 0, 0.8784f));
        
        return scene;
	}

	public void onLoadComplete() {
		//Left empty
	}
}