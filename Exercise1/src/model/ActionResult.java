package model;

public class ActionResult {

	private int[][] cells;
	private int scoreGained;
	private boolean altered;
	private int direction;

	public ActionResult(int[][] cells, int scoreGained, boolean altered, int direction) {
		super();
		this.cells = cells;
		this.scoreGained = scoreGained;
		this.altered = altered;
		this.direction = direction;
	}

	public int[][] getCells() {
		return cells;
	}

	public int getScoreGained() {
		return scoreGained;
	}

	public boolean isAltered() {
		return altered;
	}

	public int getDirection() {
		return direction;
	}
	
}
