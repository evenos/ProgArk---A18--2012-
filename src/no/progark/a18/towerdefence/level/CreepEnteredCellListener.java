package no.progark.a18.towerdefence.level;

import no.progark.a18.towerdefence.gameContent.Cell;
import no.progark.a18.towerdefence.gameContent.Creep;

public interface CreepEnteredCellListener {
	public void creepEnteredCell(Creep creep, Cell cell);
}
