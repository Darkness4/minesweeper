package marc.nguyen.minesweeper.models;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A Minefield.
 *
 * <p>Initialize it with <code>Minefield::placeMines()</code>.
 *
 * <p>Clear it with <code>Minefield::clear()</code>.
 */
public class Minefield {

  final Tile[][] _tiles;

  /**
   * A minefield based on length and height.
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

  /** Place the mines on the minefield. */
  public synchronized void placeMines(int mines) {
    if (mines >= _tiles.length * _tiles[0].length) {
      throw new IllegalArgumentException(
          "Game is unplayable if mines >= length * height. Please set a lower number of mines.");
    }
    final var randomizer = new Random();

    var minesOnField =
        Arrays.stream(_tiles)
            .flatMap(Arrays::stream)
            .filter(tile -> tile instanceof Tile.Mine)
            .count();
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

  /** Clear the mines on the minefield. */
  public synchronized void clear() {
    for (Tile[] column : _tiles) {
      Arrays.parallelSetAll(column, index -> new Tile.Empty());
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
  public Tile get(int x, int y) {
    return _tiles[x][y];
  }

  private void incrementAdjacentCounters(int x, int y) {
    final int maxX = _tiles.length - 1;
    final int maxY = _tiles[0].length - 1;

    for (int dx = (x > 0 ? -1 : 0); dx <= (x < maxX ? 1 : 0); dx++) {
      for (int dy = (y > 0 ? -1 : 0); dy <= (y < maxY ? 1 : 0); dy++) {
        if (dx != 0 || dy != 0) {
          var tile = _tiles[x + dx][y + dy];
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
    Minefield minefield = (Minefield) o;
    return Arrays.equals(_tiles, minefield._tiles);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(_tiles);
  }
}
