package marc.nguyen.minesweeper.client.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import dagger.Lazy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import marc.nguyen.minesweeper.client.data.datasources.LocalDataSource;
import marc.nguyen.minesweeper.client.data.datasources.LocalDataSourceMock;
import marc.nguyen.minesweeper.client.domain.entities.HighScore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighScoreDaoSqliteTest {

  HighScoreDao highScoreDao;
  LocalDataSourceMock localDataSourceMock;
  Lazy<LocalDataSource> localDataSourceLazy;
  Connection connection;

  @BeforeEach
  void setUp(@Mock Lazy<LocalDataSource> localDataSourceLazy) throws SQLException {
    this.localDataSourceMock = new LocalDataSourceMock();
    this.localDataSourceLazy = localDataSourceLazy;
    when(localDataSourceLazy.get()).thenReturn(localDataSourceMock);
    highScoreDao = new HighScoreDaoSqlite(localDataSourceLazy);

    connection = localDataSourceMock.getConnection();
  }

  @Test
  void createTable() throws SQLException {
    // Assert
    final var metaData = connection.getMetaData();
    final var rs = metaData.getTables(null, null, "HighScore", null);
    assertTrue(rs.isBeforeFirst());
  }

  @Test
  void insert() throws SQLException {
    // Act
    highScoreDao.insert(new HighScore("name", 1, 2, 3, 4));

    // Assert
    final var statement = connection.prepareStatement("SELECT COUNT(*) FROM HighScore");
    final var rs = statement.executeQuery();
    assertEquals(rs.getInt(1), 1);
  }

  @Test
  void findAll() {
    // Arrange
    final var tHighScore1 = new HighScore("name", 1, 2, 3, 4);
    final var tHighScore2 = new HighScore("name2", 1, 2, 3, 4);
    highScoreDao.insert(tHighScore1);
    highScoreDao.insert(tHighScore2);

    // Act
    final var result = highScoreDao.findAll();

    // Assert
    assertEquals(result, List.of(tHighScore1, tHighScore2));
  }

  @AfterEach
  void cleanUp() throws SQLException {
    connection.close();
    localDataSourceMock.purge();
  }
}
