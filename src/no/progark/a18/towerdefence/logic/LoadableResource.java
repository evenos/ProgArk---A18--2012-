package no.progark.a18.towerdefence.logic;

import org.andengine.ui.activity.BaseGameActivity;

public interface LoadableResource {
	/**
	 * Loads the recourses needed for the object to display. Usable for instance prefetching.
	 * @param tda the games {@linkplain BaseGameActivity}
	 */
	public void loadResourses(final BaseGameActivity tda);
}
