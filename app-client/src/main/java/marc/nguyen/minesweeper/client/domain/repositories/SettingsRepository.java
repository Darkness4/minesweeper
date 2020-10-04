package marc.nguyen.minesweeper.client.domain.repositories;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import org.jetbrains.annotations.NotNull;

/** Settings repository. */
public interface SettingsRepository {
  /**
   * Save a settings to the DB.
   *
   * @param settings Settings to be saved.
   * @return A completable.
   */
  @NotNull
  Completable save(@NotNull Settings settings);

  /**
   * Find the settings by name.
   *
   * @param name Name of the settings.
   * @return Maybe the settings.
   */
  @NotNull
  Maybe<Settings> findByKey(@NotNull String name);

  /**
   * Get the list of Settings.
   *
   * @return A Single List of Settings.
   */
  @NotNull
  Single<List<Settings>> findAll();

  /**
   * Delete the settings by name.
   *
   * @param name Name of the settings.
   * @return A completable.
   */
  @NotNull
  Completable delete(@NotNull String name);
}
