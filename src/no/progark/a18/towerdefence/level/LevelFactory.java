package no.progark.a18.towerdefence.level;

import org.andengine.ui.activity.BaseGameActivity;

import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.logic.Scene;

/**
 *Factory class for loading scenes
 */
public class LevelFactory {
	private static TowerDefenceActivity TDA;

	/**
	 * Set the games {@linkplain BaseGameActivity} used to load resourses. This
	 * property must be set before {@linkplain LevelFactory#getLevel(String)} is
	 * Called.
	 */
	public static void setTowerDefenceActivity(TowerDefenceActivity tda){
		LevelFactory.TDA = tda;
	}
	
	/**
	 * Loads the the {@linkplain Scene} corresponding to the given {@code id}.
	 * If no corresponding level was found, it returns {@code null}.
	 * 
	 * @param id the ID identifying the level
	 * @return the loaded scene or null if non were found.
	 */
	public static Scene getLevel(String id) {
		if ("1".equals(id))
			return new StaticLevel1(TDA);
		if ("2".equals(id))
		return new FirstLevel(TDA);
		// TODO: Load from local fileSystem

		return null;
	}
}
