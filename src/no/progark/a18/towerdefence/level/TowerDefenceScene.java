package no.progark.a18.towerdefence.level;

import java.util.List;

import no.progark.a18.towerdefence.R;
import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.gameContent.Cell;
import no.progark.a18.towerdefence.gameContent.Creep;
import no.progark.a18.towerdefence.gameContent.Wave;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.color.Color;

public abstract class TowerDefenceScene extends Scene implements WaveFinishedListener, CreepKilledListener, CreepEnteredCellListener{
	protected final TowerDefenceActivity towerDefenceActivity;

	protected final static float bardRegionWidth = 0.8f;
	protected final static float menuRegionWidth = 1f-bardRegionWidth;
	
	protected float screenWidth;
	protected float screenHeight;
	
	protected float textureSize;

	protected int numRows, numColls;
	
	protected float boardScale;
	protected float menuScale;
	
	protected PlayerInfo playerInfo;
	
	private Font font;
	protected Text exitToMain, money, life, score;
	
	protected Cell startCell;
	protected Cell goalCell;
	protected Cell[][] backgroundTiles;
	
	
	protected int currentWave = 0;
	protected List<Wave> waves;

	
	
	public TowerDefenceScene(TowerDefenceActivity TDA) {
		this.towerDefenceActivity = TDA;
		
		screenWidth = TDA.getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = TDA.getWindowManager().getDefaultDisplay().getHeight();
		
		textureSize = 32;
		
		numColls = 16;
		numRows = 11;
		
		boardScale= ((float)(screenWidth * bardRegionWidth)) / (numColls*textureSize); //Scale to fit board
		menuScale = ((float)(screenWidth * menuRegionWidth)) / (2f*textureSize); // Scale to add two buttons
		
		loadResourses();
		loadFonts();
		
		playerInfo = new PlayerInfo("easy", this);
		
		addText();
		
	}

	public void sendCreep(Creep creep){
		creep.registerUpdateHandler(new PathFinder(startCell.getGridPosX(), startCell.getGridPosY(), creep));
		creep.setScale(boardScale - 0.1f);
		creep.setPosition(startCell);
		startCell.addCreep(creep);
		attachChild(creep);
	}
	
	protected abstract void loadResourses();

	protected void loadFonts() {
		BitmapTextureAtlas fontTexture = new BitmapTextureAtlas(towerDefenceActivity.getTextureManager(), 256,
				256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.font = FontFactory.createFromAsset(towerDefenceActivity.getFontManager(),
				fontTexture, towerDefenceActivity.getAssets(), "Sabatica-regular.ttf", 48,
				true, Color.BLACK.getABGRPackedInt());
		towerDefenceActivity.getEngine().getTextureManager().loadTexture(fontTexture);
		towerDefenceActivity.getFontManager().loadFont(this.font);
		
	}

	/**
	 * Updates the textfield for life amount
	 */
	public void updateLife(){
		money.setText("Life:"+playerInfo.getLife());
	}
	
	/**
	 * Updates the textfield for the gold field
	 */
	public void updateGold(){
		life.setText("Gold:"+playerInfo.getgold());
	}
	
	/**
	 * Updates the textfield for the score field
	 */
	public void updateScore(){
		score.setText("Score:"+playerInfo.getScore());
	}
	
	private void addText() {

		// Level 1 text
		exitToMain = new Text(200, 50, this.font, towerDefenceActivity.getResources()
				.getString(R.string.returnToMain),
				towerDefenceActivity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				towerDefenceActivity.popState();
				return true;
			}
		};
		

		//Adding the money text
		money = new Text(0, 50, this.font, "Gold:"+Integer.MIN_VALUE, towerDefenceActivity.getVertexBufferObjectManager());
		
		//Adding the life text
		life = new Text(0, 50, this.font, "Life:"+Integer.MIN_VALUE, towerDefenceActivity.getVertexBufferObjectManager());
		
		//Adding the life text
		score = new Text(0, 50, this.font, "Score:"+Integer.MIN_VALUE, towerDefenceActivity.getVertexBufferObjectManager());
	}
	
	protected void finished(){
		towerDefenceActivity.popState();
	}
	
	protected class PathFinder implements IUpdateHandler{
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
				float boundryLeft = (posX-1) * boardScale * 32;
				if(creep.getX() < boundryLeft){
					backgroundTiles[posY][posX--].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirectionToNextRoad());
				}
				break;
			case DOWN :
				float boundryDown = (posY+1) * boardScale * 32;
				if(creep.getY() > boundryDown){
					backgroundTiles[posY++][posX].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirectionToNextRoad());
				}
				break;
			case RIGHT :
				float boundryRight = (posX+1) * boardScale * 32;
				if(creep.getX() > boundryRight){
					backgroundTiles[posY][posX++].removeCreep(creep);
					backgroundTiles[posY][posX].addCreep(creep);
					changeDir(backgroundTiles[posY][posX].getDirectionToNextRoad());
				}
				break;
			case UP :
				float boundryUp = (posY-1) * boardScale * 32 - 16;
				if(creep.getY() > boundryUp){
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
	public void removeCreep(final Creep creep) {
		for(Cell[] row : backgroundTiles)
			for(Cell cell : row)
				if(cell.containsCreep(creep))
					cell.removeCreep(creep);
		
		towerDefenceActivity.runOnUpdateThread(new Runnable() {
			public void run() {
				detachChild(creep);
			}
		});
		
	}

	public void creepKilled(Creep creep) {
		removeCreep(creep);
		playerInfo.addGold(creep.getGoldValue());
		playerInfo.addScore(creep.getScooreValue());
	}
	

	public void waweFinished(Wave wave) {
		currentWave++;
		if(currentWave >= waves.size())
			finished();
		else{
			//TODO: add time delay
			attachChild(waves.get(currentWave));
		}
				
	}

	public void creepEnteredCell(Creep creep, Cell cell) {
		if(goalCell.equals(cell)){
			handleCreepOnGoal(creep);
		}
	}
	
	protected void handleCreepOnGoal(Creep creep) {
		removeCreep(creep);
		playerInfo.subtractLife(1);
		if(playerInfo.getLife() <= 0){
			loose();
		}
	}
	
	public abstract void winn();
	
	public abstract void loose();
}
