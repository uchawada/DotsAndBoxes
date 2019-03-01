package dotsAndBoxes;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

	int dimensions;
	public String[][] board;
	public boolean[][] isLine;
	public boolean[][] visited;
	int maxScore;
	int minScore;
	int maxCount;
	int minCount;
	int depth;
	String player;
	int eval;
	int bestX;
	int bestY;
	Board parent;
	ArrayList<Edge> moves;
	Edge e;
	
	public Board(int dimensions){
		board = new String[dimensions][dimensions];
		this.dimensions = dimensions;	
		maxScore = 0;
		minScore = 0;
		maxCount = 0;
		minCount = 0;
		depth = 0;
		player =  "MIN";
		eval = 0;
		bestX = 0; 
		bestY = 0;
		parent = null;
		moves = new ArrayList<Edge>();
		initialize(dimensions);
		
	}
	
	/*
	 * initilizes the board with stars, and random values in the box.
	 * 	Prints the original state of the board
	 * initilizes the board (String[][] array) and the isLine (boolean array)

	 */
	
	public void initialize(int dimensions){
		
		board = new String [(2 * dimensions)+ 1][(2 * dimensions)+ 1];
		isLine = new boolean [(2 * dimensions)+ 1][(2 * dimensions)+ 1];
		visited = new boolean [(2 * dimensions)+ 1][(2 * dimensions)+ 1];
		
		//puts random values in the boxes.
		//also saves isLine to true, as it is already occupied. 

		Random rand = new Random();
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board.length; j++){
				board[i][j] = "(" + Integer.toString(i) + "," + Integer.toString(j)+ ")";
				isLine[i][j] = false;	
			}
		}
		
		//puts stars on the valid coordinates
		//also saves isLine to true, as it is already occupied. 

		for(int i = 0; i < board.length; i+=2){
			for(int j  = 0; j < board.length; j+=2){
				board[i][j] = "  *  ";
				isLine[i][j] = true;
			}
		}
		/*
		 * puts coordinate numbers on the edges for the user to put in the coordinates easily
		 * also saves isLine to true, as it is already occupied. 
		 */
		for(int i = 1; i < board.length; i+=2){
			for(int j  = 1; j < board.length; j+=2){
				Integer r = rand.nextInt((5 - 1) + 1) + 1;
				board[i][j] = "  " +  r.toString() + "  ";
				isLine[i][j] = true;
			}
		}
	}
	/*
	 * returns children edges of the board from the available or open edges, and adds them to the 
	 * arraylist moves 
	 */
	public ArrayList<Edge> getMoves(){
		moves = new ArrayList<Edge>();
		depth = depth + 1;
		
		Board child = clone();
		child.depth = depth + 1;
		child.parent = this;

		
		//checks for the non initilized coordinates in isLine array and returns them
		for(int i = 0; i < isLine.length; i++){
			for(int j = 0; j < isLine.length; j++){
				if(isLine[i][j] == false){
					Edge e = new Edge(i,j);
					moves.add(e);
				}
			}
		}
		return moves;
	}
	
	/*
	 * Prints the current state of the board
	 */
	public void printBoard(){
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board.length; j++){
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
	/*
	 * Makes a move, but taking the coordinates and type of line as arguments 
	 * Line = isHoriztonal() [" - "] or isVertical() [" | "]
	 * Adds the specified edge to the board at given coordinates (x and y)
	 * and initliazes isLine to true, as it is occupied 
	 */
	public String[][] makeMove(int x, int y, String line){
		
		if(isLine[x][y] == true){
			System.out.println("Line already exists");
		}
		
		else{
//			System.out.printf("Inserting " + line + " at (%d, %d)\n", x, y);
			board[x][y] = line;
			isLine[x][y] = true;	
			bestX = x; 
			bestY = y;
			updateScore(x, y);
			
			if(player == "MAX") {
				player = "MIN";

			}
			if(player == "MIN") {
				player = "MAX";
			}
		}
		
		return board;		
		
	
	}
	public boolean checkIfBox(int x, int y){
		
		Edge e = new Edge(x,y);
		if(e.isHorizontal()) {
			//checks the upper horizontal line
			if(x == 0 && y >= 0){
				return isLine[x+1][y-1] && isLine[x+1][y+1] && isLine[x+2][y];	
			}
			//checks the lower horizontal line
			else if(x == board.length - 1 && y >= 0){
				return isLine[x-1][y+1] && isLine[x-1][y-1] && isLine[x-2][y];
			}
			//checks the remaining horizontal lines
			else if(x >= 2 && y >=0 && x < board.length - 2 && y < board.length) {
				return (isLine[x-1][y+1] && isLine[x-1][y-1] && isLine[x-2][y]) || (isLine[x+1][y+1] && isLine[x+1][y-1] && isLine[x+2][y]);
			}
		}
		else if(e.isVertical()) {
			//checks for the left vertical line
			if(x >= 0 && y == 0){
				return isLine[x-1][y+1] && isLine[x][y+2] && isLine[x+1][y+1];	
			}
			//checks for the right vertical line
			else if(x >= 0 && y == board.length - 1){
				return isLine[x+1][y-1] && isLine[x][y-2] && isLine[x-1][y-1];	
			}
			//checks for the remaining vertical lines.
			else if(x >= 0 && y >= 2 && x < board.length && y <= board.length-2){
				return (isLine[x+1][y-1] && isLine[x][y-2] && isLine[x-1][y-1]) || (isLine[x+1][y+1] && isLine[x][y+2] && isLine[x-1][y+1]);
			}
		}
		
		return true;
	}
	
	/*
	 * Updates the score if the given coordinates make a box, and adds the box value to  
	 * the particular player's score.
	 */
	public void updateScore(int x, int y) {
			
			if(checkIfBox(x, y)) {
				
				//gets the value if the edge is on the top horizontal line
				if(x == 0){
					
					if(player == "MAX") {
						maxScore += splitNumber(board[x+1][y]);
					}
					else if (player == "MIN") {
						minScore += splitNumber(board[x+1][y]);
					}
				}
				//gets the value if the edge is on the bottom horizontal line
				else if(x == (board.length - 1)) {
					if(player == "MAX") {
						maxScore += splitNumber(board[x-1][y]);
					}
					else if (player == "MIN") {
						minScore += splitNumber(board[x-1][y]);
					}
				}
				
				//gets the value if the edge is on the left vertical line
				if(y == 0){
					if(player == "MAX") {
						maxScore += splitNumber(board[x][y+1]);
					}
					else if (player == "MIN") {
						minScore += splitNumber(board[x][y+1]);
					}
				}
				//gets the value if the edge is on the right vertical line
				else if(y == (board.length - 1)) {
					if(player == "MAX") {
						maxScore += splitNumber(board[x][y-1]);
					}
					else if (player == "MIN") {
						minScore += splitNumber(board[x][y-1]);
					}
				}
				
				//checks for the remaining edges that make a box
				else if(x > 0 && x < board.length-1){
					Edge e = new Edge(x,y);
					
					//if the edge is horizontal
					if(e.isHorizontal()) {
						
						//checks for the box on the left 
						if(checkIfBox(x-2, y)){
							if(player == "MAX") {
								maxScore += splitNumber(board[x-1][y]);
							}
							else if (player == "MIN") {
								minScore += splitNumber(board[x-1][y]);
							}
						}
						//checks for the box on the right
						else if(checkIfBox(x+2, y)) {
							if(player == "MAX") {
								maxScore += splitNumber(board[x-1][y]);
							}
							else if (player == "MIN") {
								minScore += splitNumber(board[x-1][y]);
							}
						}
					}
					else if(e.isVertical()) {
						
						//checks for the box on the top
						if(checkIfBox(x, y-2)){
							if(player == "MAX") {
								maxScore += splitNumber(board[x][y-1]);
							}
							else if (player == "MIN") {
								minScore += splitNumber(board[x][y-1]);
							}
						}
						//checks for the box ogn the bottom
						else if(checkIfBox(x, y+2)) {
							if(player == "MAX") {
								maxScore += splitNumber(board[x][y+1]);
							}
							else if (player == "MIN") {
								minScore += splitNumber(board[x][y+1]);
							}
						}
					}				
				}	
			}
			
		}
	
	//sets the edges
	public void setEdge(int x, int y){
		Edge e = new Edge(x,y);
		if(e.isHorizontal()){
			makeMove(x, y, " --- ");
		}
		else if(e.isVertical()){
			makeMove(x, y, "  |  ");
		}
	}

	
	//evaluate function for the minimax algorithm 
	public void evaluate() {
		eval = maxScore - minScore;
	}
	
	//gets maxCount
	public int maxCount(){
		return maxCount;
	}
	
	// gets minCount
	public int minCount(){
		return minCount;
	}
	
	//gets maxScore
	public int getMaxScore() {
		return maxScore;
	}
	
	//gets minScore
	public int getMinScore() {
		return minScore;
	}
	
	//gets depth
	public int getDepth() {
		return depth;
	}
	
	//sets the player to the provided player
	public void setPlayer(String player) {
		this.player = player;
	}
	
	//gets the current player
	public String getPlayer() {
		return player;
	}
	
	//gets the best value of X
	public int getBestX() {
		return bestX;
	}
	
	//gets the best value of Y
	public int getBestY() {
		return bestY;
	}
	
	//gets the parent
	public Board getParent() {
		return this.parent;
	}
	
	//returns a board after adding the edge. 
	public Board getNewBoard(Edge edge) {
		
		Board newBoard = clone();
		newBoard.setEdge(edge.getX(), edge.getY());
		return newBoard;
    }
	
	
	//clones the board, and returns a new board with all of the same values as the current board
	public Board clone(){
		Board cloned = new Board(dimensions);

		for(int i = 0; i < dimensions; i++){
			for(int j = 0; j < dimensions; j++){
				cloned.board[i][j] = this.board[i][j];
			}
		}
		
        cloned.maxScore = maxScore;
        cloned.minScore = minScore;
        cloned.maxCount = maxCount;
        cloned.minCount = minCount;
        cloned.depth = depth;
        cloned.player = player;
        cloned.eval = eval;
        cloned.bestX = bestX;
        cloned.bestY = bestY;

        return cloned;
	}

	//splits the number to get a string 
	//used for the updateScore() method, as to get the value from the box
	public int splitNumber(String s) {
		s = Character.toString(s.charAt(2));
		return Integer.parseInt(s);
		
	}

	
}
