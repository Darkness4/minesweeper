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
  public final boolean isSinglePlayer;
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
    clear();
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

  /** Clear everything, and replace mines */
  public synchronized void rebuild() {
    clear();
    placeMines(minesOnField);
    System.out.println("MINEFIELD HAS BEEN REBUILT.");
  }

  private void clear() {
    for (int i = 0; i < tiles.length; i++) {
      final int finalI = i;
      Arrays.parallelSetAll(tiles[i], j -> new Tile.Empty(finalI, j));
    }
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
  private synchronized void placeMines(long mines) {
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
      tiles[tile.getX()][tile.getY()] = tile.copyWith(Tile.State.FLAG);
    } else if (state == Tile.State.FLAG) {
      tiles[tile.getX()][tile.getY()] = tile.copyWith(Tile.State.BLANK);
    }
  }

  /**
   * Expose a tile if Empty. Else, change to HIT_MINE.
   *
   * @param position Position of the tile to be exposed.
   * @return Number of tiles discovered.
   */
  public int expose(Position position) {
    int numberOfTileDiscovered = 0;

    final var tile = tiles[position.getX()][position.getY()];
    if (tile instanceof Tile.Empty) {
      if (isSinglePlayer) {
        numberOfTileDiscovered = treeSearchEmptyTile((Tile.Empty) tile);
      } else {
        if (tile.getState() != Tile.State.EXPOSED) {
          synchronized (tiles[tile.getX()][tile.getY()]) {
            tiles[tile.getX()][tile.getY()] = tile.copyWith(Tile.State.EXPOSED);
            numberOfTileDiscovered = 1;
          }
        }
      }
    } else if (tile instanceof Tile.Mine) {
      synchronized (tiles[tile.getX()][tile.getY()]) {
        tiles[tile.getX()][tile.getY()] = tile.copyWith(Tile.State.HIT_MINE);
        numberOfTileDiscovered = 1;
      }
    }

    return numberOfTileDiscovered;
  }

  public synchronized void exposeAllMines() {
    Arrays.stream(tiles)
        .parallel()
        .forEach(
            column -> {
              for (int i = 0; i < column.length; i++) {
                if (column[i] instanceof Tile.Mine && column[i].getState() != State.HIT_MINE) {
                  column[i] = column[i].copyWith(State.EXPOSED_MINE);
                }
              }
            });
  }

  /**
   * Use BFS Tree Search Algorithm to reveal tiles.
   *
   * @param tile Find neighbors of that tile.
   * @return Number of tile discovered.
   */
  private synchronized int treeSearchEmptyTile(@NotNull Tile.Empty tile) {
    int numberOfTileDiscovered = 0;
    final Queue<Tile.Empty> queue = new LinkedList<>();

    queue.add(tile);

    while (!queue.isEmpty()) {
      final var head = queue.poll();

      if (tiles[head.getX()][head.getY()].getState() != Tile.State.EXPOSED) {
        tiles[head.getX()][head.getY()] = head.copyWith(Tile.State.EXPOSED);
        numberOfTileDiscovered++;

        if (head.getNeighborMinesCount() == 0) {
          head.getNeighborsTilesIn(tiles)
              .filter(t -> t.getState() != Tile.State.EXPOSED)
              .forEach(neighbor -> queue.add((Tile.Empty) neighbor));
        }
      }
    }
    return numberOfTileDiscovered;
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
   * Get a tile from x and y coordinates.
   *
   * @param position Position.
   * @return A <code>Tile</code> reference.
   */
  @NotNull
  public Tile get(Position position) {
    return tiles[position.getX()][position.getY()];
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

  /**
   * Check Winning and Losing condition.
   *
   * @return If it has ended.
   */
  public boolean hasEnded() {
    final long exposedTiles =
        Arrays.stream(tiles)
            .flatMap(Arrays::stream)
            .parallel()
            .filter(tile -> tile.getState() == State.EXPOSED && tile instanceof Tile.Empty)
            .count();
    final var winningCondition = getHeight() * getHeight() - getMinesOnField() == exposedTiles;
    if (isSinglePlayer) {
      final long minesHit =
          Arrays.stream(tiles)
              .flatMap(Arrays::stream)
              .parallel()
              .filter(tile -> tile.getState() == State.HIT_MINE)
              .count();
      final var losingCondition = minesHit >= 1;

      return losingCondition || winningCondition;
    } else {
      return winningCondition;
    }
  }

  private synchronized void incrementAdjacentCounters(@NotNull Tile tile) {
    tile.getNeighborsTilesIn(tiles)
        .parallel()
        .forEach(
            neighbor -> {
              if (neighbor instanceof Tile.Empty) {
                tiles[neighbor.getX()][neighbor.getY()] =
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
