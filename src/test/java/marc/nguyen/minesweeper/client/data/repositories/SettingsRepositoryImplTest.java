package marc.nguyen.minesweeper.client.data.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dagger.Lazy;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;
import marc.nguyen.minesweeper.client.data.database.SettingsDao;
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
  SettingsDao settingsDao;
  Lazy<SettingsDao> lazySettingsDao;

  @BeforeEach
  void setUp(@Mock SettingsDao settingsDao, @Mock Lazy<SettingsDao> lazySettingsDao) {
    this.settingsDao = settingsDao;
    this.lazySettingsDao = lazySettingsDao;
    repository = new SettingsRepositoryImpl(lazySettingsDao);

    when(lazySettingsDao.get()).thenReturn(settingsDao);
  }

  @Test
  void save() {
    // Arrange
    final var tSettings =
        new Settings("name", InetAddress.getLoopbackAddress(), 12345, 10, 10, 10, Level.EASY);
    // Act
    repository.save(tSettings);
    // Assert
    verify(settingsDao).insert(tSettings);
  }

  @Test
  void findByKey_found() {
    // Arrange
    final var tSettings =
        new Settings("name", InetAddress.getLoopbackAddress(), 12345, 10, 10, 10, Level.EASY);
    when(settingsDao.findByName("name")).thenReturn(Optional.of(tSettings));
    // Act
    final var result = repository.findByKey("name");
    // Assert
    verify(settingsDao).findByName("name");
    assertTrue(result.isPresent());
    assertEquals(result.get(), tSettings);
  }

  @Test
  void findByKey_notFound() {
    // Arrange
    when(settingsDao.findByName("name")).thenReturn(Optional.empty());
    // Act
    final var result = repository.findByKey("name");
    // Assert
    verify(settingsDao).findByName("name");
    assertTrue(result.isEmpty());
  }

  @Test
  void findAll() {
    // Arrange
    final var tSettingsList =
        List.of(
            new Settings("name", InetAddress.getLoopbackAddress(), 12345, 10, 10, 10, Level.EASY));
    when(settingsDao.findAll()).thenReturn(tSettingsList);
    // Act
    final var result = repository.findAll();
    // Assert
    verify(settingsDao).findAll();
    assertEquals(result, tSettingsList);
  }

  @Test
  void delete() {
    // Arrange
    // Act
    repository.delete("name");
    // Assert
    verify(settingsDao).deleteByName("name");
  }
}
