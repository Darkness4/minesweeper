package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

/**
 * A user should be able to load a saved settings.
 *
 * <p>Optional.empty() is returned if not found.
 */
@Singleton
public class LoadSettings implements UseCase<String, Maybe<Settings>> {

  private final Lazy<SettingsRepository> repository;

  @Inject
  public LoadSettings(Lazy<SettingsRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Maybe<Settings> execute(@NotNull String name) {
    return repository.get().findByKey(name).observeOn(Schedulers.from(IO.executor));
  }
}
