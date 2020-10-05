package marc.nguyen.minesweeper.client.data.database;

import dagger.Lazy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.data.datasources.LocalDataSource;
import marc.nguyen.minesweeper.client.domain.entities.GameMode;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.common.data.models.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class SettingsDaoSqlite implements SettingsDao {

  private final Lazy<LocalDataSource> dataSource;

  @Inject
  public SettingsDaoSqlite(Lazy<LocalDataSource> dataSource) {
    this.dataSource = dataSource;

    createTable();
  }

  /** Create "settings" SQL table. */
  public void createTable() {
    try (final var connection = dataSource.get().getConnection();
        final var statement =
            connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Settings "
                    + "(name           VARCHAR(128)    NOT NULL PRIMARY KEY,"
                    + " address        VARCHAR(128)    NOT NULL,"
                    + " port           INT             NOT NULL,"
                    + " length         INT             NOT NULL,"
                    + " height         INT             NOT NULL,"
                    + " mines          INT             NOT NULL,"
                    + " level          VARCHAR(20)     NOT NULL,"
                    + " mode           VARCHAR(20)     NOT NULL,"
                    + " player_name    VARCHAR(128)    NOT NULL);")) {

      statement.executeUpdate();
      System.out.println("Settings table initialized.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param name Name of the settings. Primary Key.
   */
  @Override
  public void deleteByName(@NotNull String name) {
    try (final var connection = dataSource.get().getConnection();
        final var statement = connection.prepareStatement("DELETE FROM Settings WHERE name = ?")) {
      statement.setString(1, name);

      final var result = statement.executeUpdate();
      System.out.printf("%d settings deleted.\n", result);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param settings Settings to be inserted.
   */
  @Override
  public void insert(@NotNull Settings settings) {
    try (final var connection = dataSource.get().getConnection();
        final var statement =
            connection.prepareStatement(
                "REPLACE INTO Settings (name, address, port, length, height, mines, level, mode, player_name) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
      statement.setString(1, settings.name);
      statement.setString(2, settings.address.getHostAddress());
      statement.setInt(3, settings.port);
      statement.setInt(4, settings.length);
      statement.setInt(5, settings.height);
      statement.setInt(6, settings.mines);
      statement.setString(7, settings.level.name());
      statement.setString(8, settings.mode.name());
      statement.setString(9, settings.playerName);

      final var result = statement.executeUpdate();
      System.out.printf("%d settings inserted.\n", result);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   *
   * @return A List of Settings. Empty if not found
   */
  @Override
  @NotNull
  public List<Settings> findAll() {
    final List<Settings> settings = new ArrayList<>();

    try (final var connection = dataSource.get().getConnection();
        final var statement = connection.prepareStatement("SELECT * FROM Settings")) {
      try (final var result = statement.executeQuery()) {
        if (!result.isBeforeFirst()) {
          System.out.println("No settings are stored.");
        }
        while (result.next()) {
          settings.add(
              new Settings(
                  result.getString("name"),
                  InetAddress.getByName(result.getString("address")),
                  result.getInt("port"),
                  result.getInt("length"),
                  result.getInt("height"),
                  result.getInt("mines"),
                  Level.valueOf(result.getString("level")),
                  GameMode.valueOf(result.getString("mode")),
                  result.getString("player_name")));
        }
      }
    } catch (SQLException | UnknownHostException e) {
      e.printStackTrace();
    }
    return settings;
  }

  /**
   * {@inheritDoc}
   *
   * @param name Name of the settings. Primary Key.
   * @return A Settings. If not found, is null.
   */
  @Override
  public @Nullable Settings findByName(@NotNull String name) {
    Settings settings = null;

    try (final var connection = dataSource.get().getConnection();
        final var statement =
            connection.prepareStatement("SELECT * FROM Settings WHERE name = ?")) {
      statement.setString(1, name);

      try (final var result = statement.executeQuery()) {
        if (!result.isBeforeFirst()) {
          System.out.println("No settings are stored.");
        } else {
          settings =
              new Settings(
                  result.getString("name"),
                  InetAddress.getByName(result.getString("address")),
                  result.getInt("port"),
                  result.getInt("length"),
                  result.getInt("height"),
                  result.getInt("mines"),
                  Level.valueOf(result.getString("level")),
                  GameMode.valueOf(result.getString("mode")),
                  result.getString("player_name"));
        }
      }
    } catch (SQLException | UnknownHostException e) {
      e.printStackTrace();
    }

    return settings;
  }
}
