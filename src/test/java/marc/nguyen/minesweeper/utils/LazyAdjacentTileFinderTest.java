package marc.nguyen.minesweeper.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import marc.nguyen.minesweeper.models.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LazyAdjacentTileFinderTest {

  static int LENGTH = 3;
  static int HEIGHT = 3;
  LazyAdjacentTileFinder lazyAdjacentTileFinder;
  Tile[][] mockTiles;

  @BeforeEach
  void setUp() {
    mockTiles = new Tile[LENGTH][HEIGHT];
    for (Tile[] column : mockTiles) {
      Arrays.parallelSetAll(column, index -> new Tile.Empty());
    }
    lazyAdjacentTileFinder = new LazyAdjacentTileFinder(mockTiles);
  }

  @Test
  void Execute_Center_ShouldFindAllAdjacentTiles() {
    // Arrange
    final Set<Tile> expected =
        Set.of(
            mockTiles[0][0],
            mockTiles[0][1],
            mockTiles[0][2],
            mockTiles[1][0],
            mockTiles[1][2],
            mockTiles[2][0],
            mockTiles[2][1],
            mockTiles[2][2]);

    // Act
    var result = lazyAdjacentTileFinder.execute(1, 1).collect(Collectors.toUnmodifiableSet());

    // Assert
    assertEquals(result, expected);
  }

  @Test
  void Execute_Corner_ShouldFindAllAdjacentTiles() {
    // Arrange
    final Set<Tile> expected = Set.of(mockTiles[0][1], mockTiles[1][1], mockTiles[1][0]);

    // Act
    var result = lazyAdjacentTileFinder.execute(0, 0).collect(Collectors.toUnmodifiableSet());

    // Assert
    assertEquals(result, expected);
  }
}
