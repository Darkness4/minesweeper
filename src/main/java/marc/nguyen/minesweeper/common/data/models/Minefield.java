package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
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
  public Minefield(@NotNull Level level) {
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
   * @param tile Tile to be flagged.
   */
  public synchronized void flag(Tile tile) {
    final var state = tile.getState();
    if (state == Tile.State.BLANK) {
      _tiles[tile.x][tile.y] = tile.copyWith(Tile.State.FLAG);
    } else if (state == Tile.State.FLAG) {
      _tiles[tile.x][tile.y] = tile.copyWith(Tile.State.BLANK);
    }
  }

  /**
   * Expose a tile if Empty. Else, change to HIT_MINE.
   *
   * @param tile Tile to be exposed.
   */
  public void expose(Tile tile) {
    if (tile instanceof Tile.Empty) {
      // TODO: Apply here tree-search
      // treeSearchEmptyTile((Tile.Empty) tile);
      if (tile.getState() != Tile.State.EXPOSED) {
        synchronized (_tiles[tile.x][tile.y]) {
          _tiles[tile.x][tile.y] = tile.copyWith(Tile.State.EXPOSED);
        }
      }
    } else if (tile instanceof Tile.Mine) {
      synchronized (_tiles[tile.x][tile.y]) {
        _tiles[tile.x][tile.y] = tile.copyWith(Tile.State.HIT_MINE);
      }
      // TODO: Minus score -1
    }
  }

  /**
   * Use BFS Tree Search Algorithm to reveal tiles.
   *
   * @param tile Find neighbors of that tile.
   */
  private void treeSearchEmptyTile(@NotNull Tile.Empty tile) {
    final Queue<Tile.Empty> queue = new LinkedList<>();

    queue.add(tile);

    while (!queue.isEmpty()) {
      final var head = queue.poll();

      if (head.getState() != Tile.State.EXPOSED) {
        synchronized (_tiles[head.x][head.y]) {
          _tiles[head.x][head.y] = head.copyWith(Tile.State.EXPOSED);
        }

        if (head.getNeighborMinesCount() == 0) {
          head.getNeighborsTilesIn(_tiles).forEach(neighbor -> queue.add((Tile.Empty) neighbor));
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
        .flatMap(Arrays::stream)
        .parallel()
        .filter(tile -> tile instanceof Tile.Mine)
        .count();
  }

  /** @return Number of flags and mine exposed. */
  public long countFlagsAndVisibleBombsOnField() {
    return Arrays.stream(_tiles)
        .flatMap(Arrays::stream)
        .parallel()
        .filter(
            tile -> tile.getState() == Tile.State.FLAG || tile.getState() == Tile.State.HIT_MINE)
        .count();
  }

  /** @return Number of correct guesses and mine exposed. */
  public long countCorrectGuessesAndVisibleBombs() {
    return Arrays.stream(_tiles)
        .flatMap(Arrays::stream)
        .parallel()
        .filter(
            tile ->
                (tile.getState() == Tile.State.FLAG && tile instanceof Tile.Mine)
                    || tile.getState() == Tile.State.HIT_MINE)
        .count();
  }

  private synchronized void incrementAdjacentCounters(@NotNull Tile tile) {
    tile.getNeighborsTilesIn(_tiles)
        .parallel()
        .forEach(
            neighbor -> {
              if (neighbor instanceof Tile.Empty) {
                _tiles[neighbor.x][neighbor.y] =
                    ((Tile.Empty) neighbor).copyAndIncrementAdjacentMines();
              }
            });
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
