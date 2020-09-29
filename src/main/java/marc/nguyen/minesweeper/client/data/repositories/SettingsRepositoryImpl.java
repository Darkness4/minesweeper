package marc.nguyen.minesweeper.client.data.repositories;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.data.database.SettingsDao;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

/** Implementation of SettingsRepository. */
@Singleton
public class SettingsRepositoryImpl implements SettingsRepository {

  private final Lazy<SettingsDao> dao;

  @Inject
  public SettingsRepositoryImpl(Lazy<SettingsDao> dao) {
    this.dao = dao;
  }

  /**
   * {@inheritDoc}
   *
   * @param settings Settings to be saved.
   * @return A completable.
   */
  @Override
  public @NotNull Completable save(@NotNull Settings settings) {
    return Completable.fromAction(() -> dao.get().insert(settings));
  }

  /**
   * {@inheritDoc}
   *
   * @param name Name of the settings.
   * @return Maybe the settings.
   */
  @Override
  public @NotNull Maybe<Settings> findByKey(@NotNull String name) {
    return Maybe.just(dao.get())
        .flatMap(
            (dao) -> {
              final var result = dao.findByName(name);
              if (result != null) {
                return Maybe.just(result);
              } else {
                return Maybe.empty();
              }
            });
  }

  /**
   * Get the list of Settings.
   *
   * @return A Single List of Settings.
   */
  @Override
  public @NotNull Single<List<Settings>> findAll() {
    return Single.fromCallable(() -> dao.get().findAll());
  }

  /**
   * Delete the settings by name.
   *
   * @param name Name of the settings.
   * @return A completable.
   */
  @Override
  public @NotNull Completable delete(@NotNull String name) {
    return Completable.fromAction(() -> dao.get().deleteByName(name));
  }
}
