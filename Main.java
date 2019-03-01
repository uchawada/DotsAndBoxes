package dotsAndBoxes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {

	public boolean[][] isLine;
	public int dimensions; 
	String[][] board; 
	String horizontal = "  -  ";
	String vertical = "  |  ";
	
	public static void main(String[] args) throws IOException{
		Main main = new Main();
		BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
		System.out.print("Board dimensions: ");
		String x = reader.readLine();
		main.dimensions = Integer.parseInt(x);
		Board board = new Board(main.dimensions);
		board.initialize(main.dimensions);
		board.printBoard();
		
		main.miniMaxPlayer(board);
		
	}
	
	
	/*
	 * Takes the input from the user (values of x and y) and plays the game using the inputs. 
	 * Alternatively calls the functions for the min and the max player, starts with the min player.
	 */
	public void miniMaxPlayer(Board board) throws IOException{
		
		
		board.player = "MIN";
		
		BufferedReader reader =  
                new BufferedReader(new InputStreamReader(System.in)); 
				
		int size = board.getMoves().size();
		
		System.out.print("Number of plys: ");
		String p = reader.readLine();
		int ply = Integer.parseInt(p);

		System.out.println("Start with player (MIN, MAX, RANDOM): ");
		System.out.println("MIN = 1");
		System.out.println("MAX = 2");
		System.out.println("RANDOM = 3");

		System.out.print("Select a number: ");
		String play = reader.readLine();
		int player = Integer.parseInt(play);
		
		
		//givevs player options to pick their game, and which player to start first
		if(player==1) board.player = "MIN";
		if(player==2) board.player = "MAX";
		if(player == 3) randomPlayer(board);
		
		
		while(size >= 0){

			if(size == 0) {
				System.out.println("MinScore = "+ board.getMinScore());
				System.out.println("MaxScore = "+ board.getMaxScore());
				break;
			}
			
			/*
			 * if the player is MIN, then asks player for their coordinate inputs and makes 
			 * a move according to the coordinate and the type of the line(horizontal or vertical) 
			 */
			if(board.player == "MIN") {
				
				System.out.println("Players turn: ");
				System.out.print("     Type x: "); String xx = reader.readLine();
				int x = Integer.parseInt(xx);
				
					
				System.out.print("     Type y: "); String yy = reader.readLine();	

				int y = Integer.parseInt(yy);
				
				//Creates an edge 
				Edge e = new Edge(x,y);
				
				//if horizontal, puts a horizontal move
				if(e.isHorizontal()){
					board.makeMove(e.getX(), e.getY(), " --- ");
					System.out.printf("Inserting " + " --- " + " at (%d, %d)\n", e.getX(), e.getY());
					board.printBoard();
				}
				
				//puts a vertical line if the edge is vertical 
				else if (e.isVertical()){
					board.makeMove(e.getX(), e.getY(), "  |  ");
					System.out.printf("Inserting " + "  |  " + " at (%d, %d)\n", e.getX(), e.getY());
					board.printBoard();
					
				}	
				board.player = "MAX";
			}
			
			else if(board.player == "MAX") {
				//Max player playing
				
				//calling the minimax() function
				Minimax min = new Minimax();
				
				//gets an edge from the function
				Edge m = min.makeMove(board, ply, "MIN");
				

				board.printBoard();
				System.out.println("Minimax turn: ");
				System.out.println("     X = " + m.getX() + ", Y = " + m.getY());
				
				//makes move according to the edge provided by the minimax 
				if(m.isHorizontal()) {
					board.board = board.makeMove(m.getX(), m.getY(), " --- ");
					System.out.printf("Inserting " + " --- " + " at (%d, %d)\n", m.getX(), m.getY());
					board.printBoard();
				}
				else if (m.isVertical()){
					board.board = board.makeMove(m.getX(), m.getY(), "  |  ");
					System.out.printf("Inserting " + "  |  " + " at (%d, %d)\n", m.getX(), m.getY());
					board.printBoard();

				}	
				board.player = "MIN";
			}
			
			
				size = board.getMoves().size();

			}
	}
	
	//returns a random edge from the children of the current board
	public Edge randomEdge(Board board) {
		int size = board.getMoves().size();
		
		Random rand = new Random();
		int num = rand.nextInt(size);
		return board.getMoves().get(num);		
		
	}
	
	public void randomPlayer(Board board) throws IOException {
		BufferedReader reader =  
                new BufferedReader(new InputStreamReader(System.in)); 
				
		int size = board.getMoves().size();
		
		while(size >= 0) {
			//gets the random edge
			
			//makes move according to the edge information
			Edge r = randomEdge(board);
			if(r.isHorizontal()) {
				board.board = board.makeMove(r.getX(), r.getY(), " --- ");
				System.out.printf("Inserting " + " --- " + " at (%d, %d)\n", r.getX(), r.getY());
				board.printBoard();
			}
			else if(r.isVertical()) {
				board.board = board.makeMove(r.getX(), r.getY(), "  |  ");
				System.out.printf("Inserting " + "  |  " + " at (%d, %d)\n", r.getX(), r.getY());
				board.printBoard();
			}
			
			//human playing 
			System.out.print("     Type x: "); String xx = reader.readLine();
			int x = Integer.parseInt(xx);
			
				
			System.out.print("     Type y: "); String yy = reader.readLine();	

			int y = Integer.parseInt(yy);
			
			//Creates an edge 
			Edge e = new Edge(x,y);
			
			//if horizontal, puts a horizontal line
			if(e.isHorizontal()){
				board.board = board.makeMove(e.getX(), e.getY(), " --- ");
				System.out.printf("Inserting " + " --- " + " at (%d, %d)\n", e.getX(), e.getY());
				board.printBoard();
			}
			//if vertical, puts a vertical line

			else if (e.isVertical()){
				board.board = board.makeMove(e.getX(), e.getY(), "  |  ");
				System.out.printf("Inserting " + "  |  " + " at (%d, %d)\n", e.getX(), e.getY());
				board.printBoard();
				
			}	
			//the size of the board updates as per the changes in the board
			size = board.getMoves().size();
		}
		
	}
	
	//checks error from the input strings
	public boolean errorCheck(String s) {
		try {
			Integer.parseInt(s);
		}
		
		catch(NumberFormatException e) {
			System.out.println("Invalid entry");
			return false;
		}
		
		catch(NullPointerException e){
			System.out.println("Invalid entry");
			return false;
		}
		try {
			return Integer.parseInt(s) < dimensions*dimensions || Integer.parseInt(s) > dimensions*dimensions;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndex out of bounds");
			return false;

		}
		
	}
	
	
	

}
