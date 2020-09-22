package marc.nguyen.minesweeper.common.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class MinefieldTest {

  static int LENGTH = 2;
  static int HEIGHT = 2;
  Minefield minefield;

  @Test
  void Constructor_IllegalSize0x0() {
    // Act
    final Executable executable = () -> new Minefield(0, 0, false);

    // Assert
    assertThrows(IllegalArgumentException.class, executable);
  }

  @Test
  void PlaceMines_ShouldPlaceTheSpecifiedNumberOfMines() {
    // Act
    minefield = new Minefield(LENGTH, HEIGHT, 3, false);

    // Assert
    assertEquals(minefield.getMinesOnField(), 3);
  }

  @Test
  void PlaceMines_ExcessOfMines() {
    // Act
    final Executable executable = () -> minefield = new Minefield(LENGTH, HEIGHT, 5, false);

    // Assert
    assertThrows(IllegalArgumentException.class, executable);
  }

  @AfterEach
  void dispose() {
    minefield = null;
  }
}
