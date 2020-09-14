package marc.nguyen.minesweeper.client.data.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.InetAddress;
import java.util.List;
import marc.nguyen.minesweeper.client.data.database.SettingsDb;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import marc.nguyen.minesweeper.common.data.models.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingsRepositoryImplTest {

  SettingsRepository repository;
  SettingsDb settingsDb;

  @BeforeEach
  void setUp(@Mock SettingsDb settingsDb) {
    this.settingsDb = settingsDb;
    repository = new SettingsRepositoryImpl(settingsDb);
  }

  @Test
  void save() {
    // Arrange
    final var tSettings =
        new Settings("name", InetAddress.getLoopbackAddress(), 10, 10, 10, Level.EASY);
    // Act
    repository.save(tSettings);
    // Assert
    verify(settingsDb).insert(tSettings);
  }

  @Test
  void findByKey() {
    // Arrange
    final var tSettings =
        new Settings("name", InetAddress.getLoopbackAddress(), 10, 10, 10, Level.EASY);
    when(settingsDb.find("name")).thenReturn(tSettings);
    // Act
    final var result = repository.findByKey("name");
    // Assert
    verify(settingsDb).find("name");
    assertEquals(result, tSettings);
  }

  @Test
  void findAll() {
    // Arrange
    final var tSettingsList =
        List.of(new Settings("name", InetAddress.getLoopbackAddress(), 10, 10, 10, Level.EASY));
    when(settingsDb.findAll()).thenReturn(tSettingsList);
    // Act
    final var result = repository.findAll();
    // Assert
    verify(settingsDb).findAll();
    assertEquals(result, tSettingsList);
  }

  @Test
  void delete() {
    // Arrange
    // Act
    repository.delete("name");
    // Assert
    verify(settingsDb).delete("name");
  }
}
