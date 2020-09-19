package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

/** A user should be able to delete a saved settings. */
@Singleton
public class DeleteSettings implements UseCase<String, Void> {

  final Lazy<SettingsRepository> repository;

  @Inject
  public DeleteSettings(Lazy<SettingsRepository> repository) {
    this.repository = repository;
  }

  @Override
  public Void execute(@NotNull String name) {
    repository.get().delete(name);
    return null;
  }
}
