package marc.nguyen.minesweeper.common.data.models;

/**
 * Level of difficulties for a minesweeper.
 *
 * <p>The values of Level.CUSTOM shouldn't be handled.
 */
public enum Level {
  EASY(10, 10, 10),
  MEDIUM(16, 16, 40),
  HARD(16, 30, 100),
  CUSTOM(-1, -1, -1);

  public final int length;
  public final int height;
  public final int mines;

  Level(int length, int height, int mines) {
    this.length = length;
    this.height = height;
    this.mines = mines;
  }
}
