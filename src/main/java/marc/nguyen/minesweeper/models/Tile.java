package marc.nguyen.minesweeper.models;

import java.util.Objects;

/**
 * A <code>Tile</code> that can be either <code>Empty</code> (which shows the number of adjacent
 * mines) or <code>Mine</code>.
 */
public abstract class Tile {

  private final State _state;

  private Tile() {
    _state = State.BLANK;
  }

  private Tile(State state) {
    _state = state;
  }

  /**
   * Get the <code>State</code> of the <code>Tile</code>.
   *
   * @return <code>State</code> of the <code>Tile</code>.
   */
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
    return _state == tile._state;
  }

  @Override
  public int hashCode() {
    return Objects.hash(_state);
  }

  /**
   * Update the <code>State</code> of the <code>Tile</code>
   *
   * @param state <code>State</code> of the <code>Tile</code>
   * @return A new instance of the updated <code>Tile</code>.
   */
  public abstract Tile update(State state);

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
    public Mine() {
      super();
    }

    private Mine(State state) {
      super(state);
    }

    @Override
    public String toString() {
      return "X";
    }

    /**
     * {@inheritDoc}
     *
     * @param state <code>State</code> of the <code>Tile</code>
     * @return A new instance of the updated <code>Tile</code>.
     */
    @Override
    public Mine update(State state) {
      return new Mine(state);
    }
  }

  /** An empty <code>Tile</code>. */
  public static final class Empty extends Tile {

    private final int _adjacentMines;

    public Empty() {
      super();
      _adjacentMines = 0;
    }

    private Empty(State state, int adjacentMines) {
      super(state);
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
      return Integer.toString(_adjacentMines);
    }

    /**
     * {@inheritDoc}
     *
     * @param state <code>State</code> of the <code>Tile</code>
     * @return A new instance of the updated <code>Tile</code>.
     */
    @Override
    public Empty update(State state) {
      return new Empty(state, this._adjacentMines);
    }

    /**
     * Increase the number of adjacent mines.
     *
     * @return A new instance of the updated <code>Tile</code>.
     */
    public Empty incrementAdjacentMinesAndGet() {
      return new Empty(this.getState(), this._adjacentMines + 1);
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
      Empty empty = (Empty) o;
      return _adjacentMines == empty._adjacentMines;
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), _adjacentMines);
    }
  }
}
