package marc.nguyen.minesweeper.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    assertThrows(
        IllegalArgumentException.class,
        () -> new Minefield(0, 0));
  }

  @Test
  void PlaceMines_3Mines_ShouldPlaceTheSpecifiedNumberOfMines() {
    // Act
    minefield.placeMines(3);
    var result = minefield.toString();

    // Assert
    assertEquals(result.chars().filter(c -> c == 'X').count(), 3);
  }

  @Test
  void PlaceMines_1Mine_AdjacentTileShouldBeEqualsToOne() {
    // Act
    minefield.placeMines(1);
    var result = minefield.toString();

    // Assert
    assertEquals(result.chars().filter(c -> c == '1').count(), 3);
  }

  @Test
  void Clear_ShouldClearTheMinefield() {
    // Arrange
    minefield.placeMines(3);
    var arrange = minefield.toString();
    assert arrange.chars().filter(c -> c == 'X').count() == 3;

    // Act
    minefield.clear();
    var result = minefield.toString();

    // Assert
    assertEquals(result.chars().filter(c -> c == 'X').count(), 0);
  }
}
