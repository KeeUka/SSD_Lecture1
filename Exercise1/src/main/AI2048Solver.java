package main;

import controller.RemoteController;
import model.Board;
import algorithm.MonteCarol;

public class AI2048Solver {

	private Board board;
	private RemoteController controller;
	private MonteCarol algorithm;
	
	public AI2048Solver() {
		board = new Board();
		controller = new RemoteController();
		algorithm = new MonteCarol();
		controller.init();
	}
	
	public void start() {
		board.setCells(controller.getBoardArray());
		while(!board.isEnd()) {
			int direction = algorithm.getNextDirection(board, 1000);
			controller.move(direction);
			board.setCells(controller.getBoardArray());			
			board.setCurrentScore(controller.getScore());;	
		}
	}
	
	
	public static void main(String[] args) {
		AI2048Solver ai = new AI2048Solver();
		ai.start();
	}
	
}
