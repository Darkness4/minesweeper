package marc.nguyen.minesweeper.client.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.InetAddress;
import java.util.List;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.common.data.models.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettingsDbInMemoryTest {

  SettingsDb db;

  @BeforeEach
  void setUp() {
    db = new SettingsDbInMemory();
  }

  @Test
  void delete() {
    // Arrange
    db.insert(new Settings("name", InetAddress.getLoopbackAddress(), 10, 10, 10, Level.EASY));
    // Act
    db.delete("name");
    // Assert
    assertNull(db.find("name"));
  }

  @Test
  void insert() {
    // Arrange
    final var tSettings =
        new Settings("name", InetAddress.getLoopbackAddress(), 10, 10, 10, Level.EASY);
    // Act
    db.insert(tSettings);
    // Assert
    assertEquals(db.find("name"), tSettings);
  }

  @Test
  void findAll() {
    // Arrange
    final var tSettings =
        new Settings("name", InetAddress.getLoopbackAddress(), 10, 10, 10, Level.EASY);
    final var tSettings2 =
        new Settings("name2", InetAddress.getLoopbackAddress(), 10, 10, 10, Level.EASY);
    db.insert(tSettings);
    db.insert(tSettings2);
    // Act
    final var result = db.findAll();
    // Assert
    assertEquals(result, List.of(tSettings, tSettings2));
  }

  @Test
  void find() {
    // Arrange
    final var tSettings =
        new Settings("name", InetAddress.getLoopbackAddress(), 10, 10, 10, Level.EASY);
    final var tSettings2 =
        new Settings("name2", InetAddress.getLoopbackAddress(), 10, 10, 10, Level.EASY);
    db.insert(tSettings);
    db.insert(tSettings2);
    // Act
    final var result = db.find("name");
    // Assert
    assertEquals(result, tSettings);
  }
}
