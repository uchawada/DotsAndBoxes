package dotsAndBoxes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Minimax{
	
	//myPlayer (computer) is maximizing player 
	//oppPlayer (human) is minimizing player
	
	/*
	 * minimax function implements the minimax algorithm, which returns the best value 
	 * for the max player (computer player)
	 */
	public int minimax(Board board, int ply, String player){

		
		board.player = player;
		List<Edge> children = board.getMoves();

		Collections.shuffle(children);


		if(board.depth >= ply || children.isEmpty()) {
			board.evaluate();
			return board.eval;
		}
		
		/*
		 * If the player is max, then it recursively calls minimax and changing the player to min
		 * returns the maximum value.
		 * Decrements ply 
		 */
		if(player == "MAX") {
			
			int value = Integer.MIN_VALUE;
			int minMaxVal;
			for(Edge e: children) {
				
				Board child = board.getNewBoard(e);
				minMaxVal = minimax(child, ply-1, "MIN");
								
				//max value
				if(minMaxVal > value) {
					value = minMaxVal;
				}
				board.eval = value;
				return value;	
			}
		}
		/*
		 * If the player is min, then it recursively calls minimax and changing the player to max
		 * returns the minimum value.
		 * Decrements ply 
		 */
		else if(player == "MIN") {
			int value = Integer.MAX_VALUE;
			int minMaxVal;
			for(Edge e: children) {
				
				Board child = board.getNewBoard(e);
				minMaxVal = minimax(child, ply-1, "MAX");
				
				//min value 
				if(minMaxVal < value) {
					value = minMaxVal;
				}

				board.eval = value;
				return value;	
			}
		}
		return 0;
	}
	
	/*
	 * Makes a move, takes board, ply and player type as the argument 
	 * calls the minimax function and saves its value to a value, and if the value equals 
	 * 		the best value of the root, then it returns the best edge. 
	 */
	public Edge makeMove(Board board, int ply, String player) {
		Board child = board.clone();
		int value = minimax(child, ply, player);
		Edge edge = new Edge(0,0);
		ArrayList<Edge> children = child.getMoves();
		for(Edge e : children) {
			Board root = board.clone();
			root.setEdge(e.getX(), e.getY());
			if(root.eval == value) {
				edge.setX(root.bestX);
				edge.setY(root.bestY);
			}
		}
		return edge;
	}
}

