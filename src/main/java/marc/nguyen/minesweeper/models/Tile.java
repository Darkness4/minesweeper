package marc.nguyen.minesweeper.models;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A <code>Tile</code> that can be either <code>Empty</code> (which shows the number of adjacent
 * mines) or <code>Mine</code>.
 */
public abstract class Tile {

  private final AtomicReference<State> _state = new AtomicReference<>(State.BLANK);

  private Tile() {}

  /**
   * Get the <code>State</code> of the <code>Tile</code>.
   *
   * @return <code>State</code> of the <code>Tile</code>.
   */
  public State getState() {
    return _state.get();
  }

  /**
   * Set the <code>State</code> of the <code>Tile</code>.
   *
   * @param state <code>State</code> of the <code>Tile</code>.
   */
  public void setState(State state) {
    if (state == null) {
      throw new IllegalArgumentException();
    }

    _state.set(state);
  }

  /** <code>State</code> of a tile based on the Minesweeper */
  public enum State {
    BLANK,
    FLAG,
    EXPOSED,
    HIT_MINE,
    WRONG_MINE
  }

  /** A <code>Tile</code> filled with a <code>Mine</code>. */
  public static final class Mine extends Tile {

    @Override
    public String toString() {
      return "X";
    }
  }

  /** An empty <code>Tile</code>. */
  public static final class Empty extends Tile {

    private final AtomicInteger _adjacentMines = new AtomicInteger(0);

    /**
     * Get the number of adjacent mines of the <code>Tile.Empty</code>.
     *
     * @return The adjacent mines.
     */
    public int getNeighborMinesCount() {
      return _adjacentMines.get();
    }

    /** Increment the number of adjacent mines. */
    public void incrementAdjacentMines() {
      _adjacentMines.getAndIncrement();
    }

    /** Reset the number of adjacent mines to 0. */
    public void reset() {
      _adjacentMines.set(0);
    }

    @Override
    public String toString() {
      return _adjacentMines.toString();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Empty empty = (Empty) o;
      return _adjacentMines.equals(empty._adjacentMines);
    }

    @Override
    public int hashCode() {
      return Objects.hash(_adjacentMines);
    }
  }
}
