package marc.nguyen.minesweeper.client.data.database;

import java.util.List;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Settings Data Access Object.
 *
 * <p>Interface to access saved local settings.
 */
public interface SettingsDao {

  /**
   * Delete a Settings from the DB based on the name.
   *
   * @param name Name of the settings. Primary Key.
   */
  void deleteByName(@NotNull String name);

  /**
   * Insert a Settings in the DB.
   *
   * @param settings Settings to be inserted.
   */
  void insert(@NotNull Settings settings);

  /** @return A List of Settings. Empty if not found. */
  @NotNull
  List<Settings> findAll();

  /**
   * @param name Name of the settings. Primary Key.
   * @return A Settings. If not found, is null.
   */
  @Nullable
  Settings findByName(@NotNull String name);
}
