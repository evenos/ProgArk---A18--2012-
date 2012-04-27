package no.progark.a18.towerdefence.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.gameContent.Cell;
import no.progark.a18.towerdefence.gameContent.Creep;
import no.progark.a18.towerdefence.gameContent.Direction;
import no.progark.a18.towerdefence.gameContent.TouchListener;
import no.progark.a18.towerdefence.gameContent.Tower;
import no.progark.a18.towerdefence.gameContent.TowerDefenceSprite;
import no.progark.a18.towerdefence.gameContent.Wave;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

import android.util.Log;

public class Level extends TowerDefenceScene{
	
	private final static String tag = Level.class.getName();
	
	private int numRows, numColumns;
	
	private Tower[][] towers;
	private Sprite menuTower;
	private Tower towerToAdd;

	private Font exitFont;
	private BitmapTextureAtlas fontTexture;

	private ITextureRegion creepTextureRegion;
	private ITextureRegion towerTextureRegion;
	private ITextureRegion brownTextureRegion;
	private ITextureRegion greenTextureRegion;

	private Creep creep;
	
	private Direction startDirection = null;
	
	private Scanner scanner;
	
	public Level(TowerDefenceActivity TowerDefenceActivity, String id) {
		super(TowerDefenceActivity);
		
		
		try {
			scanner = new Scanner(towerDefenceActivity.getResources().getAssets().open(id));
		} catch (IOException e) {
			Log.e(tag, "Scanner IOException", e);
		}
		
		loadProperties();		
		loadBoard();
		loadWawes();
		System.out.println("num waves "+waves.size());
		
		setBackground(new Background(Color.RED));
		
		addMenue();
		
		if(waves != null || waves.size() > 0)
			attachChild(waves.get(0));
	}

	private void loadWawes() {
		final int numberOfWaves = scanner.nextInt();
		waves = new ArrayList<Wave>();
		
		for(int numWave = 0; numWave < numberOfWaves; numWave++){
			ArrayList<Creep> creeps = new ArrayList<Creep>();
			ArrayList<Float> spawnDelay = new ArrayList<Float>();
			
			String[] stringWaves = scanner.next().trim().split(",");
			for(String s : stringWaves)
				if("creep".equals(s))
					creeps.add(createCreep());
			String[] stringDelays = scanner.next().trim().split(",");
			for(String s : stringDelays)
				spawnDelay.add(Float.valueOf(s));
			
			
			Wave wave = new Wave(creeps, spawnDelay, this);
			wave.registerWaveFinishedListener(this);
			waves.add(wave);
		}
		//TODO: add the waves
	}
	
	private Creep createCreep() {
		creep = new Creep(0f, 0f, textureSize, textureSize, creepTextureRegion,
				towerDefenceActivity.getVertexBufferObjectManager(), this);
		creep.setSpeed(-300f, 0f);
		creep.setScale(boardScale - 0.1f);
		return creep;
	}

	private void loadBoard() {
		backgroundTiles = new Cell[numRows][numColumns];
		towers = new Tower[numRows][numColumns];
		
		screenWidth = towerDefenceActivity.getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = towerDefenceActivity.getWindowManager().getDefaultDisplay().getHeight();
		
		for(int rowIndex = 0; rowIndex < numRows; rowIndex++) {
			
			String line = scanner.next().trim();
			
			String[] row = line.split(",");
			
			
			for(int colIndex = 0; colIndex < row.length; colIndex++) {
				
				Cell cell = null;
				
				switch(row[colIndex].charAt(0)) {
					case('u'): {
						cell = new Cell(boardScale * colIndex * 32, boardScale * rowIndex * 32, textureSize, textureSize, colIndex, rowIndex, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.UP);
						attachChild(cell);
						break;
					}
					case('r'): {
						cell = new Cell(boardScale * colIndex * 32, boardScale * rowIndex * 32, textureSize, textureSize, colIndex, rowIndex, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.RIGHT);
						attachChild(cell);
						break;
					}
					case('d'): {
						cell = new Cell(boardScale * colIndex * 32, boardScale * rowIndex * 32, textureSize, textureSize, colIndex, rowIndex, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.DOWN);
						attachChild(cell);
						break;
					}
					case('l'): {
						cell = new Cell(boardScale * colIndex * 32, boardScale * rowIndex * 32, textureSize, textureSize, colIndex, rowIndex, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.LEFT);
						attachChild(cell);
						break;
					}
					case('s'): {
						cell = new Cell(boardScale * colIndex * 32, boardScale * rowIndex * 32, textureSize, textureSize, colIndex, rowIndex, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(startDirection);
						startCell = cell;
						attachChild(cell);
						break;
					}
					case('g'): {
						cell = new Cell(boardScale * colIndex * 32, boardScale * rowIndex * 32, textureSize, textureSize, colIndex, rowIndex, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.LEFT);
						attachChild(cell);
						break;
					}
					case('x'): {
						cell = new Cell(boardScale * colIndex * 32, boardScale * rowIndex * 32, textureSize, textureSize, colIndex, rowIndex, greenTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.LEFT);
						cell.setTouchListener(new CellTouchListener(colIndex, rowIndex));
						registerTouchArea(cell);
						attachChild(cell);
						break;
					}
					case('t'): {
						towers[rowIndex][colIndex] = new Tower(colIndex, rowIndex, 32f, 32f, towerTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), backgroundTiles, towerDefenceActivity);
						cell = new Cell(boardScale * colIndex * 32, boardScale * rowIndex * 32, textureSize, textureSize, colIndex, rowIndex, greenTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						towers[rowIndex][colIndex].setPosition(cell);
						attachChild(cell);
						attachChild(towers[rowIndex][colIndex]);
						break;
					}
				}
				cell.setScale(boardScale);
				backgroundTiles[rowIndex][colIndex] = cell;
			}
		}
	}

	private void loadProperties() {
		numColumns = scanner.nextInt();
		numRows = scanner.nextInt();
		startDirection = Direction.valueOf(scanner.next().trim());
	}
	
	public class CellTouchListener implements TouchListener {
		
		private final int posX;
		private final int posY;
		
		public CellTouchListener(int posX, int posY) {
			this.posX = posX;
			this.posY = posY;
		}

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
	}
	
	private void addMenue() {
		menuTower = new Sprite(screenWidth - 64, 32, 32, 32, towerTextureRegion, towerDefenceActivity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				towerToAdd = new Tower(0, 0, 32, 32, towerTextureRegion, getVertexBufferObjectManager(), backgroundTiles, towerDefenceActivity);
				Log.d(tag, "User selected a new tower");
				return true;
			}
		};

		menuTower.setScale(3f);
		registerTouchArea(menuTower);
		attachChild(menuTower);
	}

	public void reatchedtTargt(final TowerDefenceSprite sprite) {
		System.out.println("Weeeehoooooo");
		//TODO:
		towerDefenceActivity.runOnUpdateThread(new Runnable() {
			public void run() {
				detachChild(sprite);
			}
		});
	}
	
	public boolean addTower(int x, int y, Tower tower){
		if(backgroundTiles[y][x].isRoad() || towers[y][x] != null)
			return false;
		
		towers[y][x] = tower;
		tower.setX(x*boardScale*textureSize);
		tower.setY(y*boardScale*textureSize);
		attachChild(tower);
		return true;
	}
	
	public boolean removeTower(int x, int y){
		Tower tower = towers[y][x];
		boolean datatched = detachChild(tower);

		return datatched;
	}

	
	@Override
	public void loadResourses() {
		// Load font texture
		this.fontTexture = new BitmapTextureAtlas(towerDefenceActivity.getTextureManager(), 256,
				256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.exitFont = FontFactory.createFromAsset(towerDefenceActivity.getFontManager(),
				this.fontTexture, towerDefenceActivity.getAssets(), "Sabatica-regular.ttf", 48,
				true, Color.BLACK.getABGRPackedInt());
		towerDefenceActivity.getEngine().getTextureManager().loadTexture(this.fontTexture);
		towerDefenceActivity.getFontManager().loadFont(this.exitFont);

		// load creep texture
		creepTextureRegion = loadTexture("gfx/", "creep1.png", 32, 32);
		
		// load creep texture
		towerTextureRegion = loadTexture("gfx/", "tower1.png", 32, 32);

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

		Log.d(tag, "Loaded " + fileName + " texture");

		return textureRegion;
	}
	
	@Override
	public void winn() {
		// TODO Auto-generated method stub
		finished();
	}

	@Override
	public void loose() {
		// TODO Auto-generated method stub
		finished();		
	}



}
