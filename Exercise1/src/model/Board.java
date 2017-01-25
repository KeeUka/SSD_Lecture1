package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Random;

public class Board {

	public static final int BOARD_SIZE = 4;
	public static final int MOVE_UP = 38;
	public static final int MOVE_DOWN = 40;
	public static final int MOVE_LEFT = 37;
	public static final int MOVE_RIGHT = 39;
	public static final int MOVE_INVALID = -1;
	public static final int BORDER = -1;

	private int[][] cells;
	private Random random;
	private int currentScore;

	public Board() {
		resetCells();
		random = new Random();
	}

	public Board(int[][] cells) {
		this.cells = cells;
		random = new Random();
	}

	public int[][] getCells() {
		return cells;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	public int getCurrentScore() {
		return currentScore;
	}

	public void resetCells() {
		int[][] tmpCells = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
				{ 0, 0, 0, 0 } };
		cells = tmpCells;
	}

	public boolean insert2Or4() {
		double val = random.nextDouble();
		int num = (val < 0.9) ? 2 : 4;
		List<Point> availableCells = getAvailableCells();
		if (availableCells.isEmpty())
			return false;
		int pos = random.nextInt(availableCells.size());
		Point p = availableCells.get(pos);
		cells[p.x][p.y] = num;
		return true;
	}

	private List<Point> getAvailableCells() {
		List<Point> availableCells = new ArrayList<Point>();
		for (int r = 0; r < BOARD_SIZE; r++)
			for (int c = 0; c < BOARD_SIZE; c++)
				if (cells[r][c] == 0)
					availableCells.add(new Point(r, c));
		return availableCells;
	}

	// This method is super ugly and long but it's super fast.
	// It is called millions of times in simulation (in some algorithms)
	// so the speed is more important than readability.
	public ActionResult tilt(int direction) {

		int scoreGain = 0;
		int[][] newCells = new int[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				newCells[row][col] = cells[row][col];
			}
		}

		switch (direction) {
		case MOVE_UP:
			for (int col = 0; col < BOARD_SIZE; col++) {
				int row = 0;
				int mergePosition = -1;
				boolean merged = false;
				while (row < BOARD_SIZE) {
					if (newCells[row][col] != 0) {
						if (mergePosition == -1) {
							mergePosition = 0;
							int tmp = newCells[row][col];
							newCells[row][col] = 0;
							newCells[0][col] = tmp;
							merged = false;
						} else if (newCells[mergePosition][col] == newCells[row][col]
								&& !merged) {
							int tmp = newCells[mergePosition][col];
							newCells[row][col] = 0;
							newCells[mergePosition][col] = tmp * 2;
							scoreGain += tmp * 2;
							merged = true;
							row = mergePosition;
						} else {
							mergePosition++;
							int tmp = newCells[row][col];
							newCells[row][col] = 0;
							newCells[mergePosition][col] = tmp;
							merged = false;
						}
					}
					row++;
				}
			}
			break;
		case MOVE_DOWN:
			for (int col = 0; col < BOARD_SIZE; col++) {
				int row = BOARD_SIZE - 1;
				int mergePosition = BOARD_SIZE;
				boolean merged = false;
				while (row >= 0) {
					if (newCells[row][col] != 0) {
						if (mergePosition == BOARD_SIZE) {
							mergePosition = BOARD_SIZE - 1;
							int tmp = newCells[row][col];
							newCells[row][col] = 0;
							newCells[BOARD_SIZE - 1][col] = tmp;
							merged = false;
						} else if (newCells[mergePosition][col] == newCells[row][col]
								&& !merged) {
							int tmp = newCells[mergePosition][col];
							newCells[row][col] = 0;
							newCells[mergePosition][col] = tmp * 2;
							scoreGain += tmp * 2;
							merged = true;
							row = mergePosition;
						} else {
							mergePosition--;
							int tmp = newCells[row][col];
							newCells[row][col] = 0;
							newCells[mergePosition][col] = tmp;
							merged = false;
						}
					}
					row--;
				}
			}
			break;
		case MOVE_RIGHT:
			for (int row = 0; row < BOARD_SIZE; row++) {
				int col = BOARD_SIZE - 1;
				int mergePosition = BOARD_SIZE;
				boolean merged = false;
				while (col >= 0) {
					if (newCells[row][col] != 0) {
						if (mergePosition == BOARD_SIZE) {
							mergePosition = BOARD_SIZE - 1;
							int tmp = newCells[row][col];
							newCells[row][col] = 0;
							newCells[row][BOARD_SIZE - 1] = tmp;
							merged = false;
						} else if (newCells[row][mergePosition] == newCells[row][col]
								&& !merged) {
							int tmp = newCells[row][mergePosition];
							newCells[row][col] = 0;
							newCells[row][mergePosition] = tmp * 2;
							scoreGain += tmp * 2;
							merged = true;
							col = mergePosition;
						} else {
							mergePosition--;
							int tmp = newCells[row][col];
							newCells[row][col] = 0;
							newCells[row][mergePosition] = tmp;
							merged = false;
						}
					}
					col--;
				}
			}
			break;
		case MOVE_LEFT:
			for (int row = 0; row < BOARD_SIZE; row++) {
				int col = 0;
				int mergePosition = -1;
				boolean merged = false;
				while (col < BOARD_SIZE) {
					if (newCells[row][col] != 0) {
						if (mergePosition == -1) {
							mergePosition = 0;
							int tmp = newCells[row][col];
							newCells[row][col] = 0;
							newCells[row][0] = tmp;
							merged = false;
						} else if (newCells[row][mergePosition] == newCells[row][col]
								&& !merged) {
							int tmp = newCells[row][mergePosition];
							newCells[row][col] = 0;
							newCells[row][mergePosition] = tmp * 2;
							scoreGain += tmp * 2;
							merged = true;
							col = mergePosition;
						} else {
							mergePosition++;
							int tmp = newCells[row][col];
							newCells[row][col] = 0;
							newCells[row][mergePosition] = tmp;
							merged = false;
						}
					}
					col++;
				}
			}
			break;
		default:
			break;
		}
		boolean altered = !Arrays.deepEquals(cells, newCells);
		return new ActionResult(newCells, scoreGain, altered, direction);
	}

	public void setCells(int[][] cells) {
		this.cells = cells;
	}

	public int getCell(int row, int col) {
		return cells[row][col];
	}

	public List<Integer> getAllValues() {
		List<Integer> list = new ArrayList<Integer>();
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				Integer val = new Integer(cells[row][col]);
				if (!list.contains(val))
					list.add(new Integer(val));
			}
		}
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}

	public List<ActionResult> getValidActions() {
		ActionResult leftResult = this.tilt(MOVE_LEFT);
		ActionResult rightResult = this.tilt(MOVE_RIGHT);
		ActionResult upResult = this.tilt(MOVE_UP);
		ActionResult downResult = this.tilt(MOVE_DOWN);
		List<ActionResult> list = new ArrayList<ActionResult>();
		if (leftResult.isAltered())
			list.add(leftResult);
		if (rightResult.isAltered())
			list.add(rightResult);
		if (upResult.isAltered())
			list.add(upResult);
		if (downResult.isAltered())
			list.add(downResult);
		return list;
	}

	public boolean isEnd() {
		return getValidActions().isEmpty();
	}
	
}
