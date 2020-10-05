package marc.nguyen.minesweeper.client.data.database;

import dagger.Lazy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.data.datasources.LocalDataSource;
import marc.nguyen.minesweeper.client.domain.entities.HighScore;
import org.jetbrains.annotations.NotNull;

public class HighScoreDaoSqlite implements HighScoreDao {

  private final Lazy<LocalDataSource> dataSource;

  @Inject
  public HighScoreDaoSqlite(Lazy<LocalDataSource> dataSource) {
    this.dataSource = dataSource;

    createTable();
  }

  /** Create "settings" SQL table. */
  public void createTable() {
    try (final var connection = dataSource.get().getConnection();
        final var statement =
            connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS HighScore "
                    + "(id               ROWID,"
                    + " name             VARCHAR(128)    NOT NULL,"
                    + " score            INT             NOT NULL,"
                    + " minefield_length INT             NOT NULL,"
                    + " minefield_height INT             NOT NULL,"
                    + " mines            INT             NOT NULL);")) {

      statement.executeUpdate();
      System.out.println("Player table initialized.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void insert(@NotNull HighScore highScore) {
    try (final var connection = dataSource.get().getConnection();
        final var statement =
            connection.prepareStatement(
                "REPLACE INTO HighScore (name, score, minefield_length, minefield_height, mines) "
                    + "VALUES (?, ?, ?, ?, ?);")) {
      statement.setString(1, highScore.playerName);
      statement.setInt(2, highScore.score);
      statement.setInt(3, highScore.minefieldLength);
      statement.setInt(4, highScore.minefieldHeight);
      statement.setInt(5, highScore.mines);

      final var result = statement.executeUpdate();
      System.out.printf("%d player inserted.\n", result);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public @NotNull List<HighScore> findAll() {
    final List<HighScore> highScores = new ArrayList<>();

    try (final var connection = dataSource.get().getConnection();
        final var statement = connection.prepareStatement("SELECT * FROM HighScore")) {
      try (final var result = statement.executeQuery()) {
        if (!result.isBeforeFirst()) {
          System.out.println("No high score are stored.");
        }
        while (result.next()) {
          highScores.add(
              new HighScore(
                  result.getString("name"),
                  result.getInt("score"),
                  result.getInt("minefield_length"),
                  result.getInt("minefield_height"),
                  result.getInt("mines")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return highScores;
  }
}
