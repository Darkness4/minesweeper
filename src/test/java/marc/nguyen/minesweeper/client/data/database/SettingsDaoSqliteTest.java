package marc.nguyen.minesweeper.client.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import dagger.Lazy;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import marc.nguyen.minesweeper.client.data.datasources.LocalDataSource;
import marc.nguyen.minesweeper.client.data.datasources.MockLocalDataSource;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.common.data.models.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingsDaoSqliteTest {

  SettingsDao settingsDao;
  MockLocalDataSource mockLocalDataSource;
  Lazy<LocalDataSource> localDataSourceLazy;
  Connection connection;

  @BeforeEach
  void setUp(@Mock Lazy<LocalDataSource> localDataSourceLazy) throws SQLException {
    this.mockLocalDataSource = new MockLocalDataSource();
    this.localDataSourceLazy = localDataSourceLazy;
    when(localDataSourceLazy.get()).thenReturn(mockLocalDataSource);
    settingsDao = new SettingsDaoSqlite(localDataSourceLazy);

    connection = mockLocalDataSource.getConnection();
  }

  @Test
  void createTable() throws SQLException {
    // Assert
    final var metaData = connection.getMetaData();
    final var rs = metaData.getTables(null, null, "settings", null);
    assertTrue(rs.isBeforeFirst());
  }

  @Test
  void deleteByName() throws SQLException {
    // Arrange
    settingsDao.insert(
        new Settings("name", InetAddress.getLoopbackAddress(), 1, 2, 3, 4, Level.EASY));

    // Act
    settingsDao.deleteByName("name");

    // Assert
    final var statement = connection.prepareStatement("SELECT COUNT(*) FROM settings");
    final var rs = statement.executeQuery();
    assertEquals(rs.getInt(1), 0);
  }

  @Test
  void insert() throws SQLException {
    // Act
    settingsDao.insert(
        new Settings("name", InetAddress.getLoopbackAddress(), 1, 2, 3, 4, Level.EASY));

    // Assert
    final var statement = connection.prepareStatement("SELECT COUNT(*) FROM settings");
    final var rs = statement.executeQuery();
    assertEquals(rs.getInt(1), 1);
  }

  @Test
  void findAll() {
    // Arrange
    final var tSettings =
        new Settings("name", InetAddress.getLoopbackAddress(), 1, 2, 3, 4, Level.EASY);
    final var tSettings2 =
        new Settings("name2", InetAddress.getLoopbackAddress(), 1, 2, 3, 4, Level.EASY);
    settingsDao.insert(tSettings);
    settingsDao.insert(tSettings2);

    // Act
    final var result = settingsDao.findAll();

    // Assert
    assertEquals(result, List.of(tSettings, tSettings2));
  }

  @Test
  void findByName_found() {
    // Arrange
    final var tSettings =
        new Settings("name", InetAddress.getLoopbackAddress(), 1, 2, 3, 4, Level.EASY);
    settingsDao.insert(tSettings);

    // Act
    final var result = settingsDao.findByName("name");

    // Assert
    assertTrue(result.isPresent());
    assertEquals(result.get(), tSettings);
  }

  @Test
  void findByName_notFound() {
    // Act
    final var result = settingsDao.findByName("name");

    // Assert
    assertTrue(result.isEmpty());
  }

  @AfterEach
  void cleanUp() throws SQLException {
    connection.close();
    mockLocalDataSource.purge();
  }
}