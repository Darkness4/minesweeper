package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
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
public class LoadSettings implements UseCase<String, Optional<Settings>> {

  final Lazy<SettingsRepository> repository;

  @Inject
  public LoadSettings(Lazy<SettingsRepository> repository) {
    this.repository = repository;
  }

  @Override
  public @NotNull Optional<Settings> execute(@NotNull String name) {
    return repository.get().findByKey(name);
  }
}
