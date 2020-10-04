package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * A <code>Tile</code> that can be either <code>Empty</code> (which shows the number of adjacent
 * mines) or <code>Mine</code>.<br>
 * <br>
 * This class is immutable.
 */
public abstract class Tile implements Serializable {

  public final Position position;
  private final State state;

  private Tile(@NotNull Position position, @NotNull State state) {
    this.position = position;
    this.state = state;
  }

  private Tile(@NotNull Position position) {
    this(position, State.BLANK);
  }

  private Tile(int x, int y) {
    this(x, y, State.BLANK);
  }

  private Tile(int x, int y, @NotNull State state) {
    this(new Position(x, y), state);
  }

  public Integer getX() {
    return position.getX();
  }

  public Integer getY() {
    return position.getY();
  }

  /**
   * Look for the neighbor based on the indices stored in this object in a 2D Array of tiles.
   *
   * @param tiles A 2D array of Tile.
   * @return Neighbors Tiles of this.
   */
  public Stream<Tile> getNeighborsTilesIn(Tile[][] tiles) {
    final int maxX = tiles.length - 1;
    final int maxY = tiles[0].length - 1;

    final int startX = position.getX() > 0 ? position.getX() - 1 : position.getX();
    final int endX = position.getX() < maxX ? position.getX() + 1 : position.getX();
    final int startY = position.getY() > 0 ? position.getY() - 1 : position.getY();
    final int endY = position.getY() < maxY ? position.getY() + 1 : position.getY();

    return IntStream.rangeClosed(startX, endX)
        .mapToObj(
            i ->
                IntStream.rangeClosed(startY, endY)
                    .filter(j -> i != position.getX() || j != position.getY())
                    .mapToObj(j -> tiles[i][j]))
        .flatMap(Function.identity());
  }

  /**
   * Get the <code>State</code> of the <code>Tile</code>.
   *
   * @return <code>State</code> of the <code>Tile</code>.
   */
  @NotNull
  public State getState() {
    return state;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tile tile = (Tile) o;
    return Objects.equals(position, tile.position) && state == tile.state;
  }

  @Override
  public int hashCode() {
    return Objects.hash(position, state);
  }

  @Override
  public String toString() {
    return "Tile{" + "position=" + position + ", state=" + state + '}';
  }

  /**
   * Copy the Tile and change the state.
   *
   * @param state <code>State</code> of the <code>Tile</code>
   * @return A new instance of the updated <code>Tile</code>.
   */
  @NotNull
  public abstract Tile copyWith(@NotNull State state);

  /** <code>State</code> of a tile based on the Minesweeper */
  public enum State {
    BLANK,
    FLAG,
    EXPOSED,
    HIT_MINE,
    EXPOSED_MINE
  }

  /** A <code>Tile</code> filled with a <code>Mine</code>. */
  public static final class Mine extends Tile {

    public Mine(Position position) {
      super(position);
    }

    public Mine(Position position, @NotNull State state) {
      super(position, state);
    }

    public Mine(int x, int y) {
      super(x, y);
    }

    private Mine(int x, int y, @NotNull State state) {
      super(x, y, state);
    }

    @Override
    public String toString() {
      return "Mine{" + "state=" + getState() + ", position=" + position + '}';
    }

    /**
     * {@inheritDoc}
     *
     * @param state <code>State</code> of the <code>Tile</code>
     * @return A new instance of the updated <code>Tile</code>.
     */
    @Override
    @NotNull
    public Mine copyWith(@NotNull State state) {
      return new Mine(position, state);
    }
  }

  /** An empty <code>Tile</code>. */
  public static final class Empty extends Tile {

    private final int adjacentMines;

    public Empty(Position position) {
      super(position);
      adjacentMines = 0;
    }

    public Empty(Position position, @NotNull State state, int adjacentMines) {
      super(position, state);
      this.adjacentMines = adjacentMines;
    }

    public Empty(int x, int y) {
      super(x, y);
      adjacentMines = 0;
    }

    private Empty(int x, int y, @NotNull State state, int adjacentMines) {
      super(x, y, state);
      this.adjacentMines = adjacentMines;
    }

    /**
     * Get the number of adjacent mines of the <code>Tile.Empty</code>.
     *
     * @return The adjacent mines.
     */
    public int getNeighborMinesCount() {
      return adjacentMines;
    }

    @Override
    public String toString() {
      return "Empty{"
          + "state="
          + getState()
          + ", position="
          + position
          + ", adjacentMines="
          + adjacentMines
          + '}';
    }

    /**
     * {@inheritDoc}
     *
     * @param state <code>State</code> of the <code>Tile</code>
     * @return A new instance of the updated <code>Tile</code>.
     */
    @Override
    @NotNull
    public Empty copyWith(@NotNull State state) {
      return new Empty(position, state, this.adjacentMines);
    }

    /**
     * Increase the number of adjacent mines.
     *
     * @return A new instance of the updated <code>Tile</code>.
     */
    @NotNull
    public Empty copyAndIncrementAdjacentMines() {
      return new Empty(position, this.getState(), this.adjacentMines + 1);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      if (!super.equals(o)) {
        return false;
      }
      final Empty empty = (Empty) o;
      return adjacentMines == empty.adjacentMines;
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), adjacentMines);
    }
  }
}
