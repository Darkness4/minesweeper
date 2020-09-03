package marc.nguyen.minesweeper.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import marc.nguyen.minesweeper.models.Tile;

/**
 * <code>LazyAdjacentTileFinder</code> looks for the adjacent tiles lazily for reactive programming
 * purpose.
 */
public class LazyAdjacentTileFinder {

  private final Tile[][] _tiles;

  public LazyAdjacentTileFinder(Tile[][] tiles) {
    _tiles = tiles;
  }

  /**
   * Look for adjacent tiles based on coordinates.
   *
   * @param x X coordinate (<code>tiles[x][y]</code>)
   * @param y Y coordinate (<code>tiles[x][y]</code>)
   * @return A <code>Stream</code> of Tile.
   */
  public Stream<Tile> execute(int x, int y) {
    final Set<Coordinates> coordinatesSet = new HashSet<>();
    final int maxX = _tiles.length - 1;
    final int maxY = _tiles[1].length - 1;

    // Look for correct coordinates sequentially.
    for (int dx = (x > 0 ? -1 : 0); dx <= (x < maxX ? 1 : 0); dx++) {
      for (int dy = (y > 0 ? -1 : 0); dy <= (y < maxY ? 1 : 0); dy++) {
        if (dx != 0 || dy != 0) {
          coordinatesSet.add(new Coordinates(x + dx, y + dy));
        }
      }
    }

    // Returns lazily the tiles.
    return coordinatesSet.stream()
        .parallel()
        .map(coordinates -> _tiles[coordinates.x][coordinates.y]);
  }

  static class Coordinates {

    final int x;
    final int y;

    public Coordinates(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
}
