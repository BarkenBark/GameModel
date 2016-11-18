package lab1;

public class WinningException extends Exception {
	private final int score;

	/** Creates a Winning Exception telling the final score.
	 * 
	 * @param score The final score of the game.
	 */
	public WinningException(final int score) {
		this.score = score;
	}

	/** Get the score of the game */
	public int getScore() {
		return this.score;
	}

}
