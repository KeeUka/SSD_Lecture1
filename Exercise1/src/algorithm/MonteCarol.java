package algorithm;

import java.util.List;
import java.util.Random;
import model.ActionResult;
import model.Board;

public class MonteCarol {

	private Random random = new Random();
	private double upScore, downScore, leftScore, rightScore;
	private int upCount, downCount, leftCount, rightCount;
	private double avgUp, avgDown, avgLeft, avgRight;

	public MonteCarol() {
		resetAllVariables();
	}
	
	public int getNextDirection(Board board, int limitSamplesCount) {
		resetAllVariables();
		List<ActionResult> actions = board.getValidActions();
		for(int count = 0; count < limitSamplesCount; count++) {
			ActionResult a = actions.get(random.nextInt(actions.size()));
			int direction = a.getDirection();
			if (direction == Board.MOVE_UP) {
				upCount++;
				upScore += getRandomPlayScore(a.getDirection(), board);
			} else if (direction == Board.MOVE_DOWN) {
				downCount++;
				downScore += getRandomPlayScore(a.getDirection(), board);
			} else if (direction == Board.MOVE_LEFT) {
				leftCount++;
				leftScore += getRandomPlayScore(a.getDirection(), board);
			} else if (direction == Board.MOVE_RIGHT) {
				rightCount++;
				rightScore += getRandomPlayScore(a.getDirection(), board);
			}
		}
		updateMovementValues();
		return getMovementWithMaxvalue();
	}

	private void resetAllVariables() {
		upScore = downScore = leftScore = rightScore = 0;
		upCount = downCount = leftCount = rightCount = 0;
		avgUp = avgDown = avgLeft = avgRight = 0;
	}

	private void updateMovementValues() {
		avgUp = upCount == 0 ? 0 : upScore / upCount;
		avgDown = downCount == 0 ? 0 : downScore / downCount;
		avgLeft = leftCount == 0 ? 0 : leftScore / leftCount;
		avgRight = rightCount == 0 ? 0 : rightScore / rightCount;
	}

	private int getMovementWithMaxvalue() {
		double maxAverage = Math.max(avgUp,
				Math.max(avgDown, Math.max(avgLeft, avgRight)));
		if (avgUp >= maxAverage)
			return Board.MOVE_UP;
		if (avgDown >= maxAverage)
			return Board.MOVE_DOWN;
		if (avgLeft >= maxAverage)
			return Board.MOVE_LEFT;
		if (avgRight >= maxAverage)
			return Board.MOVE_RIGHT;	
		return Board.MOVE_INVALID;
	}
	
	private int getRandomPlayScore(int startDirection, Board b) {
		Board board = new Board(b.getCells());
		ActionResult r = b.tilt(startDirection);
		board.setCurrentScore(board.getCurrentScore() + r.getScoreGained());
		board.setCells(r.getCells());
		board.insert2Or4();
		while (!board.isEnd()) {
			List<ActionResult> actions = board.getValidActions();
			ActionResult a = actions.get(random.nextInt(actions.size()));
			board.setCurrentScore(board.getCurrentScore() + a.getScoreGained());
			board.setCells(a.getCells());
			board.insert2Or4();
		}
		return board.getCurrentScore();
	}

}
