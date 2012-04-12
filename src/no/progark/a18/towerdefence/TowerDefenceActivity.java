package no.progark.a18.towerdefence;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

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
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(
				getTextureManager(), 32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		faceTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(bitmapTextureAtlas, this, "face_box.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(bitmapTextureAtlas);
	}

	@Override
	protected Scene onCreateScene() {
		getEngine().registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(Color.BLUE));

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
		scene.attachChild(face);
		scene.registerTouchArea(face);

		return scene;
	}

}