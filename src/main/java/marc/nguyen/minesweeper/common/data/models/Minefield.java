package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import marc.nguyen.minesweeper.common.data.models.Tile.State;
import org.jetbrains.annotations.NotNull;

/**
 * A Minefield.
 *
 * <p>Initialize it with <code>Minefield::placeMines()</code>.
 */
public class Minefield implements Serializable {

  private final Tile[][] tiles;
  private final boolean isSinglePlayer;
  private long minesOnField = 0;

  /**
   * An empty minefield based on length and height.
   *
   * @param length Length of the minefield.
   * @param height Height of the minefield.
   * @param isSinglePlayer Game is single player. This will change the discovering algorithm to a
   *     tree search if enabled.
   */
  public Minefield(int length, int height, boolean isSinglePlayer) {
    if (length <= 0 || height <= 0) {
      throw new IllegalArgumentException("Length and Height should be > 0");
    }
    this.isSinglePlayer = isSinglePlayer;
    tiles = new Tile[length][height];
    for (int i = 0; i < tiles.length; i++) {
      final int finalI = i;
      Arrays.parallelSetAll(tiles[i], j -> new Tile.Empty(finalI, j));
    }
  }

  /**
   * A minefield based on length, height and the number of mines.
   *
   * @param length Length of the minefield.
   * @param height Height of the minefield.
   * @param mines Number of mines.
   * @param isSinglePlayer Game is single player. This will change the discovering algorithm to a
   *     tree search if enabled.
   */
  public Minefield(int length, int height, int mines, boolean isSinglePlayer) {
    this(length, height, isSinglePlayer);
    placeMines(mines);
  }

  /**
   * Generate a Minefield based on the difficulty.
   *
   * @param level Level of difficulty.
   * @param isSinglePlayer Game is single player. This will change the discovering algorithm to a
   *     tree search if enabled.
   */
  public Minefield(@NotNull Level level, boolean isSinglePlayer) {
    this(level.length, level.height, isSinglePlayer);
    placeMines(level.mines);
  }

  /**
   * Get the number of mines on the field.
   *
   * @return Number of mines on the field.
   */
  public long getMinesOnField() {
    return minesOnField;
  }

  /**
   * Place the mines on the minefield.
   *
   * @param mines Number of mines to be placed.
   */
  private synchronized void placeMines(int mines) {
    if (mines >= tiles.length * tiles[0].length) {
      throw new IllegalArgumentException(
          "Game is unplayable if mines >= length * height. Please set a lower number of mines.");
    }
    final var randomizer = new Random();

    minesOnField = countMinesOnField();
    while (minesOnField < mines) {
      final int x = randomizer.nextInt(tiles.length);
      final int y = randomizer.nextInt(tiles[0].length);

      if (tiles[x][y] instanceof Tile.Empty) {
        tiles[x][y] = new Tile.Mine(x, y);
        minesOnField++;
        incrementAdjacentCounters(tiles[x][y]);
      }
    }
  }

  /** @return Length of the Minefield. */
  public int getLength() {
    return tiles.length;
  }

  /** @return Height of the Minefield. */
  public int getHeight() {
    return tiles[0].length;
  }

  /**
   * Switch the state of a tile to FLAG or BLANK.
   *
   * @param tile Tile to be flagged.
   */
  public synchronized void flag(Tile tile) {
    final var state = tile.getState();
    if (state == Tile.State.BLANK) {
      tiles[tile.x][tile.y] = tile.copyWith(Tile.State.FLAG);
    } else if (state == Tile.State.FLAG) {
      tiles[tile.x][tile.y] = tile.copyWith(Tile.State.BLANK);
    }
  }

  /**
   * Expose a tile if Empty. Else, change to HIT_MINE.
   *
   * @param tile Tile to be exposed.
   */
  public void expose(Tile tile) {
    if (tile instanceof Tile.Empty) {
      if (isSinglePlayer) {
        treeSearchEmptyTile((Tile.Empty) tile);
      } else {
        if (tile.getState() != Tile.State.EXPOSED) {
          synchronized (tiles[tile.x][tile.y]) {
            tiles[tile.x][tile.y] = tile.copyWith(Tile.State.EXPOSED);
          }
        }
      }
    } else if (tile instanceof Tile.Mine) {
      synchronized (tiles[tile.x][tile.y]) {
        tiles[tile.x][tile.y] = tile.copyWith(Tile.State.HIT_MINE);
      }
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
        synchronized (tiles[head.x][head.y]) {
          tiles[head.x][head.y] = head.copyWith(Tile.State.EXPOSED);
        }

        if (head.getNeighborMinesCount() == 0) {
          head.getNeighborsTilesIn(tiles).forEach(neighbor -> queue.add((Tile.Empty) neighbor));
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
    return tiles[x][y];
  }

  /**
   * Calculate the number of mines.
   *
   * <p>Please use `getMinesOnField()`
   *
   * @return Number of Mines on field.
   */
  private long countMinesOnField() {
    return Arrays.stream(tiles)
        .flatMap(Arrays::stream)
        .parallel()
        .filter(tile -> tile instanceof Tile.Mine)
        .count();
  }

  /** @return Number of flags and mine exposed. */
  public long countFlagsAndVisibleBombsOnField() {
    return Arrays.stream(tiles)
        .flatMap(Arrays::stream)
        .parallel()
        .filter(
            tile -> tile.getState() == Tile.State.FLAG || tile.getState() == Tile.State.HIT_MINE)
        .count();
  }

  public boolean hasEnded() {
    final long exposedTiles =
        Arrays.stream(tiles)
            .flatMap(Arrays::stream)
            .parallel()
            .filter(tile -> tile.getState() == State.EXPOSED && tile instanceof Tile.Empty)
            .count();
    return getHeight() * getHeight() - getMinesOnField() == exposedTiles;
  }

  private synchronized void incrementAdjacentCounters(@NotNull Tile tile) {
    tile.getNeighborsTilesIn(tiles)
        .parallel()
        .forEach(
            neighbor -> {
              if (neighbor instanceof Tile.Empty) {
                tiles[neighbor.x][neighbor.y] =
                    ((Tile.Empty) neighbor).copyAndIncrementAdjacentMines();
              }
            });
  }

  @Override
  public String toString() {
    return "Minefield{\n_tiles="
        + Arrays.stream(tiles).map(Arrays::toString).collect(Collectors.joining(",\n  "))
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
    return Arrays.equals(tiles, minefield.tiles);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(tiles);
  }
}
