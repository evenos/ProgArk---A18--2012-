package no.progark.a18.towerdefence.level;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.util.Log;

import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.gameContent.Cell;
import no.progark.a18.towerdefence.gameContent.Direction;
import no.progark.a18.towerdefence.gameContent.Creep;
import no.progark.a18.towerdefence.gameContent.TouchListener;
import no.progark.a18.towerdefence.gameContent.Tower;
import no.progark.a18.towerdefence.gameContent.Wave;

/**
 * A static scene mainly for testing purposes.
 */
public class StaticLevel1 extends TowerDefenceScene{
	private final static String TAG = StaticLevel1.class.getName();
	private Tower towerToAdd;
	
	private Tower[][] towers;
	private Sprite menuTower;

	private ITextureRegion creepTextureregion;
	private ITextureRegion towerTextureregion;
	private ITextureRegion brownTextureRegion;
	private ITextureRegion greenTextureRegion;
	
	private Creep creep;


	/**
	 * A static scene mainly for testing purposes.
	 * 
	 * @param tda the games {@linkplain BaseGameActivity}
	 */
	StaticLevel1(TowerDefenceActivity tda) {
		super(tda);

		backgroundTiles = new Cell[numRows][numColls];
		towers = new Tower[numRows][numColls];
		setBackground(new Background(Color.RED));
		addBackgCells();
		
		ArrayList<Creep> creeps = new ArrayList<Creep>();
		ArrayList<Float> spawnDelay = new ArrayList<Float>();
		
		for(int i = 0; i < 10; i++){
			creeps.add(createCreep());
			spawnDelay.add(1000f);
		}
		
		waves = new ArrayList<Wave>();
		waves.add(new Wave(creeps, spawnDelay, this));
		waves.get(0).registerWaveFinishedListener(this);
		
		addMenue();
		addTower();
		
		attachChild(waves.get(currentWave));
	}

	private void addMenue() {
		exitToMain.setX(screenWidth - exitToMain.getWidth());
		exitToMain.setY(screenHeight -exitToMain.getHeight() + 5);
		registerTouchArea(exitToMain);
		attachChild(exitToMain);
		
		final float spacing = screenWidth / 3.5f;
		life.setX(0);
		life.setY(screenHeight -life.getHeight() + 5);
		
		money.setX(life.getX() + spacing);
		money.setY(screenHeight -money.getHeight() + 5);
		
		score.setX(money.getX() + spacing);
		score.setY(screenHeight -score.getHeight() + 5);
		
		updateLife();
		updateGold();
		updateScore();
		
		attachChild(money);
		attachChild(life);
		attachChild(score);
		
		menuTower = new Sprite(screenWidth - 2*textureSize, textureSize, textureSize, textureSize, towerTextureregion, towerDefenceActivity.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				towerToAdd = new Tower(0, 0, textureSize, textureSize, towerTextureregion, getVertexBufferObjectManager(), backgroundTiles, towerDefenceActivity);
				return true;
			}
		};

		menuTower.setScale(menuScale);
		registerTouchArea(menuTower);
		attachChild(menuTower);
	}

	private void addTower() {
		Tower tower = new Tower(numColls-2, 1, textureSize, textureSize, towerTextureregion, towerDefenceActivity.getVertexBufferObjectManager(), backgroundTiles, towerDefenceActivity);
		addTower(numColls-2, 1, tower);
	}

	private void addBackgCells() {
		// Add brown columns(midle)
		boolean dir = true;
		for (int y = 0; y < numRows; y += 2) {
			for(int x = 1; x < numColls -1; x++){
				backgroundTiles[y][x] = new Cell(boardScale*x*textureSize, boardScale*y*textureSize, textureSize, textureSize, x, y, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
				backgroundTiles[y][x].setScale(boardScale);
				backgroundTiles[y][x].setDirectionToNextRoad(dir?Direction.LEFT : Direction.RIGHT);
			
				attachChild(backgroundTiles[y][x]);
			}
			dir = !dir;
		}
		// Add brown columns(corners) 
		dir = true;
		for (int y = 0; y < numRows; y += 2) {
			int x = backgroundTiles[y].length-1;
			backgroundTiles[y][0] = new Cell(boardScale*0*textureSize, boardScale*y*textureSize, textureSize, textureSize, x, y, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
			backgroundTiles[y][0].setScale(boardScale);
			backgroundTiles[y][0].setDirectionToNextRoad(dir? Direction.DOWN : Direction.RIGHT);
			backgroundTiles[y][x] = new Cell(boardScale*x*textureSize, boardScale*y*textureSize, textureSize, textureSize, x, y,  brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
			backgroundTiles[y][x].setScale(boardScale);
			backgroundTiles[y][x].setDirectionToNextRoad(dir? Direction.LEFT : Direction.DOWN);
			
			attachChild(backgroundTiles[y][0]);
			attachChild(backgroundTiles[y][x]);
			dir = !dir;
		}
		// Add brown center row
		dir = true;
		for (int y = 1; y < numRows; y += 2) {
			int x = dir ? 0 : backgroundTiles[y].length-1;
			backgroundTiles[y][x] = new Cell(boardScale*x*textureSize, boardScale*y*textureSize, textureSize, textureSize, x, y, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
			backgroundTiles[y][x].setScale(boardScale);
			backgroundTiles[y][x].setDirectionToNextRoad(Direction.DOWN);
			
			attachChild(backgroundTiles[y][x]);
			dir = !dir;
		}

		// Add green to the rest of the board
		for (int y = 0; y < numRows; y++) {
			for (int x = 0; x < numColls; x++) {
				final int posY = y;
				final int posX = x;
				if (backgroundTiles[y][x] == null) {
					backgroundTiles[y][x] = new Cell(boardScale * textureSize * x, boardScale * textureSize
							* y, textureSize, textureSize, x, y, greenTextureRegion,
							towerDefenceActivity.getVertexBufferObjectManager(), false);
					backgroundTiles[y][x].setScale(boardScale);
					backgroundTiles[y][x].setTouchListener(new TouchListener() {
						
						public boolean handleTouch(IEntity entity) {
							if(towerToAdd == null || towers[posY][posX] != null)
								return false;
							towerToAdd.setPosition(backgroundTiles[posY][posX]);
							towerToAdd.setGridPosX(posX);
							towerToAdd.setGridPosY(posY);
							towers[posY][posX] = towerToAdd;
							attachChild(towerToAdd);
							towerToAdd = null;
							return true;
						}
					});
					registerTouchArea(backgroundTiles[y][x]);
					attachChild(backgroundTiles[y][x]);
				}
			}
		}
		
		startCell = backgroundTiles[0][backgroundTiles[0].length-1];
		goalCell = backgroundTiles[backgroundTiles.length-1][backgroundTiles[0].length-1];
		goalCell.registerCreepEnteredCellListener(this);
		
	}

	private Creep createCreep() {
		creep = new Creep(0f, 0f, textureSize, textureSize, creepTextureregion,
				towerDefenceActivity.getVertexBufferObjectManager(), this);
		creep.setSpeed(-300f, 0f);
		creep.setScale(boardScale - 0.1f);
		return creep;
	}


	

	
	@Override
	public void loadResourses() {
		// Load font texture


		// load creep texture
		creepTextureregion = loadTexture("gfx/", "creep2.png", 32, 32);
		
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
				towerDefenceActivity.getTextureManager(), x, y,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		ITextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(brownTexture, towerDefenceActivity, fileName, 0, 0);

		towerDefenceActivity.getEngine().getTextureManager().loadTexture(brownTexture);

		Log.d(TAG, "Loaded " + fileName + " texture");

		return textureRegion;
	}

	
	public boolean addTower(int x, int y, Tower tower){
		if(backgroundTiles[y][x].isRoad() || towers[y][x] != null)
			return false;
		
		towers[y][x] = tower;
		tower.setX(x*boardScale*32);
		tower.setY(y*boardScale*32);
		attachChild(tower);
		return true;
	}
	
	public boolean removeTower(int x, int y){
		Tower tower = towers[y][x];
		boolean datatched = detachChild(tower);

		return datatched;
	}

	public void loose() {
		// TODO:handle loose
		finished();
	}
	
	public void winn(){
		// TODO: handle winn
		finished();
	}

	
}
