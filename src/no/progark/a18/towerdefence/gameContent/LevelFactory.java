package no.progark.a18.towerdefence.gameContent;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import no.progark.a18.towerdefence.TowerDefenceActivity;
import no.progark.a18.towerdefence.level.TowerDefenceScene;

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
	public static TowerDefenceScene getLevel(String id) {
		if ("static".equals(id))
			return new StaticLevel1(TDA);
		else if(id != null)
			return new Level(TDA, id);
//		return new FirstLevel(TDA);
		// TODO: Load from local fileSystem

		return null;
	}
}
