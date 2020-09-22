package marc.nguyen.minesweeper.client.data.repositories;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dagger.Lazy;
import io.reactivex.rxjava3.observers.TestObserver;
import java.net.InetAddress;
import java.util.List;
import marc.nguyen.minesweeper.client.data.database.SettingsDao;
import marc.nguyen.minesweeper.client.domain.entities.GameMode;
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
        new Settings(
            "name",
            InetAddress.getLoopbackAddress(),
            12345,
            10,
            10,
            10,
            Level.EASY,
            GameMode.SINGLEPLAYER);
    final TestObserver<Settings> observer = new TestObserver<>();
    // Act
    repository.save(tSettings).subscribe(observer);
    // Assert
    observer.assertComplete();
    verify(settingsDao).insert(tSettings);
  }

  @Test
  void findByKey_found() {
    // Arrange
    final var tSettings =
        new Settings(
            "name",
            InetAddress.getLoopbackAddress(),
            12345,
            10,
            10,
            10,
            Level.EASY,
            GameMode.SINGLEPLAYER);
    when(settingsDao.findByName("name")).thenReturn(tSettings);
    final TestObserver<Settings> observer = new TestObserver<>();

    // Act
    repository.findByKey("name").subscribe(observer);

    // Assert
    observer.assertComplete();
    observer.assertValue(tSettings);
    verify(settingsDao).findByName("name");
  }

  @Test
  void findByKey_notFound() {
    // Arrange
    when(settingsDao.findByName("name")).thenReturn(null);
    final TestObserver<Settings> observer = new TestObserver<>();

    // Act
    repository.findByKey("name").subscribe(observer);

    // Assert
    verify(settingsDao).findByName("name");
    observer.assertComplete();
  }

  @Test
  void findAll() {
    // Arrange
    final var tSettingsList =
        List.of(
            new Settings(
                "name",
                InetAddress.getLoopbackAddress(),
                12345,
                10,
                10,
                10,
                Level.EASY,
                GameMode.SINGLEPLAYER));
    when(settingsDao.findAll()).thenReturn(tSettingsList);
    final TestObserver<List<Settings>> observer = new TestObserver<>();
    // Act
    repository.findAll().subscribe(observer);
    // Assert
    verify(settingsDao).findAll();
    observer.assertComplete();
    observer.assertValue(tSettingsList);
  }

  @Test
  void delete() {
    // Arrange
    final TestObserver<List<Settings>> observer = new TestObserver<>();
    // Act
    repository.delete("name").subscribe(observer);
    // Assert
    verify(settingsDao).deleteByName("name");
    observer.assertComplete();
  }
}
