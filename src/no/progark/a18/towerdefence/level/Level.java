package no.progark.a18.towerdefence.level;

import java.io.IOException;
import java.util.Scanner;

import no.progark.a18.towerdefence.R;
import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.gameContent.Cell;
import no.progark.a18.towerdefence.gameContent.Creep;
import no.progark.a18.towerdefence.gameContent.Direction;
import no.progark.a18.towerdefence.gameContent.KillListener;
import no.progark.a18.towerdefence.gameContent.TouchListener;
import no.progark.a18.towerdefence.gameContent.Tower;
import no.progark.a18.towerdefence.gameContent.TowerDefenceSprite;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

import android.util.Log;

public class Level extends TowerDefenceScene implements KillListener{
	
	private final static String tag = Level.class.getName();
	private final TowerDefenceActivity towerDefenceActivity;

	private float scale = 1.25f;
	
	private int numRows, numColumns;
	
	private Cell startCell;
	private Cell goalCell;
	private Cell[][] backgroundTiles;
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
	private Text exitToMain;
	
	private Direction startDirection = null;
	
	private Scanner scanner;
	
	private float screenWidth;
	private float screenHeight;
	
	Level(TowerDefenceActivity TowerDefenceActivity, String id) {
		super();
		this.towerDefenceActivity = TowerDefenceActivity;
		loadResourses();
		
		
		try {
			scanner = new Scanner(towerDefenceActivity.getResources().getAssets().open(id + ".txt"));
		} catch (IOException e) {
			Log.e(tag, "scanner error", e);
		}
		
		numColumns = scanner.nextInt();
		numRows = scanner.nextInt();
		startDirection = Direction.valueOf(scanner.next().trim());
		
		backgroundTiles = new Cell[numRows][numColumns];
		towers = new Tower[numRows][numColumns];
		
		screenWidth = towerDefenceActivity.getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = towerDefenceActivity.getWindowManager().getDefaultDisplay().getHeight();
		
		System.out.println("num columns: " + numColumns);
		System.out.println("num rows: " + numRows);
		System.out.println("startDirection: " + startDirection);
		
		for(int rowIndex = 0; rowIndex < numRows; rowIndex++) {
			
			String line = scanner.next().trim();
			System.out.println("Line: " + line);
			
			String[] row = line.split(",");
			
			
			for(int colIndex = 0; colIndex < row.length; colIndex++) {
				
				Cell cell = null;
				
				switch(row[colIndex].charAt(0)) {
					case('u'): {
						cell = new Cell(scale * colIndex * 32, scale * rowIndex * 32, 32f, 32f, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.UP);
						attachChild(cell);
						break;
					}
					case('r'): {
						cell = new Cell(scale * colIndex * 32, scale * rowIndex * 32, 32f, 32f, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.RIGHT);
						attachChild(cell);
						break;
					}
					case('d'): {
						cell = new Cell(scale * colIndex * 32, scale * rowIndex * 32, 32f, 32f, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.DOWN);
						attachChild(cell);
						break;
					}
					case('l'): {
						cell = new Cell(scale * colIndex * 32, scale * rowIndex * 32, 32f, 32f, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.LEFT);
						attachChild(cell);
						break;
					}
					case('s'): {
						cell = new Cell(scale * colIndex * 32, scale * rowIndex * 32, 32f, 32f, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(startDirection);
						startCell = cell;
						attachChild(cell);
						break;
					}
					case('g'): {
						cell = new Cell(scale * colIndex * 32, scale * rowIndex * 32, 32f, 32f, brownTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.LEFT);
						attachChild(cell);
						break;
					}
					case('x'): {
						cell = new Cell(scale * colIndex * 32, scale * rowIndex * 32, 32f, 32f, greenTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						cell.setDirectionToNextRoad(Direction.LEFT);
						cell.setTouchListener(new CellTouchListener(colIndex, rowIndex));
						registerTouchArea(cell);
						attachChild(cell);
						break;
					}
					case('t'): {
						towers[rowIndex][colIndex] = new Tower(colIndex, rowIndex, 32f, 32f, towerTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), backgroundTiles);
						cell = new Cell(scale * colIndex * 32, scale * rowIndex * 32, 32f, 32f, greenTextureRegion, towerDefenceActivity.getVertexBufferObjectManager(), true);
						towers[rowIndex][colIndex].setPosition(cell);
						attachChild(cell);
						attachChild(towers[rowIndex][colIndex]);
						break;
					}
				}
				cell.setScale(scale);
				backgroundTiles[rowIndex][colIndex] = cell;
			}
		}
		setBackground(new Background(Color.RED));
		
		addText();
		addMenue();
		addCreeps();
	}
	
	public class CellTouchListener implements TouchListener {
		
		private final int posX;
		private final int posY;
		
		public CellTouchListener(int posX, int posY) {
			this.posX = posX;
			this.posY = posY;
		}

		public boolean handleTouch(IEntity entity) {
			Log.d(tag, "THEY TOUCHED ME!!!!");
			if(towerToAdd == null || towers[posY][posX] != null)
				return false;
			towerToAdd.setPosition(backgroundTiles[posY][posX]);
			towerToAdd.setGridPosX(posX);
			towerToAdd.setGridPosY(posY);
			towers[posY][posX] = towerToAdd;
			attachChild(towerToAdd);
			towerToAdd = null;
			Log.d(tag, "User placed a new tower at x:"+posX+" y:"+posY);
			return true;
		}
	}
	
	private void addMenue() {
		exitToMain.setX(screenWidth - exitToMain.getWidth());
		exitToMain.setY(screenHeight -exitToMain.getHeight() + 5);
		registerTouchArea(exitToMain);
		attachChild(exitToMain);
		
		menuTower = new Sprite(screenWidth - 64, 32, 32, 32, towerTextureRegion, towerDefenceActivity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				towerToAdd = new Tower(0, 0, 32, 32, towerTextureRegion, getVertexBufferObjectManager(), backgroundTiles);
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

	public void wasKilled(Creep creep) {
		removeCreep(creep);
	}

	private void removeCreep(final Creep creep) {
		for(Cell[] row : backgroundTiles)
			for(Cell cell : row)
				if(cell.containsCreep(creep))
					cell.removeCreep(creep);
		
		towerDefenceActivity.runOnUpdateThread(new Runnable() {
			public void run() {
				detachChild(creep);
			}
		});
		
		Log.d(tag, "Creep detaqtched");
	}
	
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
	
	private void addCreeps() {
		creep = new Creep(scale* (numColumns-1) * 32, 0f, 32f, 32f, creepTextureRegion,
				towerDefenceActivity.getVertexBufferObjectManager(), this, this);
		creep.setSpeed(-300f, 0f);
		creep.setScale(scale - 0.1f);
		creep.registerUpdateHandler(new PathFinder(backgroundTiles[0].length-1, 0, creep));
		startCell.addCreep(creep);
		attachChild(creep);
		Log.d(tag, "Added creep sprite");
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
			switch(backgroundTiles[posY][posX].getDirectionToNextRoad()){
			case LEFT :
				float boundryLeft = (posX-1) * scale * 32;
				if(creep.getX() < boundryLeft){
					backgroundTiles[posY][posX--].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirectionToNextRoad());
				}
				break;
			case DOWN :
				float boundryDown = (posY+1) * scale * 32;
				if(creep.getY() > boundryDown){
					backgroundTiles[posY++][posX].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirectionToNextRoad());
				}
				break;
			case RIGHT :
				float boundryRight = (posX+1) * scale * 32;
				if(creep.getX() > boundryRight){
					backgroundTiles[posY][posX++].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirectionToNextRoad());
				}
				break;
			case UP :
				float boundryUp = (posY-1) * scale * 32 - 16;
				if(creep.getY() < boundryUp){
					backgroundTiles[posY--][posX].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirectionToNextRoad());
				}
				break;
			default:
			}			
		}

		private void changeDir(Direction dirToNextRoad) {
			float speed = Math.max(Math.abs(creep.getSpeedX()), Math.abs(creep.getSpeedY()));
			switch(dirToNextRoad){
			case LEFT :
				creep.setSpeed(-speed, 0);
				break;
			case DOWN :
				creep.setSpeed(0, speed);
				break;
			case RIGHT :
				creep.setSpeed(speed, 0);
				break;
			case UP :
				creep.setSpeed(0, -speed);
				break;
			default:
			}
		}

		public void reset() { }
	}
	
	private void addText() {

		// Level 1 text
		exitToMain = new Text(200, 50, this.exitFont, towerDefenceActivity.getResources()
				.getString(R.string.returnToMain),
				towerDefenceActivity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Log.d(tag, "Exiting StaticLevel1");
				towerDefenceActivity.popState();
				return true;
			}
		};
	}
}
