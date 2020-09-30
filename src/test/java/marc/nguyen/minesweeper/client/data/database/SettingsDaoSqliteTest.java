package marc.nguyen.minesweeper.client.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import dagger.Lazy;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import marc.nguyen.minesweeper.client.data.datasources.LocalDataSource;
import marc.nguyen.minesweeper.client.data.datasources.LocalDataSourceMock;
import marc.nguyen.minesweeper.client.domain.entities.GameMode;
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
  LocalDataSourceMock localDataSourceMock;
  Lazy<LocalDataSource> localDataSourceLazy;
  Connection connection;

  @BeforeEach
  void setUp(@Mock Lazy<LocalDataSource> localDataSourceLazy) throws SQLException {
    this.localDataSourceMock = new LocalDataSourceMock();
    this.localDataSourceLazy = localDataSourceLazy;
    when(localDataSourceLazy.get()).thenReturn(localDataSourceMock);
    settingsDao = new SettingsDaoSqlite(localDataSourceLazy);

    connection = localDataSourceMock.getConnection();
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
        new Settings(
            "name",
            InetAddress.getLoopbackAddress(),
            1,
            2,
            3,
            4,
            Level.EASY,
            GameMode.SINGLEPLAYER,
            "playerName"));

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
        new Settings(
            "name",
            InetAddress.getLoopbackAddress(),
            1,
            2,
            3,
            4,
            Level.EASY,
            GameMode.SINGLEPLAYER,
            "playerName"));

    // Assert
    final var statement = connection.prepareStatement("SELECT COUNT(*) FROM settings");
    final var rs = statement.executeQuery();
    assertEquals(rs.getInt(1), 1);
  }

  @Test
  void findAll() {
    // Arrange
    final var tSettings =
        new Settings(
            "name",
            InetAddress.getLoopbackAddress(),
            1,
            2,
            3,
            4,
            Level.EASY,
            GameMode.SINGLEPLAYER,
            "playerName");
    final var tSettings2 =
        new Settings(
            "name2",
            InetAddress.getLoopbackAddress(),
            1,
            2,
            3,
            4,
            Level.EASY,
            GameMode.SINGLEPLAYER,
            "playerName");
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
        new Settings(
            "name",
            InetAddress.getLoopbackAddress(),
            1,
            2,
            3,
            4,
            Level.EASY,
            GameMode.SINGLEPLAYER,
            "playerName");
    settingsDao.insert(tSettings);

    // Act
    final var result = settingsDao.findByName("name");

    // Assert
    assertEquals(result, tSettings);
  }

  @Test
  void findByName_notFound() {
    // Act
    final var result = settingsDao.findByName("name");

    // Assert
    assertNull(result);
  }

  @AfterEach
  void cleanUp() throws SQLException {
    connection.close();
    localDataSourceMock.purge();
  }
}
