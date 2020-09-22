package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

/** A user should be able to delete a saved settings. */
@Singleton
public class DeleteSettings implements UseCase<String, Completable> {

  private final Lazy<SettingsRepository> repository;

  @Inject
  public DeleteSettings(Lazy<SettingsRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Completable execute(@NotNull String name) {
    return repository.get().delete(name).observeOn(Schedulers.from(IO.executor));
  }
}
