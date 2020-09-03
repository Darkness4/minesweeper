package marc.nguyen.minesweeper.models;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import marc.nguyen.minesweeper.utils.LazyAdjacentTileFinder;

/**
 * A Minefield.
 *
 * <p>Initialize it with <code>Minefield::placeMines()</code>.
 *
 * <p>Clear it with <code>Minefield::clear()</code>.
 */
public class Minefield {

  static final int MINES = 30;
  final Tile[][] _tiles;

  /**
   * A minefield based on length and height.
   *
   * @param length Length of the minefield.
   * @param height Height of the minefield.
   */
  public Minefield(int length, int height) {
    _tiles = new Tile[length][height];
    clear();
  }

  /** Place the mines on the minefield. */
  public synchronized void placeMines() {
    final var randomizer = new Random();

    var minesOnField = 0;
    while (minesOnField < MINES) {
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

  private void incrementAdjacentCounters(int x, int y) {
    getAdjacentTiles(x, y)
        .forEach(
            tile -> {
              if (tile instanceof Tile.Empty) {
                ((Tile.Empty) tile).incrementAdjacentMines();
              }
            });
  }

  private Stream<Tile> getAdjacentTiles(int x, int y) {
    final var finder = new LazyAdjacentTileFinder(_tiles);

    return finder.execute(x, y);
  }

  @Override
  public String toString() {
    return "Minefield{_places=\n"
        + Arrays.stream(_tiles)
            .map(column -> Arrays.toString(column) + '\n')
            .collect(Collectors.joining())
        + '}';
  }
}
