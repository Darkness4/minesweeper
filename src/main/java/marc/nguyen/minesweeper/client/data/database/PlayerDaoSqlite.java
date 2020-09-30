package marc.nguyen.minesweeper.client.data.database;

import dagger.Lazy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.data.datasources.LocalDataSource;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerDaoSqlite implements PlayerDao {

  private final Lazy<LocalDataSource> dataSource;

  @Inject
  public PlayerDaoSqlite(Lazy<LocalDataSource> dataSource) {
    this.dataSource = dataSource;

    createTable();
  }

  /** Create "settings" SQL table. */
  public void createTable() {
    try (final var connection = dataSource.get().getConnection();
        final var statement =
            connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS players "
                    + "(id             ROWID,"
                    + " name           VARCHAR(128)    NOT NULL,"
                    + " score          INT             NOT NULL);")) {

      statement.executeUpdate();
      System.out.println("Player table initialized.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void insert(@NotNull Player player) {
    try (final var connection = dataSource.get().getConnection();
        final var statement =
            connection.prepareStatement("REPLACE INTO players (name, score) " + "VALUES (?, ?);")) {
      statement.setString(1, player.name);
      statement.setInt(2, player.getScore());

      final var result = statement.executeUpdate();
      System.out.printf("%d player inserted.\n", result);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public @NotNull List<Player> findAll() {
    final List<Player> players = new ArrayList<>();

    try (final var connection = dataSource.get().getConnection();
        final var statement = connection.prepareStatement("SELECT * FROM players")) {
      try (final var result = statement.executeQuery()) {
        if (!result.isBeforeFirst()) {
          System.out.println("No settings are stored.");
        }
        while (result.next()) {
          players.add(new Player(result.getString("name"), result.getInt("score")));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return players;
  }

  @Override
  public @Nullable Player findByName(@NotNull String name) {
    Player player = null;

    try (final var connection = dataSource.get().getConnection();
        final var statement = connection.prepareStatement("SELECT * FROM players WHERE name = ?")) {
      statement.setString(1, name);

      try (final var result = statement.executeQuery()) {
        if (!result.isBeforeFirst()) {
          System.out.println("No player is stored.");
        } else {
          player = new Player(result.getString("name"), result.getInt("score"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return player;
  }
}
