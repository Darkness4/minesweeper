package marc.nguyen.minesweeper.client.domain.repositories;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import org.jetbrains.annotations.NotNull;

public interface SettingsRepository {

  @NotNull
  Completable save(@NotNull Settings settings);

  @NotNull
  Maybe<Settings> findByKey(@NotNull String name);

  @NotNull
  Single<List<Settings>> findAll();

  @NotNull
  Completable delete(@NotNull String name);
}
