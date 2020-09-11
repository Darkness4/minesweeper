package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * A Minefield.
 *
 * <p>Initialize it with <code>Minefield::placeMines()</code>.
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
    for (int i = 0; i < _tiles.length; i++) {
      final int finalI = i;
      Arrays.parallelSetAll(_tiles[i], j -> new Tile.Empty(finalI, j));
    }
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
  public synchronized void placeMines(int mines) {
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
        _tiles[x][y] = new Tile.Mine(x, y);
        minesOnField++;
        incrementAdjacentCounters(_tiles[x][y]);
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

  /**
   * Switch the state of a tile to FLAG or BLANK.
   *
   * @param x X coordinates.
   * @param y Y coordinates.
   */
  public synchronized void flag(int x, int y) {
    final var state = _tiles[x][y].getState();
    if (state == Tile.State.BLANK) {
      _tiles[x][y] = _tiles[x][y].update(Tile.State.FLAG);
    } else if (state == Tile.State.FLAG) {
      _tiles[x][y] = _tiles[x][y].update(Tile.State.BLANK);
    }
  }

  /**
   * Expose a tile if Empty. Else, change to HIT_MINE.
   *
   * @param x X coordinates.
   * @param y Y coordinates.
   */
  public synchronized void expose(int x, int y) {
    final var tile = _tiles[x][y];
    if (tile instanceof Tile.Empty) {
      treeSearchEmptyTile((Tile.Empty) tile);
    } else if (tile instanceof Tile.Mine) {
      _tiles[x][y] = _tiles[x][y].update(Tile.State.HIT_MINE);
      // TODO: Minus score -1
    }
  }

  /**
   * Use BFS Tree Search Algorithm to reveal tiles.
   *
   * @param tile Find neighbors of that tile.
   */
  private synchronized void treeSearchEmptyTile(Tile.Empty tile) {
    final Queue<Tile.Empty> queue = new LinkedList<>();

    queue.add(tile);

    while (!queue.isEmpty()) {
      final var head = queue.poll();

      if (head.getState() != Tile.State.EXPOSED) {
        _tiles[head.x][head.y] = head.update(Tile.State.EXPOSED);

        if (head.getNeighborMinesCount() == 0) {
          getNeighborsIndicesOf(head).forEach(neighbor -> queue.add((Tile.Empty) neighbor));
        }
      }
    }
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

  /** @return Number of flags and mine exposed. */
  public long countFlagsAndVisibleBombsOnField() {
    return Arrays.stream(_tiles)
        .parallel()
        .flatMap(Arrays::stream)
        .filter(
            tile -> tile.getState() == Tile.State.FLAG || tile.getState() == Tile.State.HIT_MINE)
        .count();
  }

  private synchronized void incrementAdjacentCounters(Tile tile) {
    getNeighborsIndicesOf(tile)
        .parallel()
        .forEach(
            neighbor -> {
              if (neighbor instanceof Tile.Empty) {
                _tiles[neighbor.x][neighbor.y] =
                    ((Tile.Empty) neighbor).incrementAdjacentMinesAndGet();
              }
            });
  }

  private Stream<Tile> getNeighborsIndicesOf(Tile tile) {
    final int maxX = _tiles.length - 1;
    final int maxY = _tiles[0].length - 1;

    final int startX = tile.x > 0 ? tile.x - 1 : tile.x;
    final int endX = tile.x < maxX ? tile.x + 1 : tile.x;
    final int startY = tile.y > 0 ? tile.y - 1 : tile.y;
    final int endY = tile.y < maxY ? tile.y + 1 : tile.y;

    return IntStream.rangeClosed(startX, endX)
        .mapToObj(
            i ->
                IntStream.rangeClosed(startY, endY)
                    .filter(j -> i != tile.x || j != tile.y)
                    .mapToObj(j -> _tiles[i][j]))
        .flatMap(Function.identity());
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
