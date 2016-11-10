package lab1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 
 * @author Jesper Larsson & Oscar Bark
 * Model of a basic snake game. The snake moves in either x or y direction.
 * Game over if the snake tries to go through itself or a wall. 
 * One piece of food is on the board at a time. When eaten a new will spawn.
 * Each piece of food increases the size of the snake by one.
 *
 */

public class SnakeModel extends GameModel {
	public enum Directions {
		EAST(1, 0),
		WEST(-1, 0),
		NORTH(0, -1),
		SOUTH(0, 1),
		NONE(0, 0);

		private final int xDelta;
		private final int yDelta;

		Directions(final int xDelta, final int yDelta) {
			this.xDelta = xDelta;
			this.yDelta = yDelta;
		}

		public int getXDelta() {
			return this.xDelta;
		}

		public int getYDelta() {
			return this.yDelta;
		}
		
		
	}
	
	/** Graphical representation of the snake's head. */
	private final GameTile SNAKE_HEAD = new RoundTile(new Color(0,0,0),
			new Color(255,0,0), 2.0);
	
	/** Graphical representation of the snake's body parts. */
	private final GameTile SNAKE_BODY = new RoundTile(new Color(0,0,0),
			new Color(0,255,0), 2.0);
	
	/** Graphical representation of a piece of food. */
	private final GameTile FOOD = new RoundTile(new Color(0,0,0),
			new Color(0,0,0), 2.0);
	
	/** Graphical representation of unoccupied space. */
	private final GameTile BLANK_TILE = new GameTile();
	
	/** ArrayDeque containing the positions of all snake 
	 * body parts and it's head. */
	private Deque<Position> snakePos = new ArrayDeque<Position>();
	
	/** Current position of the food. */
	private Position foodPos;
	
	/** Current score. */
	private int score;
	
	/** Current direction of the snake's head */
	private Directions direction = Directions.NORTH; //Initial direction north
	
	/** Default constructor */
	public SnakeModel() {
		Dimension size = getGameboardSize();
		
		//Reset the gameboard
		for (int i=0; i<size.height; i++){
			for (int j=0; j<size.width; j++){
				setGameboardState(i, j, BLANK_TILE);
			}
			
		}
		
		//Spawn snakehead in the middle 
		//TODO Spawn one or two body parts adjacent to head
		Position initialPos = new Position(size.height / 2,
				size.width / 2); 
		this.snakePos.addFirst(initialPos);
		setGameboardState(snakePos.getFirst(),SNAKE_HEAD);
		
		//Place food somewhere
		addFood();
		
	}
	
	/** Places a piece of food on the gameboard where possible */
	//TODO Ignore placing piece of food if the gameboard is filled
	private void addFood(){
		Dimension size = getGameboardSize();
		
		//Randomize position until at empty tile
		do{
			this.foodPos = new Position((int) (Math.random() * size.width),
					(int) (Math.random() * size.height));
		}while(!isPositionEmpty(foodPos));
	
		setGameboardState(foodPos, FOOD);
		
	}

	private boolean isPositionEmpty(final Position pos) {
		return (getGameboardState(pos) == BLANK_TILE);
	}
	
	
	/**
	 * Update the direction of the snake head
	 * according to the user's keypress.
	 */
	private void updateDirection(final int key) {
		switch (key) { //Should break; be within if-statements or outside?
			case KeyEvent.VK_LEFT:
				if (this.direction != Directions.EAST){ // Prevents reverse control
				this.direction = Directions.WEST;
				}
				break;
			case KeyEvent.VK_UP:
				if (this.direction != Directions.SOUTH){
				this.direction = Directions.NORTH;
				}
				break;

			case KeyEvent.VK_RIGHT:
				if (this.direction != Directions.WEST){
				this.direction = Directions.EAST;
				}
				break;
				

			case KeyEvent.VK_DOWN:
				if (this.direction != Directions.NORTH){
				this.direction = Directions.SOUTH;
				}
				break;
				
				
			default:
				// Don't change direction if another key is pressed
				break;
		}
	}
	/**
	 * Gets the next position of the snake's head
	 * @return Position x,y of next position. 
	 */
	private Position getNextSnakePos(){
		return new Position(
				this.snakePos.getFirst().getX() + this.direction.getXDelta(),
				this.snakePos.getFirst().getY() + this.direction.getYDelta()
				);
	}
	

	/**
	 * 
	 * @param pos The position to test.
	 * @return false if the position is outside the playing field, true otherwise.
	 */
	private boolean isOutOfBounds(Position pos) {
		return pos.getX() < 0 || pos.getX() >= getGameboardSize().width
				|| pos.getY() < 0 || pos.getY() >= getGameboardSize().height;
	}

	@Override
	public void gameUpdate(final int lastKey) throws GameOverException{
		updateDirection(lastKey);
		
		//Check if out of bounds or collides with body, if then; throw GameOverException
		if (isOutOfBounds(getNextSnakePos()) || this.snakePos.contains(getNextSnakePos())) {
			throw new GameOverException(this.score);
		}
		
		//Replace current headPos with body, Update snakePos, and draw it at the new position
		setGameboardState(this.snakePos.getFirst(), SNAKE_BODY);
		snakePos.addFirst(getNextSnakePos());
		setGameboardState(this.snakePos.getFirst(), SNAKE_HEAD);
		
		//Debugging
		/*
		System.out.print("X-pos " + snakePos.getFirst().getX()+ "  ");
		System.out.print("Y-pos " + snakePos.getFirst().getY()+ "  ");
		System.out.println("Foodpos x " + this.foodPos.getX() + " Foodpos y " + this.foodPos.getY());
		*/
		
		//Check if the snake head is on pos = foodPos, if then; addFood(), score+1, 
		if (this.snakePos.getFirst().equals(foodPos)){
			addFood();
			this.score++;
		}else{
			setGameboardState(snakePos.getLast(), BLANK_TILE);
			snakePos.removeLast();
		}
		
	}
	
	
	
}
