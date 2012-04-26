package no.progark.a18.towerdefence.level;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.background.Background;
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
import no.progark.a18.towerdefence.gameContent.Cell;
import no.progark.a18.towerdefence.gameContent.Direction;
import no.progark.a18.towerdefence.gameContent.Creep;
import no.progark.a18.towerdefence.gameContent.KillListener;
import no.progark.a18.towerdefence.gameContent.Tower;
import no.progark.a18.towerdefence.gameContent.TowerdefenceSprite;

/**
 * A static scene mainly for testing purposes.
 */
class StaticLevel1 extends TowerDefenceScene  implements KillListener{
	private final static String TAG = StaticLevel1.class.getName();
	private final TowerDefenceActivity TDA;

	private float scale = 1.25f;
	
	private int numRows, numColls;
	
	private Cell startCell;
	private Cell[][] backgroundTiles;
	private Tower[][] towers;

	private Font exitFont;
	private BitmapTextureAtlas fontTexture;

	private ITextureRegion creepTextureregion;
	private ITextureRegion towerTextureregion;
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
		numColls = 16;
		numRows = 11;
		backgroundTiles = new Cell[numRows][numColls];
		towers = new Tower[numRows][numColls];
		
		loadResourses();

		setBackground(new Background(Color.RED));
		
		addBackgCells();
		addText();
		addTower();
		addCreeps();
	}

	private void addTower() {
		Tower tower = new Tower(numColls-2, 1, 32, 32, towerTextureregion, TDA.getVertexBufferObjectManager(), backgroundTiles);
		addTower(numColls-2, 1, tower);
	}

	private void addBackgCells() {
		// Add brown columns(midle) 
		boolean dir = true;
		for (int y = 0; y < numRows; y += 2) {
			for(int x = 1; x < numColls -1; x++){
				backgroundTiles[y][x] = new Cell(scale*x*32, scale*y*32, 32, 32, brownTextureRegion, TDA.getVertexBufferObjectManager(), true);
				backgroundTiles[y][x].setScale(scale);
				backgroundTiles[y][x].setDirToNextRoad(dir?Direction.left : Direction.right);
			
				attachChild(backgroundTiles[y][x]);
			}
			dir = !dir;
		}
		// Add brown columns(corners) 
		dir = true;
		for (int y = 0; y < numRows; y += 2) {
			int x = backgroundTiles[y].length-1;
			backgroundTiles[y][0] = new Cell(scale*0*32, scale*y*32, 32, 32, brownTextureRegion, TDA.getVertexBufferObjectManager(), true);
			backgroundTiles[y][0].setScale(scale);
			backgroundTiles[y][0].setDirToNextRoad(dir? Direction.down : Direction.right);
			backgroundTiles[y][x] = new Cell(scale*x*32, scale*y*32, 32, 32, brownTextureRegion, TDA.getVertexBufferObjectManager(), true);
			backgroundTiles[y][x].setScale(scale);
			backgroundTiles[y][x].setDirToNextRoad(dir? Direction.left : Direction.down);
			
			attachChild(backgroundTiles[y][0]);
			attachChild(backgroundTiles[y][x]);
			dir = !dir;
		}
		// Add brown center row
		dir = true;
		for (int y = 1; y < numRows; y += 2) {
			int x = dir ? 0 : backgroundTiles[y].length-1;
			backgroundTiles[y][x] = new Cell(scale*x*32, scale*y*32, 32, 32, brownTextureRegion, TDA.getVertexBufferObjectManager(), true);
			backgroundTiles[y][x].setScale(scale);
			backgroundTiles[y][x].setDirToNextRoad(Direction.down);
			
			attachChild(backgroundTiles[y][x]);
			dir = !dir;
		}

		// Add green to the rest of the board
		for (int x = 0; x < backgroundTiles.length; x++) {
			for (int y = 0; y < backgroundTiles[x].length; y++) {
				if (backgroundTiles[x][y] == null) {
					backgroundTiles[x][y] = new Cell(scale * 32 * y, scale * 32
							* x, 32f, 32f, greenTextureRegion,
							TDA.getVertexBufferObjectManager(), false);
					backgroundTiles[x][y].setScale(scale);
					attachChild(backgroundTiles[x][y]);
				}
			}
		}
		
		startCell = backgroundTiles[0][backgroundTiles[0].length-1];
		
	}

	private void addCreeps() {
		creep = new Creep(scale* (numColls-1) * 32, 0f, 32f, 32f, creepTextureregion,
				TDA.getVertexBufferObjectManager(), this, this);
		creep.setSpeed(-300f, 0f);
		creep.setScale(scale - 0.1f);
		creep.registerUpdateHandler(new PathFinder(backgroundTiles[0].length-1, 0, creep));
		startCell.addCreep(creep);
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

	public void loadResourses() {
		// Load font texture
		this.fontTexture = new BitmapTextureAtlas(TDA.getTextureManager(), 256,
				256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.exitFont = FontFactory.createFromAsset(TDA.getFontManager(),
				this.fontTexture, TDA.getAssets(), "Sabatica-regular.ttf", 48,
				true, Color.BLACK.getABGRPackedInt());
		TDA.getEngine().getTextureManager().loadTexture(this.fontTexture);
		TDA.getFontManager().loadFont(this.exitFont);

		// load creep texture
		creepTextureregion = loadTexture("gfx/", "creep1.png", 32, 32);
		
		// load creep texture
		towerTextureregion = loadTexture("gfx/", "tower1.png", 32, 32);

		// load brown background images
		brownTextureRegion = loadTexture("sprites/", "brown.png", 32, 32);

		// load green background image
		greenTextureRegion = loadTexture("sprites/", "green.png", 32, 32);

	}

	private ITextureRegion loadTexture(String path, String fileName, int x,
			int y) {
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
		//TODO:
	}
	
	public boolean addTower(int x, int y, Tower tower){
		if(backgroundTiles[y][x].isRoad() || towers[y][x] != null)
			return false;
		
		towers[y][x] = tower;
		tower.setX(x*scale*32);
		tower.setY(y*scale*32);
		attachChild(tower);
		return true;
	}
	
	public boolean removeTower(int x, int y){
		Tower tower = towers[y][x];
		boolean datatched = detachChild(tower);

		return datatched;
	}
	
	private class PathFinder implements IUpdateHandler{
		private Creep creep;
		private int posX, posY;
		
		public PathFinder(int posX, int posY, Creep creep){
			this.creep = creep;
			this.posX = posX;
			this.posY = posY;
		}

		public void onUpdate(float pSecondsElapsed) {
			switch(backgroundTiles[posY][posX].getDirToNextRoad()){
			case left :
				float boundryLeft = (posX-1) * scale * 32;
				if(creep.getX() < boundryLeft){
					backgroundTiles[posY][posX--].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirToNextRoad());
				}
				break;
			case down :
				float boundryDown = (posY+1) * scale * 32;
				if(creep.getY() > boundryDown){
					backgroundTiles[posY++][posX].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirToNextRoad());
				}
				break;
			case right :
				float boundryRight = (posX+1) * scale * 32;
				if(creep.getX() > boundryRight){
					backgroundTiles[posY][posX++].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirToNextRoad());
				}
				break;
			case up :
				float boundryUp = (posY-1) * scale * 32 - 16;
				if(creep.getY() > boundryUp){
					backgroundTiles[posY--][posX].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirToNextRoad());
				}
				break;
			default:
			}			
		}

		private void changeDir(Direction dirToNextRoad) {
			float speed = Math.max(Math.abs(creep.getSpeedX()), Math.abs(creep.getSpeedY()));
			switch(dirToNextRoad){
			case left :
				creep.setSpeed(-speed, 0);
				break;
			case down :
				creep.setSpeed(0, speed);
				break;
			case right :
				creep.setSpeed(speed, 0);
				break;
			case up :
				creep.setSpeed(0, -speed);
				break;
			default:
			}
		}

		public void reset() { }
	}

	public void wasKilled(final Creep creep) {
		for(Cell[] row : backgroundTiles)
			for(Cell cell : row)
				if(cell.containsCreep(creep))
					cell.removeCreep(creep);
		
		TDA.runOnUpdateThread(new Runnable() {
			public void run() {
				detachChild(creep);
			}
		});
		
		Log.d(TAG, "Creep detaqtched");
	}
}
