package no.progark.a18.towerdefence.gameContent;

import java.util.LinkedList;
import java.util.List;

import no.progark.a18.towerdefence.level.CreepKilledListener;
import no.progark.a18.towerdefence.level.TowerDefenceScene;
import no.progark.a18.towerdefence.level.WaveFinishedListener;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;

public class Wave extends Entity {
	private List<WaveFinishedListener> waweFinishedListeners = new LinkedList<WaveFinishedListener>();
	protected List<Creep> creeps;
	protected List<Float> spawnDelay;
	protected TowerDefenceScene scene;

	public Wave(final List<Creep> creeps, final List<Float> spawnDelay,
			final TowerDefenceScene scene) {
		if (creeps.size() != spawnDelay.size())
			throw new IllegalArgumentException(
					"Creeps and spawnDelay must be of same size");

		this.creeps = creeps;
		this.spawnDelay = spawnDelay;
		this.scene = scene;
		scene.detachChild(Wave.this);
		creeps.get(creeps.size() - 1).registerCreepKilledListener(
				new CreepKilledListener() {

					public void creepKilled(Creep creep) {
						fireWaveFinished();
					}
				});
		registerUpdateHandler(setCreepDispenserHandeler(creeps, spawnDelay, scene));
	}

	protected IUpdateHandler setCreepDispenserHandeler(final List<Creep> creeps,
			final List<Float> spawnDelay, final TowerDefenceScene scene) {
		return new IUpdateHandler() {
			private long lastSent = System.currentTimeMillis();
			private int nextCreep = 0;

			public void onUpdate(float pSecondsElapsed) {
				if (nextCreep < creeps.size()-1 && System.currentTimeMillis() - lastSent < spawnDelay
						.get(nextCreep))
					return;
				lastSent = System.currentTimeMillis();
				Creep creep = creeps.get(nextCreep++);
				if(nextCreep >= creeps.size())
					scene.detachChild(Wave.this);
				scene.sendCreep(creep);

			}

			public void reset() {
				nextCreep = 0;
			}
		};
	}

	private void fireWaveFinished() {
		for (WaveFinishedListener wfl : waweFinishedListeners)
			wfl.waweFinished(this);
	}

	public void registerWaveFinishedListener(WaveFinishedListener wfl) {
		waweFinishedListeners.add(wfl);
	}

	public boolean removeWaveFinishedListener(WaveFinishedListener wfl) {
		return waweFinishedListeners.remove(wfl);
	}

}
