package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

/** A user should be to save a new settings. */
@Singleton
public class SaveSettings implements UseCase<Settings, Completable> {

  private final Lazy<SettingsRepository> repository;

  @Inject
  public SaveSettings(Lazy<SettingsRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Completable execute(@NotNull Settings params) {
    return repository.get().save(params).observeOn(Schedulers.from(IO.executor));
  }
}
