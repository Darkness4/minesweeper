package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * A <code>Tile</code> that can be either <code>Empty</code> (which shows the number of adjacent
 * mines) or <code>Mine</code>.<br>
 * <br>
 * This class is immutable.
 */
public abstract class Tile implements Serializable {

  private final State _state;

  public final int x;
  public final int y;

  private Tile(int x, int y) {
    this(x, y, State.BLANK);
  }

  private Tile(int x, int y, @NotNull State state) {
    this.x = x;
    this.y = y;
    _state = state;
  }

  /**
   * Get the <code>State</code> of the <code>Tile</code>.
   *
   * @return <code>State</code> of the <code>Tile</code>.
   */
  @NotNull
  public State getState() {
    return _state;
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
    return x == tile.x && y == tile.y && _state == tile._state;
  }

  @Override
  public int hashCode() {
    return Objects.hash(_state, x, y);
  }

  @Override
  public String toString() {
    return "Tile{" + "_state=" + _state + ", x=" + x + ", y=" + y + '}';
  }

  /**
   * Update the <code>State</code> of the <code>Tile</code>
   *
   * @param state <code>State</code> of the <code>Tile</code>
   * @return A new instance of the updated <code>Tile</code>.
   */
  @NotNull
  public abstract Tile update(@NotNull State state);

  /** <code>State</code> of a tile based on the Minesweeper */
  public enum State {
    BLANK,
    FLAG,
    EXPOSED,
    HIT_MINE
  }

  /** A <code>Tile</code> filled with a <code>Mine</code>. */
  public static final class Mine extends Tile {
    public Mine(int x, int y) {
      super(x, y);
    }

    private Mine(int x, int y, @NonNull State state) {
      super(x, y, state);
    }

    @Override
    public String toString() {
      return "Tile{" + "_state=" + getState() + ", x=" + x + ", y=" + y + '}';
    }

    /**
     * {@inheritDoc}
     *
     * @param state <code>State</code> of the <code>Tile</code>
     * @return A new instance of the updated <code>Tile</code>.
     */
    @Override
    @NotNull
    public Mine update(@NotNull State state) {
      return new Mine(x, y, state);
    }
  }

  /** An empty <code>Tile</code>. */
  public static final class Empty extends Tile {

    private final int _adjacentMines;

    public Empty(int x, int y) {
      super(x, y);
      _adjacentMines = 0;
    }

    private Empty(int x, int y, @NotNull State state, int adjacentMines) {
      super(x, y, state);
      _adjacentMines = adjacentMines;
    }

    /**
     * Get the number of adjacent mines of the <code>Tile.Empty</code>.
     *
     * @return The adjacent mines.
     */
    public int getNeighborMinesCount() {
      return _adjacentMines;
    }

    @Override
    public String toString() {
      return "Empty{"
          + "_state="
          + getState()
          + ", x="
          + x
          + ", y="
          + y
          + ", _adjacentMines="
          + _adjacentMines
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
    public Empty update(@NotNull State state) {
      return new Empty(x, y, state, this._adjacentMines);
    }

    /**
     * Increase the number of adjacent mines.
     *
     * @return A new instance of the updated <code>Tile</code>.
     */
    @NotNull
    public Empty incrementAdjacentMinesAndGet() {
      return new Empty(x, y, this.getState(), this._adjacentMines + 1);
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
      return _adjacentMines == empty._adjacentMines;
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), _adjacentMines);
    }
  }
}
