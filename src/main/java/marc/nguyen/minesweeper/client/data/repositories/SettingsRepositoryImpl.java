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

  @Override
  public @NotNull Completable save(@NotNull Settings settings) {
    return Completable.fromAction(() -> dao.get().insert(settings));
  }

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

  @Override
  public @NotNull Single<List<Settings>> findAll() {
    return Single.fromCallable(() -> dao.get().findAll());
  }

  @Override
  public @NotNull Completable delete(@NotNull String name) {
    return Completable.fromAction(() -> dao.get().deleteByName(name));
  }
}
