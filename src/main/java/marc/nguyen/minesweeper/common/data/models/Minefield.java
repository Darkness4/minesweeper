package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import marc.nguyen.minesweeper.common.data.models.Tile.State;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * A Minefield.
 *
 * <p>Initialize it with <code>Minefield::placeMines()</code>.
 *
 * <p>Clear it with <code>Minefield::clear()</code>.
 */
public class Minefield implements Serializable {

  final Tile[][] _tiles;

  /**
   * An empty minefield based on length and height.
   *
   * @param length Length of the minefield.
   * @param height Height of the minefield.
   */
  public Minefield(int length, int height) {
    if (length <= 0 || height <= 0) {
      throw new IllegalArgumentException("Length and Height should be > 0");
    }
    _tiles = new Tile[length][height];
    clear();
  }

  /**
   * A minefield based on length, height and the number of mines.
   *
   * @param length Length of the minefield.
   * @param height Height of the minefield.
   * @param mines Number of mines.
   */
  public Minefield(int length, int height, int mines) {
    this(length, height);
    placeMines(mines);
  }

  /**
   * Generate a Minefield based on the difficulty.
   *
   * @param level Level of difficulty.
   */
  public Minefield(@NonNull Level level) {
    this(level.length, level.height);
    placeMines(level.mines);
  }

  /**
   * Place the mines on the minefield.
   *
   * @param mines Number of mines to be placed.
   */
  public void placeMines(int mines) {
    if (mines >= _tiles.length * _tiles[0].length) {
      throw new IllegalArgumentException(
          "Game is unplayable if mines >= length * height. Please set a lower number of mines.");
    }
    final var randomizer = new Random();

    var minesOnField = countMinesOnField();
    while (minesOnField < mines) {
      final int x = randomizer.nextInt(_tiles.length);
      final int y = randomizer.nextInt(_tiles[0].length);

      if (_tiles[x][y] instanceof Tile.Empty) {
        _tiles[x][y] = new Tile.Mine();
        minesOnField++;
        incrementAdjacentCounters(x, y);
      }
    }
  }

  /** @return Length of the Minefield. */
  public int getLength() {
    return _tiles.length;
  }

  /** @return Height of the Minefield. */
  public int getHeight() {
    return _tiles[0].length;
  }

  /** Clear the mines on the minefield. */
  public void clear() {
    for (Tile[] column : _tiles) {
      Arrays.parallelSetAll(column, index -> new Tile.Empty());
    }
  }

  public void flag(int x, int y) {
    final var state = _tiles[x][y].getState();
    if (state == State.BLANK) {
      _tiles[x][y] = _tiles[x][y].update(State.FLAG);
    } else if (state == State.FLAG) {
      _tiles[x][y] = _tiles[x][y].update(State.BLANK);
    }
  }

  public void expose(int x, int y) {
    final var tile = _tiles[x][y];
    final var state = tile.getState();
    if (tile instanceof Tile.Empty) {
      _tiles[x][y] = _tiles[x][y].update(State.EXPOSED);
    } else if (tile instanceof Tile.Mine) {
      _tiles[x][y] = _tiles[x][y].update(State.HIT_MINE);
      // TODO: Export everything
    }
  }

  /** Show the minefield in stdout. */
  public void affText() {
    System.out.println(this);
  }

  /**
   * Get a tile from x and y coordinates.
   *
   * @param x X coordinate.
   * @param y Y coordinate.
   * @return A <code>Tile</code> reference.
   */
  @NotNull
  public Tile get(int x, int y) {
    return _tiles[x][y];
  }

  /** @return Number of Mines on field. */
  public long countMinesOnField() {
    return Arrays.stream(_tiles)
        .parallel()
        .flatMap(Arrays::stream)
        .filter(tile -> tile instanceof Tile.Mine)
        .count();
  }

  private void incrementAdjacentCounters(int x, int y) {
    final int maxX = _tiles.length - 1;
    final int maxY = _tiles[0].length - 1;

    final int dxStart = x > 0 ? -1 : 0;
    final int dxEnd = x < maxX ? 1 : 0;

    final int dyStart = y > 0 ? -1 : 0;
    final int dyEnd = y < maxY ? 1 : 0;

    for (int dx = dxStart; dx <= dxEnd; dx++) {
      for (int dy = dyStart; dy <= dyEnd; dy++) {
        if (dx != 0 || dy != 0) {
          final var tile = _tiles[x + dx][y + dy];
          if (tile instanceof Tile.Empty) {
            _tiles[x + dx][y + dy] = ((Tile.Empty) tile).incrementAdjacentMinesAndGet();
          }
        }
      }
    }
  }

  @Override
  public String toString() {
    return "Minefield{\n_tiles="
        + Arrays.stream(_tiles).map(Arrays::toString).collect(Collectors.joining(",\n  "))
        + "\n}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Minefield minefield = (Minefield) o;
    return Arrays.equals(_tiles, minefield._tiles);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(_tiles);
  }
}
