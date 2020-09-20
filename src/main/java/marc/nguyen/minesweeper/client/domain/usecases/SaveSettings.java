package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

/** A user should be to save a new settings. */
@Singleton
public class SaveSettings implements UseCase<Settings, Void> {
  private final Lazy<SettingsRepository> repository;

  @Inject
  public SaveSettings(Lazy<SettingsRepository> repository) {
    this.repository = repository;
  }

  @Override
  public Void execute(@NotNull Settings params) {
    repository.get().save(params);
    return null;
  }
}
