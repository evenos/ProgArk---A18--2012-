package no.progark.a18.towerdefence.level;

import java.io.IOException;
import java.util.Scanner;

import no.progark.a18.towerdefence.R;
import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.gameContent.Cell;
import no.progark.a18.towerdefence.gameContent.Creep;
import no.progark.a18.towerdefence.gameContent.Direction;
import no.progark.a18.towerdefence.gameContent.KillListener;
import no.progark.a18.towerdefence.gameContent.Tower;
import no.progark.a18.towerdefence.gameContent.TowerDefenceSprite;

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
		addCreeps();
	}

	public void reatchedtTargt(TowerDefenceSprite sprite) {
		// TODO Auto-generated method stub
	}

	public void wasKilled(Creep creep) {
		// TODO Auto-generated method stub
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
		this.registerTouchArea(exitToMain);
		this.attachChild(exitToMain);
	}
}
