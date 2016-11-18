package lab1;

/**
 * Thrown by GameModel.doCommand at game termination. The exception contains
 * information about how many points the player got.
 */
public class GameOverException extends Exception {
	private final int score;
	private final boolean didWin;

	/** Constructs a new exception with the final score.
	 * 
	 * @param score The final score of the game.
	 */
	public GameOverException(final int score, final boolean didWin) {
		this.score = score;
		this.didWin = didWin;
	}

	/** Get the score of the game */
	public int getScore() {
		return this.score;
	}
	public boolean getDidWin(){
		return this.didWin;
	}
}
