package no.progark.a18.towerdefence.gameContent;

import no.progark.a18.towerdefence.level.StaticLevel1;


/**
 * Player values connected to the game
 */
public class PlayerInfo {
	
	private int life;
	private int gold;
	private StaticLevel1 level;

	public PlayerInfo(String difficulty, StaticLevel1 level){
		if(difficulty.equals("easy"))
			life=15;
		else if(difficulty.equals("medium"))
			life=10;
		else
			life=1;
		
		this.level = level;
	}
	
	public PlayerInfo(StaticLevel1 level){
		life=3;
		this.level = level;
	}

	/**
	 * @return the life
	 */
	public int getLife() {
		return life;
	}
	
	public void subtractLife(int amount){
		life-=amount;
		level.updateLife();
	}


	/**
	 * @return the gold
	 */
	public int getgold() {
		return gold;
	}

	/**
	 * @param gold the gold to add
	 */
	public void addGold(int gold) {
		this.gold = gold;
		level.updateGold();
	}
	
	/**
	 * @param gold the gold to subtract
	 */
	public void subtractGold(int gold) {
		this.gold = gold;
		level.updateGold();
	}

}
