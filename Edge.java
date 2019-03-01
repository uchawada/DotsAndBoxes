package dotsAndBoxes;

public class Edge {
	public int x;
	public int y;
	
	/*
	 * Edge class that creates an edge using coordinates x and y 
	 * implements methods isHorizontal and isVertical to check if the edge should be horizontal of vertical
	 */
	public Edge(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
		//enter a horizontal line if x = even numbers, and y =  odd numbers 
		public boolean isHorizontal(){
			if(!(isEven(x) && isOdd(y))){
				return false;
			}
			return true;
		}
		
		
		//enter a vertical line if x = odd numbers, and y = even numbers 
		public boolean isVertical(){
			if(!(isOdd(x) && isEven(y))){
				return false;
			}
			return true;
			
		}
		
		//checks if a number is even 
		public boolean isEven(int num){
			if(!(num%2 == 0)){
				return false;
			}
			return true;
		}
		
		//checks if a number is odd
		public boolean isOdd(int num){
			if(!(num%2 == 1)){
				return false;
			}
			return true;
		}
		
}
