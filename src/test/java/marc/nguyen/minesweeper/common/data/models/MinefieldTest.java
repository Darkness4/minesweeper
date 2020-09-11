package marc.nguyen.minesweeper.common.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class MinefieldTest {

  static int LENGTH = 2;
  static int HEIGHT = 2;
  Minefield minefield;

  @BeforeEach
  void init() {
    minefield = new Minefield(LENGTH, HEIGHT);
  }

  @Test
  void Constructor_IllegalSize0x0() {
    // Act
    final Executable executable = () -> new Minefield(0, 0);

    // Assert
    assertThrows(IllegalArgumentException.class, executable);
  }

  @Test
  void PlaceMines_3Mines_ShouldPlaceTheSpecifiedNumberOfMines() {
    // Act
    minefield.placeMines(3);

    // Assert
    assertEquals(minefield.countMinesOnField(), 3);
  }

  @Test
  void PlaceMines_1Mine_AdjacentTileShouldBeEqualsToOne() {
    // Act
    minefield.placeMines(1);

    // Assert
    assertEquals(4 - minefield.countMinesOnField(), 3);
  }

  @Test
  void PlaceMines_ExcessOfMines() {
    // Act
    final Executable executable = () -> minefield.placeMines(5);

    // Assert
    assertThrows(IllegalArgumentException.class, executable);
  }
}
